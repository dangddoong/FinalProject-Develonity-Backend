package com.develonity.order.entity;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    // 주문과 기프트카드는 1대1 관계
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String recipientName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Long giftCardId;
    @Column(nullable = false)
    private String giftCardName;
    @Column(nullable = false)
    private int purchasePrice;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public Order(String image, Long userId, String recipientName, String phoneNumber, Long giftCardId, String giftCardName, int purchasePrice) {
        this.image = image;
        this.userId = userId;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.giftCardId = giftCardId;
        this.giftCardName = giftCardName;
        this.purchasePrice = purchasePrice;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.PAYMENT_COMPLETED;
    }

    //==생성 메서드==//
    // 정적 팩터리 메서드를 사용함으로 써 이름을 가진 생성자처럼 객체를 생성할 수 있다.
    public static Order createOrder(String image, Long userId, String recipientName, String phoneNumber, Long giftCardId, String giftCardName, int purchasePrice) {
        Order order = Order.builder()
                .image(image)
                .userId(userId)
                .recipientName(recipientName)
                .phoneNumber(phoneNumber)
                .giftCardId(giftCardId)
                .giftCardName(giftCardName)
                .purchasePrice(purchasePrice)
                .build();

        //주문이 생성되면 User의 포인트 점수 차감

        return order;
    }

    public void checkUser(Order order, Long userId) {
        if (order.getUserId() != userId) {
            throw new CustomException(ExceptionStatus.AUTHENTICATED_EXCEPTION);
        }
    }

}
