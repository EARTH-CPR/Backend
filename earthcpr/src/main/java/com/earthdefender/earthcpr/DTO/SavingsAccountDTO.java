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
        @JsonProperty("loginId")
        private String loginId;

        @JsonProperty("accountTypeUniqueNo")
        private String accountTypeUniqueNo;

        @JsonProperty("depositBalance")
        private String depositBalance;

        @JsonProperty("withdrawalAccountNo")
        private String withdrawalAccountNo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountResponse extends ShinhanApiDTO.ResponseHeader {

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
        private String loginId;
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
        private String bankCode;
        private String bankName;
        private String accountName;
        private List<PaymentInfo> paymentInfo;

        public CreateAccountRequest toCreateAccountRequest() {
            return CreateAccountRequest.builder()
                    .loginId(loginId)
                    .withdrawalAccountNo(withdrawalAccountNo)
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .depositBalance(depositBalance)
                    .build();
        }
        public InquireAccountRequest toInquireAccountRequest() {
            return InquireAccountRequest.builder()
                    .loginId(loginId)
                    .accountNo(accountNo)
                    .build();
        }
        public InquirePaymentRequest toInquirePaymentRequest() {
            return InquirePaymentRequest.builder()
                    .loginId(loginId)
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
        @JsonProperty("loginId")
        private String loginId;

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
                    .accountName(this.accountName)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiInquirePaymentResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private List<InquirePaymentResponse> rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquirePaymentRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;
        @JsonProperty("accountNo")
        private String accountNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquirePaymentResponse {
        @JsonProperty("bankCode")
        private String bankCode;
        @JsonProperty("bankName")
        private String bankName;
        @JsonProperty("accountNo")
        private String accountNo;
        @JsonProperty("accountName")
        private String accountName;
        @JsonProperty("interestRate")
        private String interestRate;
        @JsonProperty("depositBalance")
        private String depositBalance;
        @JsonProperty("totalBalance")
        private String totalBalance;
        @JsonProperty("accountCreateDate")
        private String accountCreateDate;
        @JsonProperty("accountExpiryDate")
        private String accountExpiryDate;
        @JsonProperty("paymentInfo")
        private List<PaymentInfo> paymentInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        @JsonProperty("depositInstallment")
        private String depositInstallment;
        @JsonProperty("paymentBalance")
        private String paymentBalance;
        @JsonProperty("paymentDate")
        private String paymentDate;
        @JsonProperty("paymentTime")
        private String paymentTime;
        @JsonProperty("status")
        private String status;
        @JsonProperty("failureReason")
        private String failureReason;
    }
}