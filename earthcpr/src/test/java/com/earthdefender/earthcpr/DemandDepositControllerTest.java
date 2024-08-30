package com.earthdefender.earthcpr;

import com.earthdefender.earthcpr.DTO.DemandDepositAccountDTO;
import com.earthdefender.earthcpr.DTO.DemandDepositProductDTO;
import com.earthdefender.earthcpr.controller.DemandDepositController;
import com.earthdefender.earthcpr.restdocs.AbstractRestDocsTests;
import com.earthdefender.earthcpr.service.DemandDepositService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemandDepositController.class)
public class DemandDepositControllerTest extends AbstractRestDocsTests  {

    @MockBean
    DemandDepositService demandDepositService;

    @Test
    void CreateDemandDepositTest() throws Exception {
        DemandDepositProductDTO.ProductData productData = DemandDepositProductDTO.ProductData.builder()
                .bankCode("088")
                .accountName("이름")
                .accountDescription("설명")
                .build();


        mockMvc.perform(post("/api/v1/deposit/create/depositproduct")
                        .content(objectMapper.writeValueAsString(productData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("accountName").type(JsonFieldType.STRING).description("입출금계좌 상품 계좌명"),
                                        fieldWithPath("accountDescription").type(JsonFieldType.STRING).description("입출금 계좌 상품에 대한 설명"),
                                        fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드 (088: 신한은행)")

                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("반환되는 데이터 없음")
                                )
                        )
                );
    }
    @Test
    void GetDemandDepositAccountsTest() throws Exception {
        DemandDepositAccountDTO.AccountListRequestData accountListRequestData = DemandDepositAccountDTO.AccountListRequestData.builder()
                .loginId("testUser")
                .build();

        DemandDepositAccountDTO.AccountListData accountListData1 = DemandDepositAccountDTO.AccountListData.builder()
                .bankCode("088")
                .accountNo("1234567890")
                .accountName("Account 1")
                .accountBalance("10000")
                .build();

        DemandDepositAccountDTO.AccountListData accountListData2 = DemandDepositAccountDTO.AccountListData.builder()
                .bankCode("088")
                .accountNo("0987654321")
                .accountName("Account 2")
                .accountBalance("20000")
                .build();

        given(demandDepositService.getDemandDepositAccounts(any(DemandDepositAccountDTO.AccountListRequestData.class)))
                .willReturn(Arrays.asList(accountListData1, accountListData2));

        mockMvc.perform(post("/api/v1/deposit/get/depositaccounts")
                        .content(objectMapper.writeValueAsString(accountListRequestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("계좌 목록")
                                ).andWithPrefix("data.[].",
                                        fieldWithPath("bankCode").type(JsonFieldType.STRING).description("은행 코드"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("accountName").type(JsonFieldType.STRING).description("계좌 이름"),
                                        fieldWithPath("accountBalance").type(JsonFieldType.STRING).description("계좌 잔액")
                                )
                        )
                );
    }
    @Test
    void CreateDemandDepositAccountTest() throws Exception {
        DemandDepositAccountDTO.CreateAccountData createAccountData = DemandDepositAccountDTO.CreateAccountData.builder()
                .loginId("testUser")
                .accountTypeUniqueNo("123456")
                .build();

        mockMvc.perform(post("/api/v1/deposit/create/depositaccount")
                        .content(objectMapper.writeValueAsString(createAccountData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountTypeUniqueNo").type(JsonFieldType.STRING).description("계좌 유형 고유 번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("반환되는 데���터 없음")
                                )
                        )
                );
    }
    @Test
    void GetDepositHistoryTest() throws Exception {
        DemandDepositAccountDTO.HistoryData historyData = DemandDepositAccountDTO.HistoryData.builder()
                .loginId("testUser")
                .accountNo("1234567890")
                .startDate("20230101")
                .endDate("20231231")
                .transactionType("ALL")
                .orderByType("DESC")
                .build();

        DemandDepositAccountDTO.TransactionRecord transactionRecord1 = DemandDepositAccountDTO.TransactionRecord.builder()
                .transactionUniqueNo("1")
                .transactionDate("20230101")
                .transactionTime("120000")
                .transactionType("DEPOSIT")
                .transactionTypeName("Deposit")
                .transactionAccountNo("1234567890")
                .transactionBalance("10000")
                .transactionAfterBalance("20000")
                .transactionSummary("Deposit Summary")
                .transactionMemo("Deposit Memo")
                .build();

        DemandDepositAccountDTO.TransactionRecord transactionRecord2 = DemandDepositAccountDTO.TransactionRecord.builder()
                .transactionUniqueNo("2")
                .transactionDate("20230102")
                .transactionTime("130000")
                .transactionType("WITHDRAWAL")
                .transactionTypeName("Withdrawal")
                .transactionAccountNo("1234567890")
                .transactionBalance("5000")
                .transactionAfterBalance("15000")
                .transactionSummary("Withdrawal Summary")
                .transactionMemo("Withdrawal Memo")
                .build();

        given(demandDepositService.getDepositHistory(any(DemandDepositAccountDTO.HistoryData.class)))
                .willReturn(Arrays.asList(transactionRecord1, transactionRecord2));

        mockMvc.perform(post("/api/v1/deposit/get/deposithistory")
                        .content(objectMapper.writeValueAsString(historyData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("accountNo").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜"),
                                        fieldWithPath("transactionType").type(JsonFieldType.STRING).description("거래 유형"),
                                        fieldWithPath("orderByType").type(JsonFieldType.STRING).description("정렬 순서")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                                ).andWithPrefix("data.[].",
                                        fieldWithPath("transactionUniqueNo").type(JsonFieldType.STRING).description("거래 고유 번호"),
                                        fieldWithPath("transactionDate").type(JsonFieldType.STRING).description("거래 날짜"),
                                        fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("거래 시간"),
                                        fieldWithPath("transactionType").type(JsonFieldType.STRING).description("거래 유형"),
                                        fieldWithPath("transactionTypeName").type(JsonFieldType.STRING).description("거래 유형 이름"),
                                        fieldWithPath("transactionAccountNo").type(JsonFieldType.STRING).description("거래 계좌 번호"),
                                        fieldWithPath("transactionBalance").type(JsonFieldType.STRING).description("거래 금액"),
                                        fieldWithPath("transactionAfterBalance").type(JsonFieldType.STRING).description("거래 후 잔액"),
                                        fieldWithPath("transactionSummary").type(JsonFieldType.STRING).description("거래 요약"),
                                        fieldWithPath("transactionMemo").type(JsonFieldType.STRING).description("거래 메모")
                                )
                        )
                );
    }




}
