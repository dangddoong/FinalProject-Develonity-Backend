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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Long userId;
    private String realName;

    private String phoneNumber;
    private Long giftCardId;
    private int purchasePrice; //구매 가격 //이거는 기프트카드 레포에서 가져와서 값 넣어주기
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

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

    public void checkUser(Order order, Long userId) {
        if (order.getUserId() != userId) {
            throw new IllegalArgumentException("유저 불일치");
        }
    }

}
