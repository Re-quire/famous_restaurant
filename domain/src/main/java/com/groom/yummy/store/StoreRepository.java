package com.groom.yummy.store;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {
    Optional<Store> findStoreById(Long id);
    Long saveStore(Store store, Long regionId);
    List<Store> findAllStores();
}
