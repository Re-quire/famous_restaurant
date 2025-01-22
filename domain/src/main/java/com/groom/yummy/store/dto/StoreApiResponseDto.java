package com.groom.yummy.store.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreApiResponseDto {

    private Long storeId;
    private String name;
    private String category;
    private Long regionId;

    public static StoreApiResponseDto fromJsonNode(JsonNode node) {
        return StoreApiResponseDto.builder()
                .storeId(node.path("storeId").asLong())
                .name(node.path("name").asText())
                .category(node.path("category").asText())
                .regionId(node.path("regionId").asLong())
                .build();
    }

}
