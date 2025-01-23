package com.groom.yummy.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    CHICKEN("치킨"),
    CHINESE("중식"),
    CUTLET_SASHIMI("돈까스-회"),
    PIZZA("피자"),
    FAST_FOOD("패스트푸드"),
    STEW_SOUP("찜-탕"),
    JOKBAL_BOSSAM("족발-보쌈"),
    SNACK("분식"),
    CAFE_DESSERT("카페-디저트"),
    KOREAN("한식"),
    MEAT("고기"),
    WESTERN("양식"),
    ASIAN("아시안"),
    LATE_NIGHT("야식"),
    LUNCH_BOX("도시락");
    private final String description;

}
