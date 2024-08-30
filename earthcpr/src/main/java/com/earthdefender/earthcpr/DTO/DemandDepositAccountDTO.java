package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DemandDepositAccountDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;
        @JsonProperty("accountTypeUniqueNo")
        private String accountTypeUniqueNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountListRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private AccountResponseData rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiGetDepositAccountsResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private List<AccountListResponseData> rec;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountListResponseData {
        @JsonProperty("bankCode")
        private String bankCode;
        @JsonProperty("accountNo")
        private String accountNo;
        @JsonProperty("accountName")
        private String accountName;
        @JsonProperty("accountBalance")
        private String accountBalance;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountListData {
        private String bankCode;
        private String accountNo;
        private String accountName;
        private String accountBalance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountResponseData {
        @JsonProperty("bankCode")
        private String bankCode;

        @JsonProperty("accountNo")
        private String accountNo;

        @JsonProperty("currency")
        private Currency currency;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Currency {
        @JsonProperty("currency")
        private String currency;

        @JsonProperty("currencyName")
        private String currencyName;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiHistoryResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private HistoryResponse rec;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryResponse {
        @JsonProperty("totalCount")
        private String totalCount;

        @JsonProperty("list")
        private List<TransactionRecord> list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionRecord {
        @JsonProperty("transactionUniqueNo")
        private String transactionUniqueNo;

        @JsonProperty("transactionDate")
        private String transactionDate;

        @JsonProperty("transactionTime")
        private String transactionTime;

        @JsonProperty("transactionType")
        private String transactionType;

        @JsonProperty("transactionTypeName")
        private String transactionTypeName;

        @JsonProperty("transactionAccountNo")
        private String transactionAccountNo;

        @JsonProperty("transactionBalance")
        private String transactionBalance;

        @JsonProperty("transactionAfterBalance")
        private String transactionAfterBalance;

        @JsonProperty("transactionSummary")
        private String transactionSummary;

        @JsonProperty("transactionMemo")
        private String transactionMemo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;

        @JsonProperty("accountNo")
        private String accountNo;

        @JsonProperty("startDate")
        private String startDate;

        @JsonProperty("endDate")
        private String endDate;

        @JsonProperty("transactionType")
        private String transactionType;

        @JsonProperty("orderByType")
        private String orderByType;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiTransferResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private List<TransferResponse> rec;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferResponse {
        @JsonProperty("transactionUniqueNo")
        private String transactionUniqueNo;

        @JsonProperty("accountNo")
        private String accountNo;

        @JsonProperty("transactionDate")
        private String transactionDate;

        @JsonProperty("transactionType")
        private String transactionType;

        @JsonProperty("transactionTypeName")
        private String transactionTypeName;

        @JsonProperty("transactionAccountNo")
        private String transactionAccountNo;

        public TransferResponseData toTransferResponseData() {
            return TransferResponseData.builder()
                    .transactionUniqueNo(transactionUniqueNo)
                    .accountNo(accountNo)
                    .transactionDate(transactionDate)
                    .transactionType(transactionType)
                    .transactionTypeName(transactionTypeName)
                    .transactionAccountNo(transactionAccountNo)
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferResponseData {
        private String transactionUniqueNo;
        private String accountNo;
        private String transactionDate;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;
        @JsonProperty("depositAccountNo")
        private String depositAccountNo;
        @JsonProperty("transactionBalance")
        private String transactionBalance;
        @JsonProperty("withdrawalAccountNo")
        private String withdrawalAccountNo;
        @JsonProperty("depositTransactionSummary")
        private String depositTransactionSummary;
        @JsonProperty("withdrawalTransactionSummary")
        private String withdrawalTransactionSummary;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountListRequestData {
        private String loginId;

        public AccountListRequest toAccountListRequest() {
            return AccountListRequest.builder()
                    .loginId(loginId)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountData {
        private String loginId;
        private String accountTypeUniqueNo;

        public CreateAccountRequest toCreateAccountRequest(String accountTypeUniqueNo) {
            return CreateAccountRequest.builder()
                    .loginId(loginId)
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferRequestData {
        private String loginId;
        private String depositAccountNo;
        private String transactionBalance;
        private String withdrawalAccountNo;
        private String depositTransactionSummary;
        private String withdrawalTransactionSummary;

        public TransferRequest toTransferRequest() {
            return TransferRequest.builder()
                    .loginId(loginId)
                    .depositAccountNo(depositAccountNo)
                    .transactionBalance(transactionBalance)
                    .withdrawalAccountNo(withdrawalAccountNo)
                    .depositTransactionSummary(depositTransactionSummary)
                    .withdrawalTransactionSummary(withdrawalTransactionSummary)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryData {
        private String loginId;
        private String accountNo;
        private String startDate;
        private String endDate;
        private String transactionType;
        private String orderByType;
    public HistoryRequest toHistoryRequest() {
        return HistoryRequest.builder()
                .loginId(loginId)
                .accountNo(accountNo)
                .startDate(startDate)
                .endDate(endDate)
                .transactionType(transactionType)
                .orderByType(orderByType)
                .build();
    }
}

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {
        private String loginId;
        private String accountTypeUniqueNo;
        private String depositAccountNo;
        private String transactionBalance;
        private String withdrawalAccountNo;
        private String depositTransactionSummary;
        private String withdrawalTransactionSummary;
        private String transactionUniqueNo;
        private String accountNo;
        private String transactionDate;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
        private String transactionTime;
        private String transactionAfterBalance;
        private String transactionSummary;
        private String transactionMemo;
        private String startDate;
        private String endDate;
        private String orderByType;
        public CreateAccountRequest toCreateAccountRequest(String accountTypeUniqueNo) {
            return CreateAccountRequest.builder()
                    .loginId(loginId)
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .build();
        }
        public AccountListRequest toAccountListRequest() {
            return AccountListRequest.builder()
                    .loginId(loginId)
                    .build();
        }
        public TransferRequest toTransferRequest() {
            return TransferRequest.builder()
                    .loginId(loginId)
                    .depositAccountNo(depositAccountNo)
                    .transactionBalance(transactionBalance)
                    .withdrawalAccountNo(withdrawalAccountNo)
                    .depositTransactionSummary(depositTransactionSummary)
                    .withdrawalTransactionSummary(withdrawalTransactionSummary)
                    .build();
        }
        public HistoryRequest toHistoryRequest() {
            return HistoryRequest.builder()
                    .loginId(loginId)
                    .accountNo(accountNo)
                    .startDate(startDate)
                    .endDate(endDate)
                    .transactionType(transactionType)
                    .orderByType(orderByType)
                    .build();
        }
    }
}