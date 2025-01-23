package com.groom.yummy.controller;

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.external.StoreApiClient;
import com.groom.yummy.external.dto.ApiResponse;
import com.groom.yummy.external.dto.StoreListResponse;
import com.groom.yummy.external.dto.StoreRequestDto;
import com.groom.yummy.external.dto.StoreResponseDto;
import com.groom.yummy.store.Category;
import com.groom.yummy.store.StoreService;
import com.groom.yummy.store.dto.StoreApiResponseDto;
import com.groom.yummy.webclient.SomeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[Store] Store API")
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final SomeApiService someApiService;
    private final StoreService storeService;
    private final StoreApiClient storeApiClient;

    @GetMapping("/sync")
    public ResponseEntity<ResponseDto<Void>> syncStores(@RequestParam String regionCode) {
        try {
            List<StoreApiResponseDto> storeDtos = someApiService.fetchStoresFromApi(regionCode);

            // 각 StoreApiResponseDto의 category를 내부 Category enum으로 변환
            storeDtos.forEach(dto -> {
                String description = dto.getCategory();
                String apiCode = Category.fromDescription(description).getApiCode();
                dto.setCategory(apiCode); // description -> apiCode로 변환
            });

            storeService.saveStores(storeDtos);

            return ResponseEntity.ok(new ResponseDto<>(null, "가게 데이터 동기화 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto<>(null, "가게 데이터 동기화 중 오류 발생"));
        }
    }
}


