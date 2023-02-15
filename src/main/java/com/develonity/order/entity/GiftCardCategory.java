package com.develonity.order.entity;

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
}
