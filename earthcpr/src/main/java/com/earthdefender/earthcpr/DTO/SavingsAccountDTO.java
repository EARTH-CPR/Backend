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
        private String accountNo;
        private String accountTypeUniqueNo;
        private String withdrawalAccountNo;
        private String interestRate;
        private String subscriptionPeriod;
        private String installmentNumber;
        private String totalBalance;
        private String accountCreateDate;
        private String accountExpiryDate;
        private String depositBalance;
        public CreateAccountRequest toCreateAccountRequest() {
            return CreateAccountRequest.builder()
                    .withdrawalAccountNo(withdrawalAccountNo)
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .depositBalance(depositBalance)
                    .build();
        }
        public InquireAccountRequest toInquireAccountRequest() {
            return InquireAccountRequest.builder()
                    .accountNo(accountNo)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiInquireAccountResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private InquireAccountResponse rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireAccountRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("accountNo")
        private String accountNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireAccountResponse {
        @JsonProperty("bankCode")
        private String bankCode;
        @JsonProperty("bankName")
        private String bankName;
        @JsonProperty("userName")
        private String userName;
        @JsonProperty("accountNo")
        private String accountNo;
        @JsonProperty("accountName")
        private String accountName;
        @JsonProperty("accountDescription")
        private String accountDescription;
        @JsonProperty("withdrawalBankCode")
        private String withdrawalBankCode;
        @JsonProperty("withdrawalBankName")
        private String withdrawalBankName;
        @JsonProperty("withdrawalAccountNo")
        private String withdrawalAccountNo;
        @JsonProperty("subscriptionPeriod")
        private String subscriptionPeriod;
        @JsonProperty("depositBalance")
        private String depositBalance;
        @JsonProperty("interestRate")
        private String interestRate;
        @JsonProperty("installmentNumber")
        private String installmentNumber;
        @JsonProperty("totalBalance")
        private String totalBalance;
        @JsonProperty("accountCreateDate")
        private String accountCreateDate;
        @JsonProperty("accountExpiryDate")
        private String accountExpiryDate;
        public ProductData toProductData() {
            return ProductData.builder()
                    .accountNo(this.accountNo)
                    .withdrawalAccountNo(this.withdrawalAccountNo)
                    .accountTypeUniqueNo(this.accountDescription)
                    .depositBalance(this.depositBalance)
                    .interestRate(this.interestRate)
                    .subscriptionPeriod(this.subscriptionPeriod)
                    .installmentNumber(this.installmentNumber)
                    .totalBalance(this.totalBalance)
                    .accountCreateDate(this.accountCreateDate)
                    .accountExpiryDate(this.accountExpiryDate)
                    .build();
        }
    }
}