package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.DTO.SavingsAccountDTO;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.service.SaveService;
import com.mysql.cj.Session;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/save")
@RequiredArgsConstructor
public class SaveController {
    private final SaveService saveService;

    @PostMapping("/create/savingproduct")
    public ResponseEntity<ApiResponseEntity> createSavingProduct(@Valid @RequestBody SavingProductDTO.ProductData productData) {
        saveService.createSavingsProduct(productData);
        return ApiResponseEntity.toResponseEntity();
    }

    @PostMapping("/get/savingproducts")
    public ResponseEntity<ApiResponseEntity> getSavingProductList() {
        List<SavingProductDTO.ProductData> savingProductsResponseList =  saveService.getSavingProductList();
        return ApiResponseEntity.toResponseEntity(savingProductsResponseList);
    }

    @PostMapping("/create/savingaccount")
    public ResponseEntity<ApiResponseEntity> createSavingsAccount(@Valid @RequestBody SavingsAccountDTO.ProductData productData) {
        saveService.createSavingsAccount(productData);
        return ApiResponseEntity.toResponseEntity();
    }
    @PostMapping("/inquire/savingaccount")
    public ResponseEntity<ApiResponseEntity> getSavingAccountList(@Valid @RequestBody SavingsAccountDTO.ProductData productData) {
        SavingsAccountDTO.ProductData productDetail  =  saveService.getSavingProductDetail(productData);
        return ApiResponseEntity.toResponseEntity(productDetail);
    }
    @PostMapping("/inquire/payment")
    public ResponseEntity<ApiResponseEntity> getPaymentList(@Valid @RequestBody SavingsAccountDTO.ProductData productData) {
        List<SavingsAccountDTO.PaymentInfo> paymentList =  saveService.getPaymentList(productData);
        return ApiResponseEntity.toResponseEntity(paymentList);
    }
    //만기이자 조회
    @PostMapping("/inquire/expiryinterest")
    public ResponseEntity<ApiResponseEntity> getExpiryInterest(@Valid @RequestBody SavingsAccountDTO.ProductData productData) {
        SavingsAccountDTO.ProductData expiryInterest =  saveService.inquireExpiryInterest(productData);
        return ApiResponseEntity.toResponseEntity(expiryInterest);
    }
    //중도해지 이자 조회
    @PostMapping("/inquire/earlyinterest")
    public ResponseEntity<ApiResponseEntity> getEarlyInterest(@Valid @RequestBody SavingsAccountDTO.ProductData productData) {
        SavingsAccountDTO.ProductData earlyInterest=  saveService.inquireEarlyInterest(productData);
        return ApiResponseEntity.toResponseEntity(earlyInterest);
    }
    //중도해지
    @PostMapping("/delete/account")
    public ResponseEntity<ApiResponseEntity> deleteAccount(@Valid @RequestBody SavingsAccountDTO.ProductData productData) {
        SavingsAccountDTO.ProductData deleteAccount  =saveService.deleteAccount(productData);
        return ApiResponseEntity.toResponseEntity(deleteAccount);
    }
}
