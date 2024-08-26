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
        private String user_nickname;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class UserShinhanRequest {
        @JsonProperty("userId")
        private String login_id;
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
                    .login_id(loginId)
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
        private String login_id;
        @JsonProperty("userKey")
        private String user_key;
        @JsonIgnore
        private String password;
        @JsonIgnore
        private String user_nickname;
    }
}
