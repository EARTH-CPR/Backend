package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DemandDepositProductDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest extends ShinhanApiDTO.RequestHeader {
        @JsonProperty("bankCode")
        private String bankCode;

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("accountDescription")
        private String accountDescription;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private ResponseData rec;
    }




    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseData {
        @JsonProperty("accountTypeUniqueNo")
        private String accountTypeUniqueNo;

        @JsonProperty("bankCode")
        private String bankCode;

        @JsonProperty("bankName")
        private String bankName;

        @JsonProperty("accountTypeCode")
        private String accountTypeCode;

        @JsonProperty("accountTypeName")
        private String accountTypeName;

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("accountDescription")
        private String accountDescription;

        @JsonProperty("accountType")
        private String accountType;


    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {
        private String bankCode;
        private String accountName;
        private String accountDescription;

        public CreateRequest toCreateRequest() {
            return CreateRequest.builder()
                    .bankCode(bankCode)
                    .accountName(accountName)
                    .accountDescription(accountDescription)
                    .build();
        }
    }
}