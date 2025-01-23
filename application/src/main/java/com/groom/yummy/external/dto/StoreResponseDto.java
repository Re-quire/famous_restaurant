package com.groom.yummy.external.dto;


import lombok.Builder;

@Builder
public record StoreResponseDto(
        Long id,
        String name,
        Long storeId,
        String category,
        Long regionId
) {
}
