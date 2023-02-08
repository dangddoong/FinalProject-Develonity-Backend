package com.develonity.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GiftCard {

    @Id
    @GeneratedValue
    @Column(name = "GIFTCARD_ID")
    private Long id;

    private String name;
    private String details;
    private String imageUrl;
    private int price;
    private String giftCardOrderNumber;

}
