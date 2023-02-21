package com.develonity.order.entity;

import com.develonity.user.entity.TimeStamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class GiftCardImage extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @JoinColumn(name = "GIFT_CARD_ID")
  private Long giftCardId;
  @Column(nullable = false)
  private String imagePath;


  public GiftCardImage(String filePath, Long giftCardId) {
    this.imagePath = filePath;
    this.giftCardId = giftCardId;
  }

}
