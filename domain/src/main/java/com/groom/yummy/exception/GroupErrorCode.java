package com.groom.yummy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GroupErrorCode implements ErrorCode{
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹이 존재하지 않습니다."),
    GROUP_PARTICIPATION_FULL(HttpStatus.NOT_FOUND, "참가 인원이 가득 찼습니다."),
    GROUP_CREATE_FAILED(HttpStatus.NOT_FOUND, "그룹 생성이 실패했습니다.")
    ;
    private final HttpStatus code;
    private final String message;

    GroupErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
