package com.groom.yummy.domain.region;

import com.groom.yummy.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "region")
@SuperBuilder
public class RegionEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String regionName;

    @Column(nullable = false, unique = true)
    private String regionCode;

    @Builder
    public RegionEntity(String regionName, String regionCode) {
        this.regionName = regionName;
        this.regionCode = regionCode;
    }
}
