package com.groom.yummy.dto;


import lombok.Builder;
import lombok.Getter;

public record ResponseDto<T>(T data, String msg) {
    @Builder
    public ResponseDto {
    }

    public static <T> ResponseDto<T> of(T data, String msg) {
        return ResponseDto.<T>builder().data(data).msg(msg).build();
    }
}
