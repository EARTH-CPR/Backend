package com.earthdefender.earthcpr.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeData {
        private Long id;
        private String name;
        private String info;
        private Long type;
        private Long verification;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeSuccessList {
        private Long id;
        private String name;
        private String info;
        private Long type;
        private Long verification;
        private List<LocalDateTime> localDateTimeList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeSuccessResponse {
        private boolean success;
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetChallengeSuccessRequest {
        private String loginId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetChallengeSuccessResponse {
        private String loginId;
    }
}
