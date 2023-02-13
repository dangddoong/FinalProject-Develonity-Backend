package com.develonity.order.entity;

import com.develonity.common.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class GiftCard {
    // 기프트 카드도 카테고리가 필요할까..?
    // 카카오톡 선물하기 처럼 카페, 아이스크림, 등등등 ?
    @Id
    @GeneratedValue
    @Column(name = "GIFTCARD_ID")
    private Long id;
    private String name;
    private String details;
    private String imageUrl;
    private int price;
    private String giftCardOrderNumber; // 이걸 어떻게 활용할 수 있을까..?
    private int stockQuantity; // 동시성 문제를 막기 위한 수량 설정(?)

    public GiftCard(String name, String details, String imageUrl, int price, int stockQuantity) {
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void update(String name, String details, String imageUrl, int price, int stockQuantity) {
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
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
     *  stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 부족합니다");
        }
        this.stockQuantity = restStock;
    }

}
