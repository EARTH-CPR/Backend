package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.ChallengeDTO;
import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.DTO.ShinhanApiDTO;
import com.earthdefender.earthcpr.model.Challenge;
import com.earthdefender.earthcpr.model.SavingsProduct;
import com.earthdefender.earthcpr.repository.ChallengeRepository;
import com.earthdefender.earthcpr.repository.SavingsProductRepository;

import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveService {

    private final SavingsProductRepository savingsProductRepository;
    private final ChallengeRepository challengeRepository;
    private final ApiService apiService;

    @Transactional
    public ResponseEntity<ApiResponseEntity> createSavingsProduct(SavingProductDTO.ProductData productData) {

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
                    .interestInterestRate(productData.getIncreaseInterestRate())
                    .challengeList(challegeList)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return ApiResponseEntity.toResponseEntity();
    }

    public SavingProductDTO.GetSavingProductsResponse getSavingProductList() {
        SavingProductDTO.GetSavingProductsResponse response = new SavingProductDTO.GetSavingProductsResponse();
        response.setProducts(new ArrayList<>());
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
                    response.getProducts().add(shinhanApiResponseData.toProductData(savingsProduct.getInterestInterestRate(), challengeList));
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        return response;
    }
}
