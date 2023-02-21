package com.develonity.order.dto;

import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.GiftCardCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class GiftCardResponse {
    private GiftCardCategory category;
    private Long id;
    private String name;
    private String details;
    private String imageUrl;
    private int price;
    private int stockQuantity;

    @Builder
    public GiftCardResponse(GiftCardCategory category, Long id, String name, String details, String imageUrl, int price, int stockQuantity) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @Builder
    public GiftCardResponse(GiftCard giftCard) {
        this.category = giftCard.getCategory();
        this.id = giftCard.getId();
        this.name = giftCard.getName();
        this.details = giftCard.getDetails();
        this.imageUrl = giftCard.getImageUrl();
        this.price = giftCard.getPrice();
        this.stockQuantity = giftCard.getStockQuantity();
    }


    public Page<GiftCardResponse> toDtoList (Page<GiftCard> giftCardList) {

        return giftCardList.map(m -> GiftCardResponse.builder()
                .category(m.getCategory())
                .id(m.getId())
                .name(m.getName())
                .details(m.getDetails())
                .imageUrl(m.getImageUrl())
                .price(m.getPrice())
                .stockQuantity(m.getStockQuantity())
                .build());
    }
}
