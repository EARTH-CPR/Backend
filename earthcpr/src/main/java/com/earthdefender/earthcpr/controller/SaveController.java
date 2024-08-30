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
    //적금계좌목록조회
    @PostMapping("/get/savingaccount")
    public ResponseEntity<ApiResponseEntity> getSavingAccountList(@Valid @RequestBody SavingsAccountDTO.LoginIdData loginIdData) {
        List<SavingsAccountDTO.SavingAccountListResponse> savingAccount =  saveService.getSavingAccountList(loginIdData);
        return ApiResponseEntity.toResponseEntity(savingAccount);
    }

    @PostMapping("/create/savingaccount")
    public ResponseEntity<ApiResponseEntity> createSavingsAccount(@Valid @RequestBody SavingsAccountDTO.CreateAccountData createAccountData) {
        saveService.createSavingsAccount(createAccountData);
        return ApiResponseEntity.toResponseEntity();
    }

    //적금 계좌조회
    @PostMapping("/inquire/savingaccount")
    public ResponseEntity<ApiResponseEntity> getSavingProductDetail(@Valid @RequestBody SavingsAccountDTO.InquireAccountData inquireAccountData) {
        SavingsAccountDTO.InquireAccountResponseData productDetail  =  saveService.getSavingProductDetail(inquireAccountData);
        return ApiResponseEntity.toResponseEntity(productDetail);
    }

    //적금 납입회차조회
    @PostMapping("/inquire/payment")
    public ResponseEntity<ApiResponseEntity> getPaymentList(@Valid @RequestBody SavingsAccountDTO.InquirePaymentData inquirePaymentData) {
        List<SavingsAccountDTO.PaymentInfo> paymentList =  saveService.getPaymentList(inquirePaymentData);
        return ApiResponseEntity.toResponseEntity(paymentList);
    }
    //만기이자 조회
    @PostMapping("/inquire/expiryinterest")
    public ResponseEntity<ApiResponseEntity> inquireExpiryInterest(@Valid @RequestBody SavingsAccountDTO.InquireExpiryData inquireExpiryData) {
        SavingsAccountDTO.InquireExpiryResponseData expiryInterest =  saveService.inquireExpiryInterest(inquireExpiryData);
        return ApiResponseEntity.toResponseEntity(expiryInterest);
    }
    //중도해지 이자 조회
    @PostMapping("/inquire/earlyinterest")
    public ResponseEntity<ApiResponseEntity> inquireEarlyInterest(@Valid @RequestBody SavingsAccountDTO.InquireEarlyData inquireEarlyData) {
        SavingsAccountDTO.InquireEarlyResponseData earlyInterest=  saveService.inquireEarlyInterest(inquireEarlyData);
        return ApiResponseEntity.toResponseEntity(earlyInterest);
    }
    //중도해지
    @PostMapping("/delete/account")
    public ResponseEntity<ApiResponseEntity> deleteAccount(@Valid @RequestBody SavingsAccountDTO.InquireEarlyData inquireEarlyData) {
        SavingsAccountDTO.DeleteAccountResponseData deleteAccount  =saveService.deleteAccount(inquireEarlyData);
        return ApiResponseEntity.toResponseEntity(deleteAccount);
    }
}
