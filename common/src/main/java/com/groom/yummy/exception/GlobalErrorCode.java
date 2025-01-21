package com.groom.yummy.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class GlobalErrorCode {
    private final HttpStatus errorCode;
    private final String message;
}
