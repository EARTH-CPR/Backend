package com.earthdefender.earthcpr.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCodeDto {
    private int status;
    private String code;
    private String message;
}
