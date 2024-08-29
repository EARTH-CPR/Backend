package com.earthdefender.earthcpr;


import com.earthdefender.earthcpr.DTO.ChallengeDTO;
import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.controller.SaveController;
import com.earthdefender.earthcpr.restdocs.AbstractRestDocsTests;
import com.earthdefender.earthcpr.service.SaveService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SaveController.class)
public class SaveControllerTest extends AbstractRestDocsTests {

    @MockBean
    SaveService saveService;

    @Test
    void CreateSavingsProductTest() throws Exception {
        List<ChallengeDTO.ChallengeData> challengeDataList = new ArrayList<>();
        challengeDataList.add(ChallengeDTO.ChallengeData.builder()
                .id(Long.valueOf(1))
                .build());
        challengeDataList.add(ChallengeDTO.ChallengeData.builder()
                .id(Long.valueOf(2))
                .build());

        SavingProductDTO.ProductData productData = SavingProductDTO.ProductData.builder()
                .bankCode("001")
                .accountName("7일적금")
                .accountDescription("7일적금입니다")
                .subscriptionPeriod("7")
                .minSubscriptionBalance(Long.valueOf(10000))
                .maxSubscriptionBalance(Long.valueOf(1000000))
                .interestRate("10")
                .rateDescription("10% 이자를 지급합니다. 챌린지 달성시 추가 0.01% 이자를 지급합니다.")
                .increaseInterestRate("0,01")
                .challengeList(challengeDataList)
                .build();

        mockMvc.perform(post("/api/v1/save/create/savingproduct")
                        .content(objectMapper.writeValueAsString(productData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("increaseInterestRate").type(JsonFieldType.STRING).description("추가 이자율"),
                                        // Shinhan API Request fields
                                        fieldWithPath("accountName").type(JsonFieldType.STRING).description("저축 상품 계좌명"),
                                        fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드 (088: 신한은행)"),
                                        fieldWithPath("accountDescription").type(JsonFieldType.STRING).description("저축 상품에 대한 설명"),
                                        fieldWithPath("subscriptionPeriod").type(JsonFieldType.STRING).description("가입 기간"),
                                        fieldWithPath("minSubscriptionBalance").type(JsonFieldType.NUMBER).description("최소 가입 금액"),
                                        fieldWithPath("maxSubscriptionBalance").type(JsonFieldType.NUMBER).description("최대 가입 금액"),
                                        fieldWithPath("interestRate").type(JsonFieldType.STRING).description("기본 이자율"),
                                        fieldWithPath("rateDescription").type(JsonFieldType.STRING).description("이자율에 대한 설명")
                                ).andWithPrefix("challengeList.[].",
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("도전 과제 ID"),
                                        fieldWithPath("name").description("챌린지 이름"),
                                        fieldWithPath("info").description("챌린지 설명"),
                                        fieldWithPath("type").description("챌린지 타입"),
                                        fieldWithPath("verification").description("챌린지 인증 방법")),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("반환되는 데이터 없음")
                                )
                        )
                );
    }

    @Test
    void GetSavingProductsTest() throws Exception {
        List<ChallengeDTO.ChallengeData> challengeDataList = new ArrayList<>();
        challengeDataList.add(ChallengeDTO.ChallengeData.builder()
                .id(Long.valueOf(1))
                .name("챌린지 이름1")
                .info("챌린지 설명1")
                .type(Long.valueOf(1))
                .verification(Long.valueOf(1))
                .build());
        challengeDataList.add(ChallengeDTO.ChallengeData.builder()
                .id(Long.valueOf(2))
                .name("챌린지 이름2")
                .info("챌린지 설명2")
                .type(Long.valueOf(2))
                .verification(Long.valueOf(2))
                .build());

        SavingProductDTO.ProductData productData = SavingProductDTO.ProductData.builder()
                .bankCode("001")
                .accountName("7일적금")
                .accountDescription("7일적금입니다")
                .subscriptionPeriod("7")
                .minSubscriptionBalance(Long.valueOf(10000))
                .maxSubscriptionBalance(Long.valueOf(1000000))
                .interestRate("10")
                .rateDescription("10% 이자를 지급합니다. 챌린지 달성시 추가 0.01% 이자를 지급합니다.")
                .increaseInterestRate("0.01")
                .challengeList(challengeDataList)
                .build();

        given(saveService.getSavingProductList()).willReturn(Arrays.asList(productData));

        mockMvc.perform(post("/api/v1/save/get/savingproducts"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.[].",
                                        fieldWithPath("accountName").type(JsonFieldType.STRING).description("저축 상품 계좌명"),
                                        fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드"),
                                        fieldWithPath("accountDescription").type(JsonFieldType.STRING).description("계좌 설명"),
                                        fieldWithPath("subscriptionPeriod").type(JsonFieldType.STRING).description("가입 기간"),
                                        fieldWithPath("minSubscriptionBalance").type(JsonFieldType.NUMBER).description("최소 가입 금액"),
                                        fieldWithPath("maxSubscriptionBalance").type(JsonFieldType.NUMBER).description("최대 가입 금액"),
                                        fieldWithPath("interestRate").type(JsonFieldType.STRING).description("기본 이자율"),
                                        fieldWithPath("increaseInterestRate").type(JsonFieldType.STRING).description("추가 이자율"),
                                        fieldWithPath("rateDescription").type(JsonFieldType.STRING).description("이자율 설명"),
                                        fieldWithPath("challengeList.[].id").type(JsonFieldType.NUMBER).description("챌린지 ID"),
                                        fieldWithPath("challengeList.[].name").type(JsonFieldType.STRING).description("챌린지 이름"),
                                        fieldWithPath("challengeList.[].info").type(JsonFieldType.STRING).description("챌린지 설명"),
                                        fieldWithPath("challengeList.[].type").type(JsonFieldType.NUMBER).description("챌린지 타입"),
                                        fieldWithPath("challengeList.[].verification").type(JsonFieldType.NUMBER).description("챌린지 인증 방식")
                                )
                        )
                );
    }
}