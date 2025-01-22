package com.groom.yummy.exception;

import org.springframework.http.HttpStatus;

public enum ReplyErrorCode {

    ;
    private final HttpStatus code;
    private final String message;

    ReplyErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
