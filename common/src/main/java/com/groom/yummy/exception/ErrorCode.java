package com.groom.yummy.exception;

import org.springframework.http.HttpStatusCode;

public interface ErrorCode {
    HttpStatusCode getCode();
    String getMessage();
}
