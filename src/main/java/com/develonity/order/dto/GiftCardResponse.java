package com.develonity.order.dto;

import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.GiftCardCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class GiftCardResponse {

  private GiftCardCategory category;
  private Long id;
  private String name;
  private String details;
  private String imagePath;
  private int price;
  private int stockQuantity;
  private String lastModifiedDate;

  @Builder
  public GiftCardResponse(GiftCardCategory category, Long id, String name, String details,
      String imagePath, int price, int stockQuantity, String lastModifiedDate) {
    this.category = category;
    this.id = id;
    this.name = name;
    this.details = details;
    this.imagePath = imagePath;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.lastModifiedDate = lastModifiedDate;
  }

  @Builder
  public GiftCardResponse(GiftCard giftCard) {
    this.category = giftCard.getCategory();
    this.id = giftCard.getId();
    this.name = giftCard.getName();
    this.details = giftCard.getDetails();
    this.imagePath = giftCard.getImagePath();
    this.price = giftCard.getPrice();
    this.stockQuantity = giftCard.getStockQuantity();
    this.lastModifiedDate = giftCard.getLastModifiedDate();
  }


  public Page<GiftCardResponse> toDtoList(Page<GiftCard> giftCardList) {

    return giftCardList.map(m -> GiftCardResponse.builder()
        .category(m.getCategory())
        .id(m.getId())
        .name(m.getName())
        .details(m.getDetails())
        .imagePath(m.getImagePath())
        .price(m.getPrice())
        .stockQuantity(m.getStockQuantity())
        .lastModifiedDate(m.getLastModifiedDate())
        .build());
  }
}
