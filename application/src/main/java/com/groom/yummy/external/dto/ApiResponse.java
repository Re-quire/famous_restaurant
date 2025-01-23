package com.groom.yummy.external.dto;

public record ApiResponse<T>(
        T data,
        String message
) {}
