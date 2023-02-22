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
    private String image;
    private Long orderId;
    private String giftCardName;
    private Long userId;
    private String recipientName;
    private int purchasePrice;
    private String phoneNumber;
    private String orderDate;
    private OrderStatus orderStatus;

    @Builder
    public OrderResponse(String image, Long orderId, String giftCardName, Long userId, String recipientName, int purchasePrice, String phoneNumber, String orderDate, OrderStatus orderStatus) {
        this.image = image;
        this.orderId = orderId;
        this.giftCardName = giftCardName;
        this.userId = userId;
        this.recipientName = recipientName;
        this.purchasePrice = purchasePrice;
        this.phoneNumber = phoneNumber;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }


    @Builder
    public OrderResponse(Order order) {
        this.image = order.getImage();
        this.orderId = order.getId();
        this.giftCardName = order.getGiftCardName();
        this.userId = order.getUserId();
        this.recipientName = order.getRecipientName();
        this.purchasePrice = order.getPurchasePrice();
        this.phoneNumber = order.getPhoneNumber();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
    }

    public Page<OrderResponse> toDtoList (Page<Order> orderList) {
        return orderList.map(m -> OrderResponse.builder()
                .image(m.getImage())
                .orderId(m.getId())
                .giftCardName(m.getGiftCardName())
                .userId(m.getUserId())
                .recipientName(m.getRecipientName())
                .purchasePrice(m.getPurchasePrice())
                .phoneNumber(m.getPhoneNumber())
                .orderDate(m.getOrderDate())
                .orderStatus(m.getOrderStatus())
                .build());
    }
}
