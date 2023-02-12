package com.develonity.order.dto;

import com.develonity.order.entity.GiftCard;
import lombok.Getter;

@Getter
public class GiftCardResponse {
    private String name;
    private String details;
    private String imageUrl;
    private int price;

    public GiftCardResponse(GiftCard giftCard) {
        this.name = giftCard.getName();
        this.details = giftCard.getDetails();
        this.imageUrl = giftCard.getImageUrl();
        this.price = giftCard.getPrice();
    }
}
