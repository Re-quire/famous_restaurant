package com.groom.yummy.region;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;


    @Test
    @Order(1)
    @DisplayName("지역 저장 성공 테스트")
    void 지역_저장_성공_테스트() {
        //Given
        Region region = Region.builder()
                .regionName("구름동")
                .regionCode("99999999")
                .build();

        when(regionRepository.findAllRegions()).thenReturn(List.of());
        when(regionRepository.saveRegion(any(Region.class))).thenReturn(1L);

        // When
        Long regionId = regionService.createRegion(region);

        // Then
        assertThat(regionId).isEqualTo(1L);
        verify(regionRepository).saveRegion(any(Region.class));
    }

    @Test
    @Order(2)
    @DisplayName("중복된 지역 정보로 저장 시 실패")
    void 중복_자역_저장_실패_테스트() {
        // Given
        Region region = Region.builder()
                .regionName("서울특별시 강남구")
                .regionCode("1111010100")
                .build();

        when(regionRepository.findAllRegions()).thenReturn(List.of(region));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> regionService.createRegion(region));
        verify(regionRepository, never()).saveRegion(any(Region.class));
    }
}
