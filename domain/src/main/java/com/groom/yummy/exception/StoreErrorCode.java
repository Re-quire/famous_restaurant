package com.groom.yummy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StoreErrorCode implements ErrorCode{
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    ;
    private final HttpStatus code;
    private final String message;

    StoreErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
