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
    public static class ProductData {
        private String loginId;
        private String accountTypeUniqueNo;
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
    }
}