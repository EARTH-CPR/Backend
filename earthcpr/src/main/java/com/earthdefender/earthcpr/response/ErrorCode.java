package com.earthdefender.earthcpr.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "request 필수 항목을 확인하세요."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
