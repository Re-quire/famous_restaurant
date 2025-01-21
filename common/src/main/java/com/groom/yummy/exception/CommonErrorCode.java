package com.groom.yummy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements ErrorCode{
    // 서버 관련 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 알 수 없는 에러가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스와 상호작용 중 에러가 발생했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서버가 요청을 처리할 준비가 되어 있지 않음."),
    ;

    private final HttpStatus code;
    private final String message;

    CommonErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}

