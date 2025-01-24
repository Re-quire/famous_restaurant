package com.groom.yummy.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.store.Category_;
import com.groom.yummy.store.Store;
import com.groom.yummy.store.StoreService;
import com.groom.yummy.store.dto.StoreApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SomeApiService {
    private final WebClient.Builder webClientBuilder;
    private final StoreService storeService; // StoreService 주입

    public List<StoreApiResponseDto> fetchStoresFromApi(String regionCode) {
        WebClient webClient = webClientBuilder.build();
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/stores")
                            .queryParam("regionCode", regionCode)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // JSON 응답을 파싱하여 StoreApiResponseDto 리스트 생성
            List<StoreApiResponseDto> storeDtos = parseStoresFromResponse(response);

            // Store 객체로 변환 후 StoreService에 저장 요청
            storeDtos.forEach(dto -> {
                Store store = Store.builder()
                        .storeId(dto.getStoreId())
                        .name(dto.getName())
                        .category(Category_.fromApiCode(dto.getCategory()))
                        .regionId(dto.getRegionId())
                        .build();
                storeService.createStore(store);
            });

            return storeDtos; // 반환값 추가
        } catch (Exception e) {
            throw new RuntimeException("WebClient 요청 중 오류 발생", e);
        }
    }


    private List<StoreApiResponseDto> parseStoresFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode storesNode = objectMapper.readTree(response).path("data").path("stores");
            return StreamSupport.stream(storesNode.spliterator(), false)
                    .map(StoreApiResponseDto::fromJsonNode)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }
}


