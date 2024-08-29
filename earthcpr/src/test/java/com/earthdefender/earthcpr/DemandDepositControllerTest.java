package com.earthdefender.earthcpr;

import com.earthdefender.earthcpr.DTO.DemandDepositProductDTO;
import com.earthdefender.earthcpr.controller.DemandDepositController;
import com.earthdefender.earthcpr.restdocs.AbstractRestDocsTests;
import com.earthdefender.earthcpr.service.DemandDepositService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

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

}
