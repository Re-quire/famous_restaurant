package com.groom.yummy.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    private Long storeId;
    private String name;
    private Category_ category;
    private Long regionId;

}
