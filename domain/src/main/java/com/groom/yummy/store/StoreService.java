package com.groom.yummy.store;

import com.groom.yummy.region.RegionService;
import com.groom.yummy.store.dto.StoreApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final RegionService regionService;

    public Long createStore(Store store) {
        regionService.findRegionById(store.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("지역을 찾을 수 없습니다."));

        storeRepository.findAllStores().stream()
                .filter(s -> s.getName().equals(store.getName()) && s.getRegionId().equals(store.getRegionId()))
                .findAny()
                .ifPresent(s -> {
                    throw new IllegalArgumentException("이미 등록되어있는 가게 정보 입니다.");
                });

        if (store.getCategory() == null || !Category_.isValidCategory(store.getCategory())) {
            throw new IllegalArgumentException("등록되지 않은 카테고리 입니다.");
        }

        return storeRepository.saveStore(store, store.getRegionId());
    }

    public void saveStores(List<StoreApiResponseDto> storeDtos) {
        List<Store> stores = storeDtos.stream()
                .map(dto -> Store.builder()
                        .storeId(dto.getStoreId())
                        .name(dto.getName())
                        .category(Category_.fromApiCode(dto.getCategory()))
                        .regionId(dto.getRegionId())
                        .build())
                .toList();

        for (Store store : stores) {
            boolean exists = storeRepository.existsByNameAndRegionId(store.getName(), store.getRegionId());
            if (!exists) {
                storeRepository.saveStore(store, store.getRegionId());
            }
        }
    }

    public Optional<Store> findStoreById(Long storeId) {
        return storeRepository.findStoreById(storeId);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAllStores();
    }
}

