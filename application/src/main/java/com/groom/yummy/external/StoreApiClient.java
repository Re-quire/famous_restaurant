package com.groom.yummy.external;

import com.groom.yummy.external.dto.ApiResponse;
import com.groom.yummy.external.dto.StoreListResponse;
import com.groom.yummy.external.dto.StoreResponseDto;
import com.groom.yummy.store.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StoreApiClient {
    private final WebClient webClient;

    public StoreResponseDto getStoreByApi(Long storeId){
        StoreResponseDto storeResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/store/{id}")
                        .build(storeId))
                .retrieve()
                .bodyToMono(StoreResponseDto.class)
                .block();
        return storeResponse;
    }

    public ApiResponse<StoreListResponse> getStoresByFilters(
            Category category, Long regionId, String name, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/stores")
                        .queryParam("category", category)
                        .queryParam("regionId", regionId)
                        .queryParam("name", name)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<StoreListResponse>>() {})
                .block();
    }
}
