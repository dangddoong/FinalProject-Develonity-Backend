package com.develonity.order.entity;

import java.util.Arrays;

public enum GiftCardCategory {
    CAFE(1L),
    CHICKEN(2L),
    CONVENIENCE_STORE(3L);

    private final Long categoryId;

    GiftCardCategory(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public static GiftCardCategory valueOfCategoryId (Long categoryId) {
        return Arrays.stream(values())
                .filter(value -> value.categoryId.equals(categoryId))
                .findAny()
                .orElse(null);
    }
}
