package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.ChallengeDTO;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChallengeService {
    private final WebClient webClient;
    private final ChallengeRepository challengeRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final ChallengeSuccessRepository challengeSuccessRepository;

    @Value("${earthcpr.ai.baseurl}")
    private String earthcprAiBaseurl;

    public ChallengeDTO.ChallengeSuccessResponse challengeVerification(MultipartFile requestFile, String savingsAccountId, String challengeId) {
        String earthcprAiUri = earthcprAiBaseurl;
        Optional<Challenge> challenge = challengeRepository.findById(Long.valueOf(challengeId));
        Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findById(Long.valueOf(savingsAccountId));
        boolean isChallengeSuccess = false;

        if (!challenge.isPresent() || !savingsAccount.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        if (challenge.get().getVerification() == 0 || challenge.get().getVerification() == 1) {
            if (challenge.get().getVerification() == 0) {
                earthcprAiUri = earthcprAiUri + "/food";
            } else if (challenge.get().getVerification() == 1) {
                earthcprAiUri = earthcprAiUri + "/receipt";
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
                .success(isChallengeSuccess)
                .message("챌린지 실패 다시 인증 해주세요.")
                .build();
    }
}
