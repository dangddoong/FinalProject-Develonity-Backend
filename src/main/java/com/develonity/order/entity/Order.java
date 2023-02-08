package com.develonity.order.entity;

import javax.persistence.*;
import java.util.Enumeration;

@Entity
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    private Long userId;
    private Long giftCardId;
    private int purchasePrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

}
