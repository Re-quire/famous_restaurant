package com.groom.yummy.region;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public Optional<Region> findRegionById(Long id) {
        return regionRepository.findRegionById(id);
    }

    public Long createRegion(Region region) {
        regionRepository.findAllRegions().stream()
                .filter(r -> r.getRegionName().equals(region.getRegionName()) || r.getRegionCode().equals(region.getRegionCode()))
                .findAny()
                .ifPresent(r -> {
                    throw new IllegalArgumentException("이미 등록되어있는 지역입니다.");
                });

    return regionRepository.saveRegion(region);
    }

    public List<Region> getAllRegions() {
        return regionRepository.findAllRegions();
    }
}

