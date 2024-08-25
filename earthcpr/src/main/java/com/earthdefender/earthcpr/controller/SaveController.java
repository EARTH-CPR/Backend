package com.earthdefender.earthcpr.controller;

import com.earthdefender.earthcpr.DTO.SavingProductDTO;
import com.earthdefender.earthcpr.response.ApiResponseEntity;
import com.earthdefender.earthcpr.service.SaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/save")
@RequiredArgsConstructor
public class SaveController {
    private final SaveService saveService;

    @PostMapping("/create/savingproduct")
    public ResponseEntity<ApiResponseEntity> createSavingProduct(@Valid @RequestBody SavingProductDTO.ProductData productData) {
        return saveService.createSavingsProduct(productData);
    }

    @PostMapping("/get/savingproducts")
    public ResponseEntity<ApiResponseEntity> getSavingProductList() {
        return ApiResponseEntity.toResponseEntity(saveService.getSavingProductList());
    }
}
