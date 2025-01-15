package com.groom.yummy.domain.store;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Id> {
}
