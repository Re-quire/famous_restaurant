package com.groom.yummy.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {
    boolean existsByNameAndRegion_Id(String name, Long regionId);
}
