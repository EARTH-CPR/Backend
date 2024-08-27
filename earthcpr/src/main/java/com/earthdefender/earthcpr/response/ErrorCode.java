package com.earthdefender.earthcpr.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "request 필수 항목을 확인하세요."),
    USER_ID_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 등록된 ID입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
