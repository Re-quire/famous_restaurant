package com.groom.yummy.store;

import com.groom.yummy.region.Region;
import com.groom.yummy.region.RegionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private RegionService regionService;

    @InjectMocks
    private StoreService storeService;


    @Test
    @Order(1)
    @DisplayName("요청 API 가게 정보 등록 성공 테스트")
    void 가게_등록_성공_테스트() {
        // Given
        Store store = Store.builder()
                .name("GoormStore")
                .category(Category_.PIZZA)
                .regionId(1L)
                .build();

        Region region = Region.builder()
                .id(1L)
                .regionName("구름동")
                .regionCode("99999999")
                .build();

        when(regionService.findRegionById(1L)).thenReturn(Optional.of(region));
        when(storeRepository.saveStore(any(Store.class), any(Long.class))).thenReturn(1L);

        // When
        Long storeId = storeService.createStore(store);

        // Then
        assertThat(storeId).isEqualTo(1L);
        verify(regionService).findRegionById(1L);
        verify(storeRepository).saveStore(any(Store.class), eq(1L));
    }

    @Test
    @Order(2)
    @DisplayName("요청 API 가게 정보 등록 실패 테스트")
    void 가게_등록_실패_테스트() {
        // GIVEN
        Store store = Store.builder()
                .name("GoormStore")
                .category(Category_.PIZZA)
                .regionId(1L)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> storeService.createStore(store));
        verify(storeRepository, never()).saveStore(any(Store.class), anyLong());
    }
}
