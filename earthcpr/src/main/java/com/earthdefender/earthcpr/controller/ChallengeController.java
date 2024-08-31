package com.earthdefender.earthcpr.controller;


import com.earthdefender.earthcpr.DTO.ChallengeDTO;
import com.earthdefender.earthcpr.model.Challenge;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping(value = "/verification", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponseEntity> challengeVerification(@RequestPart(value = "file", required = false) MultipartFile file,
                                                                   @RequestPart(value = "savingsAccountId", required = true) String savingsAccountId,
                                                                   @RequestPart(value = "challengeId", required = true) String challengeId,
                                                                   @RequestPart(value = "data", required = true) String data) {

        return ApiResponseEntity.toResponseEntity(challengeService.challengeVerification(file, savingsAccountId, challengeId, Integer.parseInt(data)));
    }

    @PostMapping("/get/success")
    public ResponseEntity<ApiResponseEntity> getChallengeSuccess(@RequestBody ChallengeDTO.GetChallengeSuccessRequest request) {
        return ApiResponseEntity.toResponseEntity(challengeService.getChallengeSuccess(request));
    }

    @GetMapping("/get/quize")
    public ResponseEntity<ApiResponseEntity> getQuize() {
        return ApiResponseEntity.toResponseEntity(challengeService.getQuize());
    }
}
