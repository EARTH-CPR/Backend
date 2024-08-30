package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.*;
import com.earthdefender.earthcpr.model.Challenge;
import com.earthdefender.earthcpr.model.SavingsAccount;
import com.earthdefender.earthcpr.model.SavingsProduct;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.ChallengeRepository;
import com.earthdefender.earthcpr.repository.SavingsAccountRepository;
import com.earthdefender.earthcpr.repository.SavingsProductRepository;

import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import com.mysql.cj.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveService {

    private final SavingsProductRepository savingsProductRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final ApiService apiService;
    private final DemandDepositService demandDepositService;


    @Transactional
    public void createSavingsProduct(SavingProductDTO.ProductData productData) {

        List<Challenge> challegeList = new ArrayList<>();
        Mono<SavingProductDTO.ShinhanApiCreateResponse> shinhanApiResponseMono
                = apiService.postRequest("/edu/savings/createProduct", productData.toShinhanApiRequest(), SavingProductDTO.ShinhanApiCreateResponse.class);

        if (productData.getChallengeList() != null) {
            for (ChallengeDTO.ChallengeData challengeData : productData.getChallengeList()) {
                Optional<Challenge> challenge = challengeRepository.findById(challengeData.getId());
                if (challenge.isPresent()) {
                    challegeList.add(challenge.get());
                }
            }
        }

        try {
            // blocking 하는 과정에 대해서 만약 API 응답이 error 라면 예외처리
            SavingProductDTO.ShinhanApiCreateResponse shinhanApiCreateResponse = shinhanApiResponseMono.block();
            savingsProductRepository.save(SavingsProduct.builder()
                    .accountTypeUniqueNo(shinhanApiCreateResponse.getRec().getAccountTypeUniqueNo())
                    .interestInterestRate(new BigDecimal(productData.getIncreaseInterestRate()))
                    .challengeList(challegeList)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    public void createSavingsAccount(SavingsAccountDTO.ProductData productData) {
        Optional<User> userOptional = userRepository.findByLoginId(productData.getLoginId());
        if (userOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        System.out.println("productData: " + productData);
        Optional<SavingsProduct> productOptional = savingsProductRepository.findByAccountTypeUniqueNo(productData.getAccountTypeUniqueNo());
        if (productOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        Mono<SavingsAccountDTO.CreateAccountResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/savings/createAccount",
                productData.toCreateAccountRequest(),
                SavingsAccountDTO.CreateAccountResponse.class
                , productData.getLoginId()
        );
        try {
            SavingsAccountDTO.CreateAccountResponse response = responseMono.block();
            SavingsAccount savingsAccount = SavingsAccount.builder()
                    .user(userOptional.get())
                    .savingProduct(productOptional.get())
                    .accountNo(response.getRec().getAccountNo())
                    .additional_interest_rate(Double.parseDouble(response.getRec().getInterestRate()))
                    .withdrawalAccountNo(productData.getWithdrawalAccountNo())
                    .build();
            System.out.println("savingsAccount: " + savingsAccount);
            savingsAccountRepository.save(savingsAccount);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    //적금 상품 목록 가져오기
    public List<SavingProductDTO.ProductData> getSavingProductList() {
        List<SavingProductDTO.ProductData> response = new ArrayList<>();
        Mono<SavingProductDTO.ShinhanApiGetSavingProductsResponse> shinhanApiResponseMono
                = apiService.postRequest("/edu/savings/inquireSavingsProducts", new ShinhanApiDTO.RequestHeader(), SavingProductDTO.ShinhanApiGetSavingProductsResponse.class);

        try {
            SavingProductDTO.ShinhanApiGetSavingProductsResponse shinhanApiResponse = shinhanApiResponseMono.block();
            // API에서 가져온 상품 중 DB에 있는 정보를 Response에 저장
            for (SavingProductDTO.ShinhanApiResponseData shinhanApiResponseData : shinhanApiResponse.getRec()) {
                Optional<SavingsProduct> savingsProductOptional = savingsProductRepository
                        .findByAccountTypeUniqueNo(shinhanApiResponseData.getAccountTypeUniqueNo());
                if (savingsProductOptional.isPresent()) {
                    SavingsProduct savingsProduct = savingsProductOptional.get();
                    List<ChallengeDTO.ChallengeData> challengeList = new ArrayList<>();
                    // challenge model을 DTO로 변경
                    for (Challenge challenge : savingsProduct.getChallengeList()) {
                        challengeList.add(ChallengeDTO.ChallengeData.builder()
                                        .id(challenge.getId())
                                        .name(challenge.getName())
                                        .info(challenge.getInfo())
                                        .type(challenge.getType())
                                        .verification(challenge.getVerification())
                                .build());
                    }
                    response.add(shinhanApiResponseData.toProductData(savingsProduct.getInterestInterestRate().toString(), challengeList));
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        return response;
    }
    //적금 상세 정보 가져오기
    public SavingsAccountDTO.ProductData getSavingProductDetail(SavingsAccountDTO.ProductData productData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireAccountResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquireAccount", productData.toInquireAccountRequest(), SavingsAccountDTO.ShinhanApiInquireAccountResponse.class, productData.getLoginId());
        try {
            SavingsAccountDTO.ShinhanApiInquireAccountResponse shinhanApiResponse = shinhanApiResponseMono.block();
            return shinhanApiResponse.getRec().toProductData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    //적금 입금 내역 가져오기
    public List<SavingsAccountDTO.PaymentInfo> getPaymentList(SavingsAccountDTO.ProductData productData) {
        Mono<SavingsAccountDTO.ShinhanApiInquirePaymentResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquirePayment", productData.toInquirePaymentRequest(), SavingsAccountDTO.ShinhanApiInquirePaymentResponse.class, productData.getLoginId());
        try {
            SavingsAccountDTO.ShinhanApiInquirePaymentResponse shinhanApiResponse = shinhanApiResponseMono.block();
            List<SavingsAccountDTO.PaymentInfo> response = new ArrayList<>();
            for (SavingsAccountDTO.PaymentInfo paymentInfo : shinhanApiResponse.getRec().get(0).getPaymentInfo()) {
                response.add(paymentInfo);
            }
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

    }

    public SavingsAccountDTO.ProductData inquireExpiryInterest(SavingsAccountDTO.ProductData productData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireExpiryResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/savings/inquireExpiryInterest",
                productData.toInquireExpiryRequest(),
                SavingsAccountDTO.ShinhanApiInquireExpiryResponse.class,
                productData.getLoginId()
        );

        try {
            SavingsAccountDTO.ShinhanApiInquireExpiryResponse response = responseMono.block();
            return response.getRec().toProductData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
    public SavingsAccountDTO.ProductData inquireEarlyInterest(SavingsAccountDTO.ProductData productData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireEarlyResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/savings/inquireEarlyTerminationInterest",
                productData.toInquireEarlyRequest(),
                SavingsAccountDTO.ShinhanApiInquireEarlyResponse.class,
                productData.getLoginId()
        );

        try {
            SavingsAccountDTO.ShinhanApiInquireEarlyResponse response = responseMono.block();
            return response.getRec().toProductData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
    public SavingsAccountDTO.ProductData deleteAccount(SavingsAccountDTO.ProductData productData) {
        System.out.println("productData: " + productData);
        Mono<SavingsAccountDTO.ShinhanApiInquireEarlyResponse> responseMono1 = apiService.PostRequestUserKey(
                "/edu/savings/inquireEarlyTerminationInterest",
                productData.toInquireEarlyRequest(),
                SavingsAccountDTO.ShinhanApiInquireEarlyResponse.class,
                productData.getLoginId()
        );
        try {
            SavingsAccountDTO.ShinhanApiInquireEarlyResponse response1 = responseMono1.block();
            SavingsAccountDTO.ProductData SaveProductData= response1.getRec().toProductData();
            int additionalInterestRate = (int)(Double.parseDouble(SaveProductData.getEarlyTerminationInterest()) /10 * savingsAccountRepository.findByAccountNo(SaveProductData.getAccountNo()).get().getAdditional_interest_rate());
            DemandDepositAccountDTO.ProductData depositProductData = DemandDepositAccountDTO.ProductData.builder()
                    .loginId(productData.getLoginId())
                    .depositAccountNo(savingsAccountRepository.findByAccountNo(SaveProductData.getAccountNo()).get().getWithdrawalAccountNo())
                    .transactionBalance(additionalInterestRate+"")
                    .withdrawalAccountNo("0013483313292281")
                    .depositTransactionSummary("우대금리 이자")
                    .withdrawalTransactionSummary("우대금리 이자")
                    .build();
            System.out.println("SaveProductData: " + SaveProductData);
            System.out.println("depositProductData: " + depositProductData);
            demandDepositService.transferDepositAccount(depositProductData);



            Mono<SavingsAccountDTO.ShinhanApideleteAccountResponse> responseMono2 = apiService.PostRequestUserKey(
                    "/edu/savings/deleteAccount",
                    productData.toDeleteAccountRequest(),
                    SavingsAccountDTO.ShinhanApideleteAccountResponse.class,
                    productData.getLoginId()
            );

            SavingsAccountDTO.ShinhanApideleteAccountResponse response2 = responseMono2.block();
            return response2.getRec().toProductData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

    }
    //적금 계좌 목록 가져오기
    public List<SavingsAccountDTO.SavingAccountListResponse> getSavingAccountList(SavingsAccountDTO.ProductData productData) {
        Mono<SavingsAccountDTO.ShinhanApiSavingAccountListResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquireAccountList",new ShinhanApiDTO.RequestHeader(), SavingsAccountDTO.ShinhanApiSavingAccountListResponse.class, productData.getLoginId());

        try {
            SavingsAccountDTO.ShinhanApiSavingAccountListResponse shinhanApiResponse = shinhanApiResponseMono.block();
            List<SavingsAccountDTO.SavingAccountListResponse> response = new ArrayList<>();
            for (SavingsAccountDTO.SavingAccountListResponse savingAccountListResponse : shinhanApiResponse.getRec().getList()) {
                response.add(savingAccountListResponse);
            }
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

}
