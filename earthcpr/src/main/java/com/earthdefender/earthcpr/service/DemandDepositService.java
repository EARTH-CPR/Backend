package com.earthdefender.earthcpr.service;

import com.earthdefender.earthcpr.DTO.DemandDepositAccountDTO;
import com.earthdefender.earthcpr.DTO.DemandDepositProductDTO;
import com.earthdefender.earthcpr.DTO.ShinhanApiDTO;
import com.earthdefender.earthcpr.model.DemandDepositAccount;
import com.earthdefender.earthcpr.model.DemandDepositProduct;
import com.earthdefender.earthcpr.repository.DemandDepositAccountRepository;
import com.earthdefender.earthcpr.repository.DemandDepositProductRepository;
import com.earthdefender.earthcpr.repository.UserRepository;
import com.mysql.cj.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            demandDepositProductRepository.save(demandDepositProduct);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to create demand deposit product");
        }
    }

    //계좌 목록 조회
    public List<DemandDepositAccountDTO.AccountListData> getDemandDepositAccounts(DemandDepositAccountDTO.AccountListRequestData accountListRequestData) {
        List<DemandDepositAccountDTO.AccountListData> response = new ArrayList<>();
        Mono<DemandDepositAccountDTO.ShinhanApiGetDepositAccountsResponse> shinhanApiResponseMono = apiService.PostRequestUserKey(
                "/edu/demandDeposit/inquireDemandDepositAccountList",
                accountListRequestData.toAccountListRequest(),
                DemandDepositAccountDTO.ShinhanApiGetDepositAccountsResponse.class
                , accountListRequestData.getLoginId()
        );
        try{
            DemandDepositAccountDTO.ShinhanApiGetDepositAccountsResponse shinhanApiGetDepositAccountsResponse = shinhanApiResponseMono.block();
            for (DemandDepositAccountDTO.AccountListResponseData accountListResponseDataData : shinhanApiGetDepositAccountsResponse.getRec()) {
                response.add(DemandDepositAccountDTO.AccountListData.builder()
                        .bankCode(accountListResponseDataData.getBankCode())
                        .accountNo(accountListResponseDataData.getAccountNo())
                        .accountName(accountListResponseDataData.getAccountName())
                        .accountBalance(accountListResponseDataData.getAccountBalance())
                        .build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to get demand deposit products");
        }

        return response;
    }

    //입출금 계좌 생성
    @Transactional
    public void createDemandDepositAccount(DemandDepositAccountDTO.CreateAccountData createAccountData) {

        Mono<DemandDepositAccountDTO.CreateAccountResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/demandDeposit/createDemandDepositAccount",
                createAccountData.toCreateAccountRequest(accountTypeUniqueNo),
                DemandDepositAccountDTO.CreateAccountResponse.class
                , createAccountData.getLoginId()
        );

        try {
            DemandDepositAccountDTO.CreateAccountResponse response = responseMono.block();
            DemandDepositProduct depositProduct = demandDepositProductRepository.findByAccountTypeUniqueNo(accountTypeUniqueNo)
                    .orElseThrow(() -> new RuntimeException("Deposit product not found"));

            DemandDepositAccount demandDepositAccount = DemandDepositAccount.builder()
                    .user(userRepository.findByLoginId(createAccountData.getLoginId()).get())
                    .depositProduct(depositProduct)
                    .account_no(response.getRec().getAccountNo())
                    .build();

            demandDepositAccountRepository.save(demandDepositAccount);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to create demand deposit account");
        }
    }

    //입출금 계좌 이체
    public DemandDepositAccountDTO.TransferResponseData transferDepositAccount(DemandDepositAccountDTO.TransferRequestData transferRequestData) {
        Mono<DemandDepositAccountDTO.ShinhanApiTransferResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/demandDeposit/updateDemandDepositAccountTransfer",
                transferRequestData.toTransferRequest(),
                DemandDepositAccountDTO.ShinhanApiTransferResponse.class
                , transferRequestData.getLoginId()
        );
        try {
            DemandDepositAccountDTO.ShinhanApiTransferResponse response = responseMono.block();
            return response.getRec().get(1).toTransferResponseData();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to transfer deposit account");
        }
    }

    //입출금 거래 내역
    public List<DemandDepositAccountDTO.TransactionRecord> getDepositHistory(DemandDepositAccountDTO.HistoryData historyData) {
        Mono<DemandDepositAccountDTO.ShinhanApiHistoryResponse> responseMono = apiService.PostRequestUserKey(
                "/edu/demandDeposit/inquireTransactionHistoryList",
                historyData.toHistoryRequest(),
                DemandDepositAccountDTO.ShinhanApiHistoryResponse.class,
                historyData.getLoginId()
        );
        try {
            DemandDepositAccountDTO.ShinhanApiHistoryResponse shinhanApiHistoryResponse = responseMono.block();
            List<DemandDepositAccountDTO.TransactionRecord> response = new ArrayList<>();
            for(DemandDepositAccountDTO.TransactionRecord transactionRecord : shinhanApiHistoryResponse.getRec().getList()) {
                response.add(transactionRecord);
            }
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to transfer deposit account");
        }
    }
}