package com.groom.yummy.domain.region;

import com.groom.yummy.domain.BaseEntity;
import com.groom.yummy.region.Region;
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

    public Region toRegionDomain() {
        return Region.builder()
                .id(this.getId())
                .regionName(this.regionName)
                .regionCode(this.regionCode)
                .build();
    }

    public static RegionEntity fromRegionDomain(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("지역 설정은 필수입니다.");
        }
        return new RegionEntity(region.getRegionName(), region.getRegionCode());
    }
}
