package com.groom.yummy.store;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Category_ {
    // TODO : apiCode 수정
    CHICKEN("001","치킨"),
    CHINESE("002","중식"),
    CUTLET_SASHIMI("003","돈까스-회"),
    PIZZA("004","피자"),
    FAST_FOOD("005","패스트푸드"),
    STEW_SOUP("006","찜-탕"),
    JOKBAL_BOSSAM("007","족발-보쌈"),
    SNACK("008","분식"),
    CAFE_DESSERT("009","카페-디저트"),
    KOREAN("010","한식"),
    MEAT("011","고기"),
    WESTERN("012","양식"),
    ASIAN("013","아시안"),
    LATE_NIGHT("014","야식"),
    LUNCH_BOX("015","도시락");

    private final String apiCode;
    private final String description;

    private static final Map<String, Category_> API_CODE_TO_CATEGORY = new HashMap<>();

    static {
        for(Category_ category: values()){
            API_CODE_TO_CATEGORY.put(category.getApiCode(), category);
        }
    }

    Category_(String apiCode, String description) {
        this.apiCode = apiCode;
        this.description = description;
    }

    public static Category_ fromApiCode(String apiCode) {
        return API_CODE_TO_CATEGORY.get(apiCode);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) ", apiCode, description);
    }

    public static boolean isValidCategory(Category_ category) {
        return API_CODE_TO_CATEGORY.containsValue(category);
    }

    public static Category_ fromDescription(String description) {
        for (Category_ category : Category_.values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리: " + description);
    }

}
