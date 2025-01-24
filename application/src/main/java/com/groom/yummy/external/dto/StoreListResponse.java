package com.groom.yummy.external.dto;

import java.util.List;


public record StoreListResponse(
        List<StoreResponseDto> stores,
        PaginationInfo pageable
) {}
