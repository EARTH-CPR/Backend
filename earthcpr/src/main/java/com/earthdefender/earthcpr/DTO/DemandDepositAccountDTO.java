package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DemandDepositAccountDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("accountTypeUniqueNo")
        private String accountTypeUniqueNo;
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
        private String accountTypeUniqueNo;

        public CreateAccountRequest toCreateAccountRequest(String accountTypeUniqueNo) {
            return CreateAccountRequest.builder()
                    .accountTypeUniqueNo(accountTypeUniqueNo)
                    .build();
        }
    }
}