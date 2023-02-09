package com.develonity.order.entity;

import lombok.AccessLevel;
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
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private Long userId;
    private String realName;

    private String phoneNumber;
    private Long giftCardId;
    private int purchasePrice; //구매 가격
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus; //결제 완료가 되어야 주문이 생기는 거니까 초기 status 는 무조건 PAYMENT_COMPLETED가 아닐까..

    public Order(Long userId, String realName, String phoneNumber, Long giftCardId, int purchasePrice) {
        this.userId = userId;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        this.giftCardId = giftCardId;
        this.purchasePrice = purchasePrice;
        this.orderDate = LocalDateTime.now();
        this.orderstatus = OrderStatus.PAYMENT_COMPLETED;
    }

    //==생성 메서드==//
    // 정적 팩터리 메서드를 사용함으로 써 이름을 가진 생성자처럼 객체를 생성할 수 있다.
    public static Order createOrder(Long userId, String realName, String phoneNumber, Long giftCardId, int purchasePrice){
        Order order = new Order(userId, realName, phoneNumber, giftCardId, purchasePrice);

        //주문이 생성되면 User의 포인트 점수 차감

        return order;
    }

}
