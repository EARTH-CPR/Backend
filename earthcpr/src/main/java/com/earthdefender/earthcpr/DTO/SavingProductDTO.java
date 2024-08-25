package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

public class SavingProductDTO {

    // Create Saving Product

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiCreateRequest extends ShinhanApiDTO.RequestHeader {

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

        public ShinhanApiCreateRequest(ShinhanApiDTO.RequestHeaderParam header, String accountName, String bankCode, String accountDescription, String subscriptionPeriod, Long minSubscriptionBalance, Long maxSubscriptionBalance, BigDecimal interestRate, String rateDescription) {
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
    public static class ShinhanApiCreateResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private ShinhanApiResponseData rec;
    }

    // Get Saving Product

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiGetSavingProductsResponse {
        @JsonProperty("Header")
        private ShinhanApiDTO.ResponseHeader header;

        @JsonProperty("REC")
        private List<ShinhanApiResponseData> rec;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetSavingProductsResponse {
        private List<ProductData> products;
    }

    // General

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShinhanApiResponseData {
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
        private Long minSubscriptionBalance;

        @JsonProperty("maxSubscriptionBalance")
        private Long maxSubscriptionBalance;

        @JsonProperty("interestRate")
        private BigDecimal interestRate;

        @JsonProperty("rateDescription")
        private String rateDescription;

        public ProductData toProductData(BigDecimal increaseInterestRate, List<ChallengeDTO.ChallengeData> challengeList) {
            return ProductData.builder()
                    .accountName(this.accountName)
                    .bankCode(this.bankCode)
                    .accountDescription(this.accountDescription)
                    .subscriptionPeriod(this.subscriptionPeriod)
                    .minSubscriptionBalance(this.minSubscriptionBalance)
                    .maxSubscriptionBalance(this.maxSubscriptionBalance)
                    .interestRate(this.interestRate)
                    .rateDescription(this.rateDescription)
                    .increaseInterestRate(increaseInterestRate)
                    .challengeList(challengeList)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {
        private BigDecimal increaseInterestRate;
        private List<ChallengeDTO.ChallengeData> challengeList;

        // Shinhan API Request를 위한 데이터
        private String accountName;
        private String bankCode;
        private String accountDescription;
        private String subscriptionPeriod;
        private Long minSubscriptionBalance;
        private Long maxSubscriptionBalance;
        private BigDecimal interestRate;
        private String rateDescription;

        public ShinhanApiCreateRequest toShinhanApiRequest() {
            return ShinhanApiCreateRequest.builder()
                    .accountName(this.accountName)
                    .bankCode(this.bankCode)
                    .accountDescription(this.accountDescription)
                    .subscriptionPeriod(this.subscriptionPeriod)
                    .minSubscriptionBalance(this.minSubscriptionBalance)
                    .maxSubscriptionBalance(this.maxSubscriptionBalance)
                    .interestRate(this.interestRate)
                    .rateDescription(this.rateDescription)
                    .build();
        }
    }
}
