package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class SavingsAccountDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("accountTypeUniqueNo")
        private String accountTypeUniqueNo;

        @JsonProperty("depositBalance")
        private String depositBalance;

        @JsonProperty("withdrawalAccountNo")
        private String withdrawalAccountNo;
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
    public static class AccountResponseData {
        @JsonProperty("bankCode")
        private String bankCode;

        @JsonProperty("bankName")
        private String bankName;

        @JsonProperty("accountNo")
        private String accountNo;

        @JsonProperty("withdrawalBankCode")
        private String withdrawalBankCode;

        @JsonProperty("withdrawalAccountNo")
        private String withdrawalAccountNo;
        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("interestRate")
        private String interestRate;

        @JsonProperty("subscriptionPeriod")
        private String subscriptionPeriod;

        @JsonProperty("depositBalance")
        private String depositBalance;

        @JsonProperty("accountCreateDate")
        private String accountCreateDate;

        @JsonProperty("accountExpiryDate")
        private String accountExpiryDate;

    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {
        private String accountTypeUniqueNo;
        private String withdrawalAccountNo;
        private String depositBalance;
        public CreateAccountRequest toCreateAccountRequest() {
            return CreateAccountRequest.builder()
                    .withdrawalAccountNo(withdrawalAccountNo)
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .depositBalance(depositBalance)
                    .build();
        }
    }
}