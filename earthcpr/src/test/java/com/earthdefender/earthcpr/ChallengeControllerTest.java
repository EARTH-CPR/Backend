package com.earthdefender.earthcpr;

import com.earthdefender.earthcpr.DTO.DemandDepositAccountDTO;
import com.earthdefender.earthcpr.DTO.QuizeDTO;
import com.earthdefender.earthcpr.controller.ChallengeController;
import com.earthdefender.earthcpr.restdocs.AbstractRestDocsTests;
import com.earthdefender.earthcpr.service.ChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChallengeController.class)
public class ChallengeControllerTest extends AbstractRestDocsTests {

    @MockBean
    ChallengeService challengeService;

    @Test
    void GetQuizes() throws Exception {
        List<QuizeDTO> quizeList = new ArrayList<>();

        quizeList.add(QuizeDTO.builder()
                .question("다음 중 ESG의 요소가 아닌 것은?")
                .answer("Government")
                .examples(Arrays.asList("Environment", "Government", "Governance", "Social"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("ESG에 대한 설명 중 옳은 것은?")
                .answer("ESG는 E,S,G의 모든 요소가 중요하다.")
                .examples(Arrays.asList("ESG는 사회공헌 사업과도 같은 내용이다.", "ESG는 환경이 가장 강조되는 가치이다.", "ESG는 E,S,G의 모든 요소가 중요하다.", "기업에서는 ESG보다 수익 창출을 고려해야 한다."))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("ESG로 인해 기업이 새롭게 고려하는 가치는?")
                .answer("지속 가능성")
                .examples(Arrays.asList("기업의 수익", "기업의 보유 자산", "시장 점유율", "지속 가능성"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("어르신 1,000명에게 빵 2,000개와 음료 1,000개를 주는 기업의 행사가 있었습니다. 다음 이야기는 ESG중 어떤 요소에 포함될까요?")
                .answer("Social")
                .examples(Arrays.asList("Environment", "Governance", "Social", "ESG 모두 다"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("재생에너지 100%의 약자로 2050년까지 사용 전력 100%를 신재생 에너지로 조달하는 기업 모임은?")
                .answer("RE100")
                .examples(Arrays.asList("RE100", "SE100", "TE100", "ER100"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("기존의 화학 산업 소재 대신 옥수수, 콩, 목재류 등 재생 가능한 식물 자원, 미생물, 효소를 활용해 재생 가능한 화학 제품 또는 바이오 연료를 생산하는 기술은 무엇일까?")
                .answer("화이트 바이오")
                .examples(Arrays.asList("바이오 화학 산업", "재생 바이오 산업", "화이트 바이오", "그린 바이오"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("탄소중립, 혹은 넷제로는 온실가스를 배출한 양만큼 흡수하고 제거하는 활동을 통해 순 배출량 0으로 만드는 것을 의미합니다. 설명중 잘못된 것은?")
                .answer("제품 생산이 가장 중요하며 온실가스 배출은 신경쓰지 않습니다.")
                .examples(Arrays.asList("필요한 전력 공급을 재생에너지로 전환해 온실가스를 저감하고 있습니다.", "제품 생산이 가장 중요하며 온실가스 배출은 신경쓰지 않습니다.", "전력 소모와 열 발생이 적은 반도체로 데이터 센터의 전력 효율을 높입니다.", "기업의 탄소배출량을 기록하여 발전에 기여할 수 있도록 데이터 추적 시스템을 만들었습니다."))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("기업들이 경제적 이득을 위해 '친환경 제품'으로 위장하는 행위는?")
                .answer("그린 워싱")
                .examples(Arrays.asList("화이트 워싱", "그린 워싱", "공정 워싱", "에코 워싱"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("내가 사는 제품이 신념과 가치관을 드러내므로 제작 과정에서 지속가능성을 구현하는 제품을 구매하고, 소셜 미디어로 알리는 것은?")
                .answer("미닝아웃")
                .examples(Arrays.asList("그린아웃", "돈쭐", "미닝아웃", "에코 워리어"))
                .build());

        quizeList.add(QuizeDTO.builder()
                .question("ESG를 반영하여 사람들에게 공감을 얻고 있는 기업은?")
                .answer("모두")
                .examples(Arrays.asList("소녀방앗간", "고요한M", "쉐코(Sheco)", "동구밭"))
                .build());

        given(challengeService.getQuize())
                .willReturn(quizeList);

        mockMvc.perform(get("/api/v1/challenge/get/quize"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data.[].question").type(JsonFieldType.STRING).description("문제"),
                                        fieldWithPath("data.[].answer").type(JsonFieldType.STRING).description("정답"),
                                        fieldWithPath("data.[].examples.[]").type(JsonFieldType.ARRAY).description("보기")
                                )
                        )
                );
    }
}
