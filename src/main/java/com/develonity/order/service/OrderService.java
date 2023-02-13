package com.develonity.order.service;

import com.develonity.order.dto.OrderRequest;
import com.develonity.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    Long order(OrderRequest orderRequest, Long userId);
    List<OrderResponse> getMyOrders(Long userId);
    OrderResponse getMyOrder(Long orderId, Long userId);
    int getTotalPurchasePrice(Long userId);

}
