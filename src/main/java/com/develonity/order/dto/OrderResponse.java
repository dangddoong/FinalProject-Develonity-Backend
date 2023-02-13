package com.develonity.order.dto;

import com.develonity.order.entity.Order;
import com.develonity.order.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter //주문 조회할 때 필요한 정보
public class OrderResponse {
    private final Long orderId;
    private final Long userId;
    private final int purchasePrice;
    private final String phoneNumber;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.purchasePrice = order.getPurchasePrice();
        this.phoneNumber = order.getPhoneNumber();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
    }
}
