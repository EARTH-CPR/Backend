package com.earthdefender.earthcpr.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FriendDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendData {
        private Long user1Id;
        private Long user2Id;
    }
}