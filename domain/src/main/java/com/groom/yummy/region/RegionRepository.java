package com.groom.yummy.region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findRegionById(Long id);
    Long saveRegion(Region region);
    List<Region> findAllRegions();
}
