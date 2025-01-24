package com.groom.yummy.controller;

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.external.Category;
import com.groom.yummy.external.StoreApiClient;
import com.groom.yummy.external.dto.ApiResponse;
import com.groom.yummy.external.dto.StoreListResponse;
import com.groom.yummy.external.dto.StoreResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[Store] Store API")
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreApiClient storeApiClient;

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


