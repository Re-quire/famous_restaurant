package com.groom.yummy.domain.store;

import com.groom.yummy.domain.region.RegionEntity;
import com.groom.yummy.domain.region.RegionJpaRepository;
import com.groom.yummy.store.Store;
import com.groom.yummy.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StoreEntityRepository implements StoreRepository {

    private final StoreJpaRepository storeJpaRepository;
    private final RegionJpaRepository regionJpaRepository;

    @Override
    public Optional<Store> findStoreById(Long id) {
        return storeJpaRepository.findById(id).map(StoreEntity::toStoreDomain);
    }

    @Override
    public Long saveStore(Store store, Long regionId) {
        RegionEntity regionEntity = regionJpaRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("지역을 찾을 수 없습니다."));

        StoreEntity storeEntity = StoreEntity.fromStoreDomain(store, regionEntity);
        return storeJpaRepository.save(storeEntity).getId();
    }

    @Override
    public List<Store> findAllStores() {
        return storeJpaRepository.findAll().stream()
                .map(StoreEntity::toStoreDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndRegionId(String name, Long regionId) {
        return storeJpaRepository.existsByNameAndRegion_Id(name, regionId);
    }
}
