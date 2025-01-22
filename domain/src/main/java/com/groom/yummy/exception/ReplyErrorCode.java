package com.groom.yummy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReplyErrorCode implements ErrorCode{
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    ;
    private final HttpStatus code;
    private final String message;

    ReplyErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
