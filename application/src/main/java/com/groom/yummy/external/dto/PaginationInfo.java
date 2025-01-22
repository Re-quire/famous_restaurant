package com.groom.yummy.external.dto;

public record PaginationInfo(
        int page,
        int size,
        int totalPages,
        long totalElements
) {
}
