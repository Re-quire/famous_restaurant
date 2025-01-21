package com.groom.yummy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode{
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    ;

    private final HttpStatus code;
    private final String message;

    UserErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
