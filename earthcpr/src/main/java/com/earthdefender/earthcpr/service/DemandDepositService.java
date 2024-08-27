package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.DemandDepositAccountDTO;
import com.earthdefender.earthcpr.DTO.DemandDepositProductDTO;
import com.earthdefender.earthcpr.model.DemandDepositAccount;
import com.earthdefender.earthcpr.model.DemandDepositProduct;
import com.earthdefender.earthcpr.repository.DemandDepositAccountRepository;
import com.earthdefender.earthcpr.repository.DemandDepositProductRepository;
import com.earthdefender.earthcpr.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemandDepositService {

    private final ApiService apiService;
    private final DemandDepositProductRepository demandDepositProductRepository;
    private final DemandDepositAccountRepository demandDepositAccountRepository;
    private final UserRepository userRepository;

    @Value("${shinhan.api.accountTypeUniqueNo}")
    private String accountTypeUniqueNo;

    @Transactional
    public void createDemandDeposit(DemandDepositProductDTO.ProductData productData) {
        System.out.println(productData);
        Mono<DemandDepositProductDTO.CreateResponse> responseMono = apiService.postRequest(
                "/edu/demandDeposit/createDemandDeposit", productData.toCreateRequest(), DemandDepositProductDTO.CreateResponse.class
        );
        try {
            DemandDepositProductDTO.CreateResponse response = responseMono.block();
            DemandDepositProduct demandDepositProduct = DemandDepositProduct.builder()
                    .bankCode(response.getRec().getBankCode())
                    .accountName(response.getRec().getAccountName())
                    .accountDescription(response.getRec().getAccountDescription())
                    .accountTypeUniqueNo(response.getRec().getAccountTypeUniqueNo())
                    .build();
            System.out.println(demandDepositProduct);
            demandDepositProductRepository.save(demandDepositProduct);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to create demand deposit product");
        }
    }

    @Transactional
    public void createDemandDepositAccount(DemandDepositAccountDTO.ProductData productData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not found in session");
        }

        Mono<DemandDepositAccountDTO.CreateAccountResponse> responseMono = apiService.PostRequestUserKey(
                "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/createDemandDepositAccount",
                productData.toCreateAccountRequest(accountTypeUniqueNo),
                DemandDepositAccountDTO.CreateAccountResponse.class,
                session
        );

        try {
            DemandDepositAccountDTO.CreateAccountResponse response = responseMono.block();
            DemandDepositProduct depositProduct = demandDepositProductRepository.findByAccountTypeUniqueNo(accountTypeUniqueNo)
                    .orElseThrow(() -> new RuntimeException("Deposit product not found"));

            DemandDepositAccount demandDepositAccount = DemandDepositAccount.builder()
                    .user(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")))
                    .depositProduct(depositProduct)
                    .account_no(response.getRec().getAccountNo())
                    .build();

            demandDepositAccountRepository.save(demandDepositAccount);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to create demand deposit account");
        }
    }
}