package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ShinhanApiDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestHeader {
        @JsonProperty("Header")
        private RequestHeaderParam header;

    }

    @Builder
    @Data
    public static class RequestHeaderParam {
        @JsonProperty("apiName")
        private String apiName;

        @JsonProperty("transmissionDate")
        private String transmissionDate;

        @JsonProperty("transmissionTime")
        private String transmissionTime;

        @JsonProperty("institutionCode")
        private String institutionCode;

        @JsonProperty("fintechAppNo")
        private String fintechAppNo;

        @JsonProperty("apiServiceCode")
        private String apiServiceCode;

        @JsonProperty("institutionTransactionUniqueNo")
        private String institutionTransactionUniqueNo;

        @JsonProperty("apiKey")
        private String apiKey;

        @JsonProperty("userKey")
        private String userKey;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseHeader {
        @JsonProperty("responseCode")
        private String responseCode;

        @JsonProperty("responseMessage")
        private String responseMessage;

        @JsonProperty("apiName")
        private String apiName;

        @JsonProperty("transmissionDate")
        private String transmissionDate;

        @JsonProperty("transmissionTime")
        private String transmissionTime;

        @JsonProperty("institutionCode")
        private String institutionCode;

        @JsonProperty("apiKey")
        private String apiKey;

        @JsonProperty("apiServiceCode")
        private String apiServiceCode;

        @JsonProperty("institutionTransactionUniqueNo")
        private String institutionTransactionUniqueNo;
    }
}
