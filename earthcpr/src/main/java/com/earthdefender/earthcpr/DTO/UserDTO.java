package com.earthdefender.earthcpr.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserDTO {

    @Data
    @NoArgsConstructor
    public static class UserRequest {
        @JsonProperty("password")
        private String password;
        @JsonProperty("userNickname")
        private String userNickname;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class UserShinhanRequest {
        @JsonProperty("userId")
        private String loginId;
        @JsonProperty("apiKey")
        private String apiKey;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserData{
        private String loginId;
        private String password;
        private String userNickname;

        public UserShinhanRequest toShinhanRequest(String apiKey){
            return UserShinhanRequest.builder()
                    .loginId(loginId)
                    .apiKey(apiKey)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        @JsonProperty("userId")
        private String loginId;
        @JsonProperty("userKey")
        private String userKey;
        @JsonIgnore
        private String password;
        @JsonIgnore
        private String userNickname;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserLoginRequest {
        private String loginId;
        private String password;
    }
}
