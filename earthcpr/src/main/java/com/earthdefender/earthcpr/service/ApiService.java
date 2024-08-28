package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.ShinhanApiDTO;
import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.response.CustomException;
import com.earthdefender.earthcpr.response.ErrorCode;
import com.mysql.cj.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final WebClient webClient;

    @Value("${shinhan.api.transaction-unique-no-length}")
    private int transactionUniqueNoLength;

    @Value("${shinhan.api.key}")
    private String shinhanApiKey;


    public <T, R> Mono<R> postRequest(String uri, T body, Class<R> responseType) {
        if (body instanceof ShinhanApiDTO.RequestHeader) {
            // uri 끝이 api 서비스명
            String[] uris = uri.split("/");
            String serviceName = uris[uris.length - 1];

            ((ShinhanApiDTO.RequestHeader) body).setHeader(ShinhanApiDTO.RequestHeaderParam.builder()
                    .apiName(serviceName)
                    .transmissionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .transmissionTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")))
                    .institutionCode("00100")
                    .fintechAppNo("001")
                    .apiServiceCode(serviceName)
                    .institutionTransactionUniqueNo(generateTransactionUniqueNo())
                    .apiKey(shinhanApiKey)
                    .build());
            // 로깅하기
        }

        return webClient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }
    public <T, R> Mono<R> PostRequestUserKey(String uri, T body, Class<R> responseType, HttpSession session) {
        if (body instanceof ShinhanApiDTO.RequestHeader) {
            // uri 끝이 api 서비스명
            String[] uris = uri.split("/");
            String serviceName = uris[uris.length - 1];
            ((ShinhanApiDTO.RequestHeader) body).setHeader(ShinhanApiDTO.RequestHeaderParam.builder()
                    .apiName(serviceName)
                    .transmissionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .transmissionTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")))
                    .institutionCode("00100")
                    .fintechAppNo("001")
                    .apiServiceCode(serviceName)
                    .institutionTransactionUniqueNo(generateTransactionUniqueNo())
                    .apiKey(shinhanApiKey)
                    .userKey(session.getAttribute("userKey").toString())
                    .build());
            // 로깅하기
        }
        return webClient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    private String generateTransactionUniqueNo() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < transactionUniqueNoLength; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
