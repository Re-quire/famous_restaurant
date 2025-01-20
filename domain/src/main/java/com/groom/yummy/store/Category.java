package com.groom.yummy.store;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Category {
    // TODO : apiCode 수정
    KOREAN("001","한식"),
    WESTERN("002","양식"),
    Chinese("003","중식"),
    Japanese("004","일식"),
    Dessert("005","디저트");

    private final String apiCode;
    private final String description;

    private static final Map<String, Category> API_CODE_TO_CATEGORY = new HashMap<>();

    static {
        for(Category category: values()){
            API_CODE_TO_CATEGORY.put(category.getApiCode(), category);
        }
    }

    Category(String apiCode, String description) {
        this.apiCode = apiCode;
        this.description = description;
    }

    public static Category fromApiCode(String apiCode) {
        return API_CODE_TO_CATEGORY.get(apiCode);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) ", apiCode, description);
    }

    public static boolean isValidCategory(Category category) {
        return API_CODE_TO_CATEGORY.containsValue(category);
    }
}
