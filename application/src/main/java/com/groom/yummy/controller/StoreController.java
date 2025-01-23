package com.groom.yummy.controller;

import com.groom.yummy.dto.ResponseDto;

import com.groom.yummy.external.Category;
import com.groom.yummy.external.StoreApiClient;
import com.groom.yummy.external.dto.ApiResponse;
import com.groom.yummy.external.dto.StoreListResponse;
import com.groom.yummy.external.dto.StoreResponseDto;

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

//    @GetMapping("/sync")
//    public ResponseEntity<ResponseDto<Void>> syncStores(@RequestParam String regionCode) {
//        try {
//            List<StoreApiResponseDto> storeDtos = someApiService.fetchStoresFromApi(regionCode);
//
//            // 각 StoreApiResponseDto의 category를 내부 Category enum으로 변환
//            storeDtos.forEach(dto -> {
//                String description = dto.getCategory();
//                String apiCode = Category_.fromDescription(description).getApiCode();
//                dto.setCategory(apiCode); // description -> apiCode로 변환
//            });
//
//            storeService.saveStores(storeDtos);
//
//            return ResponseEntity.ok(new ResponseDto<>(null, "가게 데이터 동기화 성공"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseDto<>(null, "가게 데이터 동기화 중 오류 발생"));
//        }
//    }

    @Operation(summary = "가게 정보 조회", description = "가게 id로 가게를 조회합니다.")
    @GetMapping("/{storeId}")
    public ResponseEntity<ResponseDto<StoreResponseDto>> getStore(@PathVariable("storeId") Long storeId) {
        StoreResponseDto storeResponseDTO = storeApiClient.getStoreByApi(storeId);
        return ResponseEntity.ok(ResponseDto.of(storeResponseDTO,"storeId로 가게 조회 성공"));
    }

    @Operation(summary = "가게 조회", description = "가게들을 정렬 기준에 맞게 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<StoreListResponse>> getStoresByFilters(
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "regionId", required = false) Long regionId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ApiResponse<StoreListResponse> storeListResponseApiResponse = storeApiClient.getStoresByFilters(category, regionId,name,page,size);
        return ResponseEntity.ok(storeListResponseApiResponse);
    }
}


