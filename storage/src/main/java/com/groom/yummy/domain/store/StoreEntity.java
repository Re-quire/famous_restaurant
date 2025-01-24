package com.groom.yummy.domain.store;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.region.RegionEntity;
import com.groom.yummy.store.Category_;
import com.groom.yummy.store.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store")
@SuperBuilder
public class StoreEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category_ category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private RegionEntity region;

    private StoreEntity(String name, Category_ category, RegionEntity region) {
        this.name = name;
        this.category = category;
        this.region = region;
    }

    public static Store toStoreDomain(StoreEntity storeEntity) {
        return Store.builder()
                .storeId(storeEntity.getId())
                .name(storeEntity.getName())
                .regionId(storeEntity.getRegion().getId())
                .category(storeEntity.getCategory())
                .build();
    }

    public static StoreEntity fromStoreDomain(Store store, RegionEntity region) {
        return StoreEntity.builder()
                .name(store.getName())
                .category(store.getCategory())
                .region(region)
                .build();
    }

    public static StoreEntity create(String name, Category_ category, RegionEntity region) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Store name must not be null or blank");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (region == null) {
            throw new IllegalArgumentException("Region must not be null");
        }
        return new StoreEntity(name, category, region);
    }

}
