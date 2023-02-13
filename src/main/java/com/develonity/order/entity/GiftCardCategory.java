package com.develonity.order.entity;

public enum GiftCardCategory {
    BAKERY("베이커리"),
    CAFE("카페"),
    CHICKEN("치킨"),
    BURGER("햄버거"),
    PIZZA("피자"),
    CONVENIENCE_STORE("편의점"),
    KOREAN_FOOD("한식"),
    CHINESE_FOOD("중식"),
    JAPANESE_FOOD("일식"),
    FAMILY_RESTAURANT("패밀리 레스토랑");

    private final String categoryName;

    GiftCardCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
