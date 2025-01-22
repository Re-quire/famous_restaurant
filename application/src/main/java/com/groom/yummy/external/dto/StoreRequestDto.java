package com.groom.yummy.external.dto;

import com.groom.yummy.store.Category_;
import lombok.Builder;

@Builder
public record StoreRequestDto(
        String name,
        Category_ category,
        Long regionId
) {
}
