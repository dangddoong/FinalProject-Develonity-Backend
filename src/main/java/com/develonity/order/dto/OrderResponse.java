package com.develonity.order.dto;

import com.develonity.order.entity.Order;
import com.develonity.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter //주문 조회할 때 필요한 정보
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private int purchasePrice;
    private String phoneNumber;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

    @Builder
    public OrderResponse(Long orderId, Long userId, int purchasePrice, String phoneNumber, LocalDateTime orderDate, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.purchasePrice = purchasePrice;
        this.phoneNumber = phoneNumber;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    @Builder
    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.purchasePrice = order.getPurchasePrice();
        this.phoneNumber = order.getPhoneNumber();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
    }

    public Page<OrderResponse> toDtoList (Page<Order> orderList) {
        return orderList.map(m -> OrderResponse.builder()
                .orderId(m.getId())
                .userId(m.getUserId())
                .purchasePrice(m.getPurchasePrice())
                .phoneNumber(m.getPhoneNumber())
                .orderDate(m.getOrderDate())
                .orderStatus(m.getOrderStatus())
                .build());
    }
}
