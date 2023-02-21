package com.develonity.order.entity;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GIFTCARD_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private GiftCardCategory category;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String details;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int stockQuantity; // 동시성 문제를 막기 위한 수량 설정(?)

    @Builder
    public GiftCard(GiftCardCategory category, String name, String details, String imageUrl, int price, int stockQuantity) {
        this.category = category;
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void update(GiftCardCategory category, String name, String details, String imageUrl, int price, int stockQuantity) {
        this.category = category;
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
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new CustomException(ExceptionStatus.QUANTITY_IS_LACKING);
        }
        this.stockQuantity = restStock;
    }

}
