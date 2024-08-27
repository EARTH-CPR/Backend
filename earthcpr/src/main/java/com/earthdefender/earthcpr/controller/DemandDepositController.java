package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.DemandDepositAccountDTO;
import com.earthdefender.earthcpr.DTO.DemandDepositProductDTO;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.service.DemandDepositService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deposit")
@RequiredArgsConstructor
public class DemandDepositController {

    private final DemandDepositService demandDepositService;

    @PostMapping("/create/depositproduct")
    public ResponseEntity<ApiResponseEntity> createDemandDeposit(@Valid @RequestBody DemandDepositProductDTO.ProductData productData) {
        demandDepositService.createDemandDeposit(productData);
        return ApiResponseEntity.toResponseEntity();
    }
    @PostMapping("/create/depositaccount")
    public ResponseEntity<ApiResponseEntity> createDemandDepositAccount(@Valid @RequestBody DemandDepositAccountDTO.ProductData productData, HttpSession session) {
        demandDepositService.createDemandDepositAccount(productData, session);
        return ApiResponseEntity.toResponseEntity();
    }
}