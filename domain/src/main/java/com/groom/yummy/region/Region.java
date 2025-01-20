package com.groom.yummy.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Region {

    private Long id;
    private String regionName;
    private String regionCode;
}
