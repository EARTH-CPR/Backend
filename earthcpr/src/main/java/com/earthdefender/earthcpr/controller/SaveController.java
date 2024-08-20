package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.SavingsProductDTO;
import com.earthdefender.earthcpr.service.ApiService;
import com.earthdefender.earthcpr.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/save")
@RequiredArgsConstructor
public class SaveController {
    private final SaveService saveService;
    private final ApiService apiService;

    @PostMapping("/create/savingproduct")
    public void createSavingProduct(@RequestBody SavingsProductDTO.ShinhanApiRequest request) {
        Mono<SavingsProductDTO.ShinhanApiResponse> response = apiService.postRequest("/savings/createProduct", request, SavingsProductDTO.ShinhanApiResponse.class);

        response.doOnError(error -> {
                    System.out.println("response Error: " + error.getMessage());
                })
                .subscribe(
                shinhanApiResponse -> {
                    System.out.println("receive: " + shinhanApiResponse);
                }
        );
    }

}
