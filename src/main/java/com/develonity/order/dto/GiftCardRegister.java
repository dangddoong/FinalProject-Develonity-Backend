package com.develonity.order.dto;

import com.develonity.order.entity.GiftCardCategory;
import lombok.Getter;

@Getter
public class GiftCardRegister {

    private GiftCardCategory category;
    private String name;
    private String details;
    private String imageUrl;
    private int price;
    private int stockQuantity;
}
