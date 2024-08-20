package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

public class SavingsProductDTO {

    @Data
    @NoArgsConstructor
    public static class ShinhanApiRequest extends ShinhanApiDTO.RequestHeader {

        @JsonProperty("accountName")
        private String accountName;

        @JsonProperty("bankCode")
        private String bankCode;

        @JsonProperty("accountDescription")
        private String accountDescription;

        @JsonProperty("subscriptionPeriod")
        private String subscriptionPeriod;

        @JsonProperty("minSubscriptionBalance")
        private Long minSubscriptionBalance;

        @JsonProperty("maxSubscriptionBalance")
        private Long maxSubscriptionBalance;

        @JsonProperty("interestRate")
        private BigDecimal interestRate;

        @JsonProperty("rateDescription")
        private String rateDescription;

        public ShinhanApiRequest(ShinhanApiDTO.RequestHeaderParam header, String accountName, String bankCode, String accountDescription, String subscriptionPeriod, Long minSubscriptionBalance, Long maxSubscriptionBalance, BigDecimal interestRate, String rateDescription) {
            super(header);
            this.accountName = accountName;
            this.bankCode = bankCode;
            this.accountDescription = accountDescription;
            this.subscriptionPeriod = subscriptionPeriod;
            this.minSubscriptionBalance = minSubscriptionBalance;
            this.maxSubscriptionBalance = maxSubscriptionBalance;
            this.interestRate = interestRate;
            this.rateDescription = rateDescription;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private SavingsProductDTO.ResponseBody rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResponseBody {
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

        @JsonProperty("subscriptionPeriod")
        private String subscriptionPeriod;

        @JsonProperty("minSubscriptionBalance")
        private String minSubscriptionBalance;

        @JsonProperty("maxSubscriptionBalance")
        private String maxSubscriptionBalance;

        @JsonProperty("interestRate")
        private String interestRate;

        @JsonProperty("rateDescription")
        private String rateDescription;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSavingsProductRequest {
        private String accountName;
        private String bankCode;
        private String accountDescription;
        private String subscriptionPeriod;
        private Long minSubscriptionBalance;
        private Long maxSubscriptionBalance;
        private BigDecimal interestRate;
        private String rateDescription;

        private BigDecimal increaseInterestRate;
        private List<Long> challengeIdList;
    }
}
