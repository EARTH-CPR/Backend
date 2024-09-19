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

    //적금 상품 생성
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

    //적금 계좌 생성
    public void createSavingsAccount(SavingsAccountDTO.CreateAccountData createAccountData) {
        Optional<User> userOptional = userRepository.findByLoginId(createAccountData.getLoginId());
        if (userOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        Optional<SavingsProduct> productOptional = savingsProductRepository.findByAccountTypeUniqueNo(createAccountData.getAccountTypeUniqueNo());
        if (productOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        Mono<SavingsAccountDTO.CreateAccountResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/savings/createAccount",
                createAccountData.toCreateAccountRequest(),
                SavingsAccountDTO.CreateAccountResponse.class
                , createAccountData.getLoginId()
        );
        try {
            SavingsAccountDTO.CreateAccountResponse response = responseMono.block();
            SavingsAccount savingsAccount = SavingsAccount.builder()
                    .user(userOptional.get())
                    .savingProduct(productOptional.get())
                    .accountNo(response.getRec().getAccountNo())
                    .additional_interest_rate(Double.parseDouble(response.getRec().getInterestRate()))
                    .withdrawalAccountNo(createAccountData.getWithdrawalAccountNo())
                    .build();
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

    //적금 계좌 목록 가져오기
    public List<SavingsAccountDTO.SavingAccountListResponse> getSavingAccountList(SavingsAccountDTO.LoginIdData loginIdData) {
        Mono<SavingsAccountDTO.ShinhanApiSavingAccountListResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquireAccountList",new ShinhanApiDTO.RequestHeader(), SavingsAccountDTO.ShinhanApiSavingAccountListResponse.class, loginIdData.getLoginId());

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

    //적금 계좌조회
    public SavingsAccountDTO.InquireAccountResponseData getSavingProductDetail(SavingsAccountDTO.InquireAccountData inquireAccountData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireAccountResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquireAccount", inquireAccountData.toInquireAccountRequest(), SavingsAccountDTO.ShinhanApiInquireAccountResponse.class, inquireAccountData.getLoginId());
        try {
            SavingsAccountDTO.ShinhanApiInquireAccountResponse shinhanApiResponse = shinhanApiResponseMono.block();
            return shinhanApiResponse.getRec().toInquireAccountResponseData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    //적금 납입회차조회
    public List<SavingsAccountDTO.PaymentInfo> getPaymentList(SavingsAccountDTO.InquirePaymentData inquirePaymentData) {
        Mono<SavingsAccountDTO.ShinhanApiInquirePaymentResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquirePayment", inquirePaymentData.toInquirePaymentRequest(), SavingsAccountDTO.ShinhanApiInquirePaymentResponse.class, inquirePaymentData.getLoginId());
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

    //만기이자 조회
    public SavingsAccountDTO.InquireExpiryResponseData inquireExpiryInterest(SavingsAccountDTO.InquireExpiryData inquireExpiryData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireExpiryResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/savings/inquireExpiryInterest",
                inquireExpiryData.toInquireExpiryRequest(),
                SavingsAccountDTO.ShinhanApiInquireExpiryResponse.class,
                inquireExpiryData.getLoginId()
        );

        try {
            SavingsAccountDTO.ShinhanApiInquireExpiryResponse response = responseMono.block();
            return response.getRec().toInquireExpiryResponseData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    //중도해지 이자 조회
    public SavingsAccountDTO.InquireEarlyResponseData inquireEarlyInterest(SavingsAccountDTO.InquireEarlyData inquireEarlyData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireEarlyResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/savings/inquireEarlyTerminationInterest",
                inquireEarlyData.toInquireEarlyRequest(),
                SavingsAccountDTO.ShinhanApiInquireEarlyResponse.class,
                inquireEarlyData.getLoginId()
        );

        try {
            SavingsAccountDTO.ShinhanApiInquireEarlyResponse response = responseMono.block();
            return response.getRec().toInquireEarlyResponseData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
    //중도해지
    public SavingsAccountDTO.DeleteAccountResponseData deleteAccount(SavingsAccountDTO.InquireEarlyData inquireEarlyData) {
        Mono<SavingsAccountDTO.ShinhanApiInquireEarlyResponse> responseMono1 = apiService.PostRequestUserKey(
                "/edu/savings/inquireEarlyTerminationInterest",
                inquireEarlyData.toInquireEarlyRequest(),
                SavingsAccountDTO.ShinhanApiInquireEarlyResponse.class,
                inquireEarlyData.getLoginId()
        );
        try {
            SavingsAccountDTO.ShinhanApiInquireEarlyResponse response1 = responseMono1.block();
            SavingsAccountDTO.InquireEarlyResponseData SaveProductData= response1.getRec().toInquireEarlyResponseData();
            int additionalInterestRate = (int)(Double.parseDouble(SaveProductData.getEarlyTerminationInterest()) /10 * savingsAccountRepository.findByAccountNo(SaveProductData.getAccountNo()).get().getAdditional_interest_rate());
            DemandDepositAccountDTO.TransferRequestData depositProductData = DemandDepositAccountDTO.TransferRequestData.builder()
                    .loginId(inquireEarlyData.getLoginId())
                    .depositAccountNo(savingsAccountRepository.findByAccountNo(SaveProductData.getAccountNo()).get().getWithdrawalAccountNo())
                    .transactionBalance(additionalInterestRate+"")
                    .withdrawalAccountNo("0013483313292281")
                    .depositTransactionSummary("우대금리 이자")
                    .withdrawalTransactionSummary("우대금리 이자")
                    .build();
            demandDepositService.transferDepositAccount(depositProductData);



            Mono<SavingsAccountDTO.ShinhanApideleteAccountResponse> responseMono2 = apiService.PostRequestUserKey(
                    "/edu/savings/deleteAccount",
                    inquireEarlyData.toInquireEarlyRequest(),
                    SavingsAccountDTO.ShinhanApideleteAccountResponse.class,
                    inquireEarlyData.getLoginId()
            );

            SavingsAccountDTO.ShinhanApideleteAccountResponse response2 = responseMono2.block();
            return response2.getRec().toDeleteAccountResponseData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

    }


}
