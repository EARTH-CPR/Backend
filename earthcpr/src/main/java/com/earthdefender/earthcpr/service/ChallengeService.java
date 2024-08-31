package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.ChallengeDTO;
import com.earthdefender.earthcpr.DTO.QuizeDTO;
import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.DTO.SavingsAccountDTO;
import com.earthdefender.earthcpr.model.Challenge;
import com.earthdefender.earthcpr.model.ChallengeSuccess;
import com.earthdefender.earthcpr.model.SavingsAccount;
import com.earthdefender.earthcpr.model.User;
import com.earthdefender.earthcpr.repository.ChallengeRepository;
import com.earthdefender.earthcpr.repository.ChallengeSuccessRepository;
import com.earthdefender.earthcpr.repository.SavingsAccountRepository;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChallengeService {
    private final WebClient webClient;
    private final ChallengeRepository challengeRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final ChallengeSuccessRepository challengeSuccessRepository;
    private final UserRepository userRepository;
    private final SaveService saveService;

    @Value("${earthcpr.ai.baseurl}")
    private String earthcprAiBaseurl;

    public ChallengeDTO.ChallengeSuccessResponse challengeVerification(MultipartFile requestFile, String savingsAccountId, String challengeId, int data) {
        String earthcprAiUri = earthcprAiBaseurl;
        Optional<Challenge> challenge = challengeRepository.findById(Long.valueOf(challengeId));
        Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findById(Long.valueOf(savingsAccountId));

        if (!challenge.isPresent() || !savingsAccount.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        if (requestFile.isEmpty()) {
            if (challenge.get().getVerification() == 3) {
                // 대중교통 이용

            } else if (challenge.get().getVerification() == 4) {
                // 미라클 모닝
                if (data >= 4 && data <= 6) {
                    return ChallengeDTO.ChallengeSuccessResponse.builder()
                            .success(true)
                            .message("챌린지 성공!")
                            .build();
                }
            }else if (challenge.get().getVerification() == 5) {
                // ESG 퀴즈
                if (data >= 5) {
                    return ChallengeDTO.ChallengeSuccessResponse.builder()
                            .success(true)
                            .message("챌린지 성공!")
                            .build();
                }
            }
            return ChallengeDTO.ChallengeSuccessResponse.builder()
                    .success(false)
                    .message("챌린지 실패 다시 인증 해주세요.")
                    .build();
        }

        if (challenge.get().getVerification() == 0 || challenge.get().getVerification() == 1 || challenge.get().getVerification() == 2) {
            if (challenge.get().getVerification() == 0) {
                earthcprAiUri = earthcprAiUri + "/food";
            } else if (challenge.get().getVerification() == 1) {
                earthcprAiUri = earthcprAiUri + "/receipt";
            } else if (challenge.get().getVerification() == 2) {
                earthcprAiUri = earthcprAiUri + "/work";
            }

            // earthcprAi API에 요청
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", requestFile.getResource());
            MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

            Mono<String> response = webClient.post()
                    .uri(earthcprAiUri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(multipartBody))
                    .retrieve()
                    .bodyToMono(String.class);

            try {
                // Block the Mono to get the result (you might want to handle this asynchronously)
                String earthcprAiResult = response.block();
                log.info("earthcpr-ai API result: " + earthcprAiResult);

                if (earthcprAiResult.equals("1")) {
                    challengeSuccessRepository.save(ChallengeSuccess.builder()
                            .savingsAccount(savingsAccount.get())
                            .challenge(challenge.get())
                            .challange_success_date(LocalDateTime.now())
                            .build());
                    return ChallengeDTO.ChallengeSuccessResponse.builder()
                            .success(true)
                            .message("챌린지 성공!")
                            .build();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
        }

        return ChallengeDTO.ChallengeSuccessResponse.builder()
                .success(false)
                .message("챌린지 실패 다시 인증 해주세요.")
                .build();
    }

    public List<ChallengeDTO.ChallengeSuccessList> getChallengeSuccess(ChallengeDTO.GetChallengeSuccessRequest challengeSuccessRequest) {
        Optional<User> user = userRepository.findByLoginId(challengeSuccessRequest.getLoginId());
        if (!user.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        List<SavingsAccountDTO.SavingAccountListResponse> savingAccountListResponseList = saveService.getSavingAccountList(SavingsAccountDTO.LoginIdData.builder()
                .loginId(challengeSuccessRequest.getLoginId())
                .build());

        List<ChallengeDTO.ChallengeSuccessList> response = new ArrayList<>();
        for (SavingsAccountDTO.SavingAccountListResponse savingAccount : savingAccountListResponseList) {
            Optional<SavingsAccount> savingsAccountOptional = savingsAccountRepository.findByAccountNo(savingAccount.getAccountNo());
            if (!savingsAccountOptional.isPresent()) {
                throw new CustomException(ErrorCode.NOT_FOUND);
            }
            // Retrieve ChallengeSuccess entities associated with the savings account
            List<ChallengeSuccess> challengeSuccessList = challengeSuccessRepository.findBySavingsAccount(savingsAccountOptional.get());

            // Map each ChallengeSuccess entity to a ChallengeSuccessList DTO
            List<ChallengeDTO.ChallengeSuccessList> challengeSuccessDTOs = challengeSuccessList.stream().map(challengeSuccess -> {
                Challenge challenge = challengeSuccess.getChallenge();
                return ChallengeDTO.ChallengeSuccessList.builder()
                        .id(challengeSuccess.getId())
                        .name(challenge.getName())
                        .info(challenge.getInfo())
                        .type(challenge.getType())
                        .verification(challenge.getVerification())
                        .localDateTimeList(List.of(challengeSuccess.getChallange_success_date()))  // Assuming one date per challenge success
                        .build();
            }).collect(Collectors.toList());

            // Add the mapped DTOs to the response list
            response.addAll(challengeSuccessDTOs);

        }
        return response;
    }

    public List<QuizeDTO> getQuize() {
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

        return quizeList;
    }
}
