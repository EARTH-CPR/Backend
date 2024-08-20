package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.SavingsProductDTO;
import com.earthdefender.earthcpr.model.SavingsProduct;
import com.earthdefender.earthcpr.repository.SavingsProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class SaveService {
    private final SavingsProductRepository savingsProductRepository;
    private final ApiService apiService;

    public void createSavingsProduct(SavingsProductDTO.CreateSavingsProductRequest request) {


//        savingsProductRepository.save(SavingsProduct.builder()
//                        .accountTypeUniqueNo()
//                        .interestInterestRate(request.getIncreaseInterestRate())
//                        .build());
    }
}
