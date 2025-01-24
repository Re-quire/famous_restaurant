package com.groom.yummy.external.dto;


import lombok.Builder;
import com.groom.yummy.external.Category;


@Builder
public record StoreRequestDto(
        String name,
        Category category,
        Long regionId
) {
}
