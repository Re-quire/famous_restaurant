package com.groom.yummy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements ErrorCode {
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED,"토큰의 서명이 유효하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,"잘못된 형식의 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    UNKNOWN_TOKEN_ERROR(HttpStatus.BAD_REQUEST,"토큰의 값이 존재하지 않습니다."),
    ;
    private final HttpStatus code;
    private final String message;

    JwtErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
