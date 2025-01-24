package com.groom.yummy.group.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDetailResponseDto {
    private Long id;
    private String name;
    private String category;
    private Long regionId;
}

