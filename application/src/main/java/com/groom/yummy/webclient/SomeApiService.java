package com.groom.yummy.webclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.store.Category;
import com.groom.yummy.store.Store;
import com.groom.yummy.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SomeApiService {
    private final WebClient.Builder webClientBuilder;
    private final StoreService storeService;

    public String getSomeData(String regionCode) {
        WebClient webClient = webClientBuilder.build();
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/regions")
                            .queryParam("regionCode", regionCode)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            List<Store> stores = parseStoresFromResponse(response);
            stores.forEach(storeService::createStore);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("WebClient 요청 중 오류 발생", e);
        }
    }

    private List<Store> parseStoresFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode storesNode = rootNode.path("data").path("stores");
            return objectMapper.convertValue(storesNode, new TypeReference<List<Store>>() {})
                    .stream()
                    .map(store -> Store.builder()
                            .storeId(store.getStoreId())
                            .name(store.getName())
                            .category(Category.fromApiCode(String.valueOf(store.getCategory())))
                            .regionId(store.getRegionId())
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }

}
