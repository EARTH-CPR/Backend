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
        private Long depositBalance;

        @JsonProperty("withdrawalAccountNo")
        private String withdrawalAccountNo;

        public CreateAccountRequest(ShinhanApiDTO.RequestHeaderParam header, String accountTypeUniqueNo, Long depositBalance, String withdrawalAccountNo) {
            super(header);
            this.accountTypeUniqueNo = accountTypeUniqueNo;
            this.depositBalance = depositBalance;
            this.withdrawalAccountNo = withdrawalAccountNo;
        }
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
        private Long depositBalance;
        private String withdrawalAccountNo;
        public CreateAccountRequest toCreateAccountRequest() {
            return CreateAccountRequest.builder()
                    .accountTypeUniqueNo(this.accountTypeUniqueNo)
                    .depositBalance(this.depositBalance)
                    .withdrawalAccountNo(this.withdrawalAccountNo)
                    .build();
        }
    }
}