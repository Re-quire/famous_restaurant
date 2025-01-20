package com.groom.yummy.domain.region;

import com.groom.yummy.region.Region;
import com.groom.yummy.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RegionEntityRepository implements RegionRepository {

    private final RegionJpaRepository regionJpaRepository;

    @Override
    public Optional<Region> findRegionById(Long id) {
        return regionJpaRepository.findById(id).map(RegionEntity::toRegionDomain);
    }

    @Override
    public Long saveRegion(Region region) {
        RegionEntity regionEntity = RegionEntity.fromRegionDomain(region);
        return regionJpaRepository.save(regionEntity).getId();
    }

    @Override
    public List<Region> findAllRegions() {
        return regionJpaRepository.findAll().stream()
                .map(RegionEntity::toRegionDomain)
                .collect(Collectors.toList());
    }
}
