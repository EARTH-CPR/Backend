package com.earthdefender.earthcpr;


import com.earthdefender.earthcpr.DTO.ChallengeDTO;
import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.DTO.SavingsAccountDTO;
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
import static org.mockito.Mockito.doNothing;
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

    @Test
    void CreateSavingsAccountTest() throws Exception {
        SavingsAccountDTO.CreateAccountData createAccountData = SavingsAccountDTO.CreateAccountData.builder()
                .loginId("testUser")
                .accountTypeUniqueNo("123456")
                .depositBalance("10000")
                .withdrawalAccountNo("0015256174546107")
                .build();


        mockMvc.perform(post("/api/v1/save/create/savingaccount")
                        .content(objectMapper.writeValueAsString(createAccountData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountTypeUniqueNo").type(JsonFieldType.STRING).description("계좌 유형 고유 번호"),
                                        fieldWithPath("depositBalance").type(JsonFieldType.STRING).description("예치 금액"),
                                        fieldWithPath("withdrawalAccountNo").type(JsonFieldType.STRING).description("출금 계좌 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("반환되는 데이터 없음")
                                )
                        )
                );
    }

    @Test
    void GetSavingAccountsTest() throws Exception {
        SavingsAccountDTO.SavingAccountListResponse accountListResponse1 = SavingsAccountDTO.SavingAccountListResponse.builder()
                .bankCode("002")
                .bankName("산업은행")
                .userName("aaa")
                .accountNo("0023983668")
                .accountName("7일적금")
                .accountDescription("7일적금입니다")
                .withdrawalBankCode("001")
                .withdrawalBankName("한국은행")
                .withdrawalAccountNo("0015256174546107")
                .subscriptionPeriod("7")
                .depositBalance("10000")
                .interestRate("10")
                .installmentNumber("1")
                .totalBalance("10000")
                .accountCreateDate("20240830")
                .accountExpiryDate("20240906")
                .build();

        SavingsAccountDTO.SavingAccountListResponse accountListResponse2 = SavingsAccountDTO.SavingAccountListResponse.builder()
                .bankCode("002")
                .bankName("산업은행")
                .userName("aaa")
                .accountNo("0029582247")
                .accountName("7일적금")
                .accountDescription("7일적금입니다")
                .withdrawalBankCode("001")
                .withdrawalBankName("한국은행")
                .withdrawalAccountNo("0015256174546107")
                .subscriptionPeriod("7")
                .depositBalance("10000")
                .interestRate("10")
                .installmentNumber("3")
                .totalBalance("30000")
                .accountCreateDate("20240828")
                .accountExpiryDate("20240904")
                .build();

        SavingsAccountDTO.LoginIdData loginIdData = SavingsAccountDTO.LoginIdData.builder()
                .loginId("testUser")
                .build();

        given(saveService.getSavingAccountList(any(SavingsAccountDTO.LoginIdData.class)))
                .willReturn(Arrays.asList(accountListResponse1, accountListResponse2));


        mockMvc.perform(post("/api/v1/save/get/savingaccount")
                        .content(objectMapper.writeValueAsString(loginIdData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.[].",
                                        fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드"),
                                        fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름"),
                                        fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("accountName").type(JsonFieldType.STRING).description("계좌 이름"),
                                        fieldWithPath("accountDescription").type(JsonFieldType.STRING).description("계좌 설명"),
                                        fieldWithPath("withdrawalBankCode").type(JsonFieldType.STRING).description("출금 은행 코드"),
                                        fieldWithPath("withdrawalBankName").type(JsonFieldType.STRING).description("출금 은행 이름"),
                                        fieldWithPath("withdrawalAccountNo").type(JsonFieldType.STRING).description("출금 계좌 번호"),
                                        fieldWithPath("subscriptionPeriod").type(JsonFieldType.STRING).description("가입 기간"),
                                        fieldWithPath("depositBalance").type(JsonFieldType.STRING).description("예치 금액"),
                                        fieldWithPath("interestRate").type(JsonFieldType.STRING).description("이자율"),
                                        fieldWithPath("installmentNumber").type(JsonFieldType.STRING).description("납입 횟수"),
                                        fieldWithPath("totalBalance").type(JsonFieldType.STRING).description("총 잔액"),
                                        fieldWithPath("accountCreateDate").type(JsonFieldType.STRING).description("계좌 생성일"),
                                        fieldWithPath("accountExpiryDate").type(JsonFieldType.STRING).description("계좌 만기일")
                                )
                        )
                );
    }
    @Test
    void GetSavingProductDetailTest() throws Exception {
        SavingsAccountDTO.InquireAccountData inquireAccountData = SavingsAccountDTO.InquireAccountData.builder()
                .loginId("testUser")
                .accountNo("1234567890")
                .build();

        SavingsAccountDTO.InquireAccountResponseData productDetail = SavingsAccountDTO.InquireAccountResponseData.builder()
                .bankCode("001")
                .bankName("Shinhan Bank")
                .userName("testUser")
                .accountNo("1234567890")
                .accountName("7일적금")
                .accountDescription("7일적금입니다")
                .withdrawalBankCode("002")
                .withdrawalBankName("한국은행")
                .withdrawalAccountNo("0015256174546107")
                .subscriptionPeriod("7")
                .depositBalance("10000")
                .interestRate("10")
                .installmentNumber("1")
                .totalBalance("10000")
                .accountCreateDate("20240830")
                .accountExpiryDate("20240906")
                .build();

        given(saveService.getSavingProductDetail(any(SavingsAccountDTO.InquireAccountData.class)))
                .willReturn(productDetail);

        mockMvc.perform(post("/api/v1/save/inquire/savingaccount")
                        .content(objectMapper.writeValueAsString(inquireAccountData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.",
                                        fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드"),
                                        fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름"),
                                        fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("accountName").type(JsonFieldType.STRING).description("계좌 이름"),
                                        fieldWithPath("accountDescription").type(JsonFieldType.STRING).description("계좌 설명"),
                                        fieldWithPath("withdrawalBankCode").type(JsonFieldType.STRING).description("출금 은행 코드"),
                                        fieldWithPath("withdrawalBankName").type(JsonFieldType.STRING).description("출금 은행 이름"),
                                        fieldWithPath("withdrawalAccountNo").type(JsonFieldType.STRING).description("출금 계좌 번호"),
                                        fieldWithPath("subscriptionPeriod").type(JsonFieldType.STRING).description("가입 기간"),
                                        fieldWithPath("depositBalance").type(JsonFieldType.STRING).description("예치 금액"),
                                        fieldWithPath("interestRate").type(JsonFieldType.STRING).description("이자율"),
                                        fieldWithPath("installmentNumber").type(JsonFieldType.STRING).description("납입 횟수"),
                                        fieldWithPath("totalBalance").type(JsonFieldType.STRING).description("총 잔액"),
                                        fieldWithPath("accountCreateDate").type(JsonFieldType.STRING).description("계좌 생성일"),
                                        fieldWithPath("accountExpiryDate").type(JsonFieldType.STRING).description("계좌 만기일")
                                )
                        )
                );
    }
    @Test
    void GetPaymentListTest() throws Exception {
        SavingsAccountDTO.InquirePaymentData inquirePaymentData = SavingsAccountDTO.InquirePaymentData.builder()
                .loginId("testUser")
                .accountNo("1234567890")
                .build();

        SavingsAccountDTO.PaymentInfo paymentInfo1 = SavingsAccountDTO.PaymentInfo.builder()
                .depositInstallment("1")
                .paymentBalance("10000")
                .paymentDate("20240830")
                .paymentTime("120000")
                .status("SUCCESS")
                .failureReason(null)
                .build();

        SavingsAccountDTO.PaymentInfo paymentInfo2 = SavingsAccountDTO.PaymentInfo.builder()
                .depositInstallment("2")
                .paymentBalance("10000")
                .paymentDate("20240906")
                .paymentTime("120000")
                .status("SUCCESS")
                .failureReason(null)
                .build();

        given(saveService.getPaymentList(any(SavingsAccountDTO.InquirePaymentData.class)))
                .willReturn(Arrays.asList(paymentInfo1, paymentInfo2));

        mockMvc.perform(post("/api/v1/save/inquire/payment")
                        .content(objectMapper.writeValueAsString(inquirePaymentData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.[].",
                                        fieldWithPath("depositInstallment").type(JsonFieldType.STRING).description("납입 회차"),
                                        fieldWithPath("paymentBalance").type(JsonFieldType.STRING).description("납입 금액"),
                                        fieldWithPath("paymentDate").type(JsonFieldType.STRING).description("납입 날짜"),
                                        fieldWithPath("paymentTime").type(JsonFieldType.STRING).description("납입 시간"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("failureReason").type(JsonFieldType.STRING).description("실패 이유").optional()
                                )
                        )
                );
    }
    @Test
    void InquireExpiryInterestTest() throws Exception {
        SavingsAccountDTO.InquireExpiryData inquireExpiryData = SavingsAccountDTO.InquireExpiryData.builder()
                .loginId("testUser")
                .accountNo("1234567890")
                .build();

        SavingsAccountDTO.InquireExpiryResponseData expiryResponseData = SavingsAccountDTO.InquireExpiryResponseData.builder()
                .accountNo("1234567890")
                .interestRate("10")
                .accountCreateDate("20240830")
                .accountExpiryDate("20240906")
                .expiryBalance("10000")
                .expiryInterest("100")
                .expiryTotalBalance("10100")
                .totalBalance("10100")
                .build();

        given(saveService.inquireExpiryInterest(any(SavingsAccountDTO.InquireExpiryData.class)))
                .willReturn(expiryResponseData);

        mockMvc.perform(post("/api/v1/save/inquire/expiryinterest")
                        .content(objectMapper.writeValueAsString(inquireExpiryData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.",
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("interestRate").type(JsonFieldType.STRING).description("이자율"),
                                        fieldWithPath("accountCreateDate").type(JsonFieldType.STRING).description("계좌 생성일"),
                                        fieldWithPath("accountExpiryDate").type(JsonFieldType.STRING).description("계좌 만기일"),
                                        fieldWithPath("expiryBalance").type(JsonFieldType.STRING).description("만기 잔액"),
                                        fieldWithPath("expiryInterest").type(JsonFieldType.STRING).description("만기 이자"),
                                        fieldWithPath("expiryTotalBalance").type(JsonFieldType.STRING).description("만기 총 잔액"),
                                        fieldWithPath("totalBalance").type(JsonFieldType.STRING).description("총 잔액")
                                )
                        )
                );
    }
    @Test
    void InquireEarlyInterestTest() throws Exception {
        SavingsAccountDTO.InquireEarlyData inquireEarlyData = SavingsAccountDTO.InquireEarlyData.builder()
                .loginId("testUser")
                .accountNo("1234567890")
                .build();

        SavingsAccountDTO.InquireEarlyResponseData earlyResponseData = SavingsAccountDTO.InquireEarlyResponseData.builder()
                .accountNo("1234567890")
                .interestRate("10")
                .accountCreateDate("20240830")
                .earlyTerminationDate("20240906")
                .totalBalance("10000")
                .earlyTerminationInterest("50")
                .earlyTerminationBalance("10050")
                .build();

        given(saveService.inquireEarlyInterest(any(SavingsAccountDTO.InquireEarlyData.class)))
                .willReturn(earlyResponseData);

        mockMvc.perform(post("/api/v1/save/inquire/earlyinterest")
                        .content(objectMapper.writeValueAsString(inquireEarlyData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.",
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("interestRate").type(JsonFieldType.STRING).description("이자율"),
                                        fieldWithPath("accountCreateDate").type(JsonFieldType.STRING).description("계좌 생성일"),
                                        fieldWithPath("earlyTerminationDate").type(JsonFieldType.STRING).description("중도 해지일"),
                                        fieldWithPath("totalBalance").type(JsonFieldType.STRING).description("총 잔액"),
                                        fieldWithPath("earlyTerminationInterest").type(JsonFieldType.STRING).description("중도 해지 이자"),
                                        fieldWithPath("earlyTerminationBalance").type(JsonFieldType.STRING).description("중도 해지 잔액")
                                )
                        )
                );
    }
    @Test
    void DeleteAccountTest() throws Exception {
        SavingsAccountDTO.InquireEarlyData inquireEarlyData = SavingsAccountDTO.InquireEarlyData.builder()
                .loginId("testUser")
                .accountNo("1234567890")
                .build();

        SavingsAccountDTO.DeleteAccountResponseData deleteAccountResponseData = SavingsAccountDTO.DeleteAccountResponseData.builder()
                .status("SUCCESS")
                .accountNo("1234567890")
                .earlyTerminationDate("20240906")
                .totalBalance("10000")
                .earlyTerminationInterest("50")
                .earlyTerminationBalance("10050")
                .build();

        given(saveService.deleteAccount(any(SavingsAccountDTO.InquireEarlyData.class)))
                .willReturn(deleteAccountResponseData);

        mockMvc.perform(post("/api/v1/save/delete/account")
                        .content(objectMapper.writeValueAsString(inquireEarlyData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.",
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("earlyTerminationDate").type(JsonFieldType.STRING).description("중도 해지일"),
                                        fieldWithPath("totalBalance").type(JsonFieldType.STRING).description("총 잔액"),
                                        fieldWithPath("earlyTerminationInterest").type(JsonFieldType.STRING).description("중도 해지 이자"),
                                        fieldWithPath("earlyTerminationBalance").type(JsonFieldType.STRING).description("중도 해지 잔액")
                                )
                        )
                );
    }
}