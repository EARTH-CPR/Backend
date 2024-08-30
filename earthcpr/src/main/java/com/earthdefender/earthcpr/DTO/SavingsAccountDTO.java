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
    public static class CreateAccountData{
        private String loginId;
        private String accountTypeUniqueNo;
        private String depositBalance;
        private String withdrawalAccountNo;
        public CreateAccountRequest toCreateAccountRequest() {
            return CreateAccountRequest.builder()
                    .loginId(loginId)
                    .withdrawalAccountNo(withdrawalAccountNo)
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .depositBalance(depositBalance)
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireAccountData {
        private String loginId;
        private String accountNo;

        public InquireAccountRequest toInquireAccountRequest() {
            return InquireAccountRequest.builder()
                    .loginId(loginId)
                    .accountNo(accountNo)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireAccountResponseData {
        private String bankCode;
        private String bankName;
        private String userName;
        private String accountNo;
        private String accountName;
        private String accountDescription;
        private String withdrawalBankCode;
        private String withdrawalBankName;
        private String withdrawalAccountNo;
        private String subscriptionPeriod;
        private String depositBalance;
        private String interestRate;
        private String installmentNumber;
        private String totalBalance;
        private String accountCreateDate;
        private String accountExpiryDate;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquirePaymentData {
        private String loginId;
        private String accountNo;
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
    public static class InquireExpiryData {
        private String loginId;
        private String accountNo;
        public InquireExpiryRequest toInquireExpiryRequest() {
            return InquireExpiryRequest.builder()
                    .loginId(loginId)
                    .accountNo(accountNo)
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireEarlyData {
        private String loginId;
        private String accountNo;
        public InquireEarlyRequest toInquireEarlyRequest() {
            return InquireEarlyRequest.builder()
                    .loginId(loginId)
                    .accountNo(accountNo)
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginIdData{
        private String loginId;
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

        public InquireAccountResponseData toInquireAccountResponseData() {
            return InquireAccountResponseData.builder()
                    .bankCode(this.bankCode)
                    .bankName(this.bankName)
                    .userName(this.userName)
                    .accountNo(this.accountNo)
                    .accountName(this.accountName)
                    .accountDescription(this.accountDescription)
                    .withdrawalBankCode(this.withdrawalBankCode)
                    .withdrawalBankName(this.withdrawalBankName)
                    .withdrawalAccountNo(this.withdrawalAccountNo)
                    .subscriptionPeriod(this.subscriptionPeriod)
                    .depositBalance(this.depositBalance)
                    .interestRate(this.interestRate)
                    .installmentNumber(this.installmentNumber)
                    .totalBalance(this.totalBalance)
                    .accountCreateDate(this.accountCreateDate)
                    .accountExpiryDate(this.accountExpiryDate)
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireExpiryRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;

        @JsonProperty("accountNo")
        private String accountNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiInquireExpiryResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private InquireExpiryResponse rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireExpiryResponse {
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

        @JsonProperty("accountCreateDate")
        private String accountCreateDate;

        @JsonProperty("accountExpiryDate")
        private String accountExpiryDate;

        @JsonProperty("expiryBalance")
        private String expiryBalance;

        @JsonProperty("expiryInterest")
        private String expiryInterest;

        @JsonProperty("expiryTotalBalance")
        private String expiryTotalBalance;

        public InquireExpiryResponseData toInquireExpiryResponseData() {
            return InquireExpiryResponseData.builder()
                    .accountNo(this.accountNo)
                    .interestRate(this.interestRate)
                    .accountCreateDate(this.accountCreateDate)
                    .accountExpiryDate(this.accountExpiryDate)
                    .totalBalance(this.expiryTotalBalance)
                    .expiryBalance(this.expiryBalance)
                    .expiryInterest(this.expiryInterest)
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireExpiryResponseData{
        private String accountNo;
        private String interestRate;
        private String accountCreateDate;
        private String accountExpiryDate;
        private String expiryBalance;
        private String expiryInterest;
        private String expiryTotalBalance;
        private String totalBalance;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireEarlyRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;

        @JsonProperty("accountNo")
        private String accountNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiInquireEarlyResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private InquireEarlyResponse rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireEarlyResponse {
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

        @JsonProperty("accountCreateDate")
        private String accountCreateDate;

        @JsonProperty("earlyTerminationDate")
        private String earlyTerminationDate;

        @JsonProperty("totalBalance")
        private String totalBalance;

        @JsonProperty("earlyTerminationInterest")
        private String earlyTerminationInterest;

        @JsonProperty("earlyTerminationBalance")
        private String earlyTerminationBalance;

        public InquireEarlyResponseData toInquireEarlyResponseData() {
            return InquireEarlyResponseData.builder()
                    .accountNo(this.accountNo)
                    .interestRate(this.interestRate)
                    .accountCreateDate(this.accountCreateDate)
                    .earlyTerminationDate(this.earlyTerminationDate)
                    .totalBalance(this.totalBalance)
                    .earlyTerminationInterest(this.earlyTerminationInterest)
                    .earlyTerminationBalance(this.earlyTerminationBalance)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireEarlyResponseData {
        private String accountNo;
        private String interestRate;
        private String accountCreateDate;
        private String earlyTerminationDate;
        private String totalBalance;
        private String earlyTerminationInterest;
        private String earlyTerminationBalance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class deleteAccountRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("loginId")
        private String loginId;

        @JsonProperty("accountNo")
        private String accountNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApideleteAccountResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private deleteAccountResponse rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class deleteAccountResponse {
        @JsonProperty("status")
        private String status;

        @JsonProperty("bankCode")
        private String bankCode;

        @JsonProperty("bankName")
        private String bankName;

        @JsonProperty("accountNo")
        private String accountNo;

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("earlyTerminationDate")
        private String earlyTerminationDate;

        @JsonProperty("totalBalance")
        private String totalBalance;

        @JsonProperty("earlyTerminationInterest")
        private String earlyTerminationInterest;

        @JsonProperty("earlyTerminationBalance")
        private String earlyTerminationBalance;

        public DeleteAccountResponseData toDeleteAccountResponseData() {
            return DeleteAccountResponseData.builder()
                    .status(this.status)
                    .accountNo(this.accountNo)
                    .earlyTerminationDate(this.earlyTerminationDate)
                    .totalBalance(this.totalBalance)
                    .earlyTerminationInterest(this.earlyTerminationInterest)
                    .earlyTerminationBalance(this.earlyTerminationBalance)
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteAccountResponseData {
        private String status;
        private String accountNo;
        private String earlyTerminationDate;
        private String totalBalance;
        private String earlyTerminationInterest;
        private String earlyTerminationBalance;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecResponse {
        @JsonProperty("totalCount")
        private String totalCount;

        @JsonProperty("list")
        private List<SavingAccountListResponse> list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiSavingAccountListResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private RecResponse rec;
    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SavingAccountListResponse {
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

    }
}