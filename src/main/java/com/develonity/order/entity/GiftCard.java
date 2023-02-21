package com.develonity.order.entity;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class GiftCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "GIFT_CARD_ID")
  private Long id;
  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private GiftCardCategory category;

  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String details;
  @Column
  @ColumnDefault("'https://pbs.twimg.com/profile_images/1121253455333474304/SzW8OOtq_400x400.jpg'")
  private String imagePath;
  @Column(nullable = false)
  private int price;
  @Column(nullable = false)
  private int stockQuantity; // 동시성 문제를 막기 위한 수량 설정(?)

  @Builder
  public GiftCard(GiftCardCategory category, String name, String details, String imagePath,
      int price, int stockQuantity) {
    this.category = category;
    this.name = name;
    this.details = details;
    this.imagePath = imagePath;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }

  public void update(GiftCardCategory category, String name, String details,
      int price, int stockQuantity) {
    this.category = category;
    this.name = name;
    this.details = details;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }

  /**
   * stock 증가
   */
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }

  /**
   * stock 감소
   */
  public void removeStock(int quantity) {
    int restStock = this.stockQuantity - quantity;
    if (restStock < 0) {
      throw new CustomException(ExceptionStatus.QUANTITY_IS_LACKING);
    }
    this.stockQuantity = restStock;
  }

  public void addImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

}
