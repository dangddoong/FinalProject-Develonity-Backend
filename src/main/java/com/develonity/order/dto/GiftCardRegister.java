package com.develonity.order.dto;

import lombok.Getter;

@Getter
public class GiftCardRegister {

    private String name;
    private String details;
    private String imageUrl;
    private int price;
    private int stockQuantity;
}
