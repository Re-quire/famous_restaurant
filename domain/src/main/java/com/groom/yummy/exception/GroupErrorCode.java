package com.groom.yummy.exception;

import org.springframework.http.HttpStatus;

public enum GroupErrorCode {

    ;
    private final HttpStatus code;
    private final String message;

    GroupErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
