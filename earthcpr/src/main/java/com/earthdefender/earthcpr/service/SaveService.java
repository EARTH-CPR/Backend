package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.ChallengeDTO;
import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.DTO.SavingsAccountDTO;
import com.earthdefender.earthcpr.DTO.ShinhanApiDTO;
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

    public void createSavingsAccount(SavingsAccountDTO.ProductData productData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        Optional<User> userOptional = userRepository.findById(userId);
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
                SavingsAccountDTO.CreateAccountResponse.class,
                session
        );
        try {
            SavingsAccountDTO.CreateAccountResponse response = responseMono.block();
            SavingsAccount savingsAccount = SavingsAccount.builder()
                    .user(userOptional.get())
                    .savingProduct(productOptional.get())
                    .account_no(response.getRec().getAccountNo())
                    .additional_interest_rate(Double.parseDouble(response.getRec().getInterestRate()))
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
    public SavingsAccountDTO.ProductData getSavingProductDetail(SavingsAccountDTO.ProductData productData,HttpSession session) {
        Mono<SavingsAccountDTO.ShinhanApiInquireAccountResponse> shinhanApiResponseMono
                = apiService.PostRequestUserKey("/edu/savings/inquireAccount", productData.toInquireAccountRequest(), SavingsAccountDTO.ShinhanApiInquireAccountResponse.class, session);
        try {
            SavingsAccountDTO.ShinhanApiInquireAccountResponse shinhanApiResponse = shinhanApiResponseMono.block();
            return shinhanApiResponse.getRec().toProductData();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}
