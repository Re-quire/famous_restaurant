package com.groom.yummy.domain.store;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.domain.group.GroupEntity;
import com.groom.yummy.domain.region.RegionEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
    private Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private RegionEntity region;

    private StoreEntity(String name, Category category, RegionEntity region) {
        this.name = name;
        this.category = category;
        this.region = region;
    }

    private StoreEntity(String name, Category category, RegionEntity region, List<GroupEntity> groups) {
        this.name = name;
        this.category = category;
        this.region = region;
    }

    // 필수 필드만 생성
    public static StoreEntity create(String name, Category category, RegionEntity region) {
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

    // 그룹 포함 생성
    private static StoreEntity createWithGroups(String name, Category category, RegionEntity region, List<GroupEntity> groups) {
        StoreEntity store = create(name, category, region);
        return store;
    }
}
