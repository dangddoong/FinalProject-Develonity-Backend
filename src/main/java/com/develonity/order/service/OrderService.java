package com.develonity.order.service;

import com.develonity.order.dto.OrderRequest;
import com.develonity.order.dto.OrderResponse;
import com.develonity.order.dto.PageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Long order(OrderRequest orderRequest, Long userId);
    List<OrderResponse> getMyOrders(Long userId);

    Page<OrderResponse> getMyOrdersByPaging(Long userId, PageDTO pageDTO);

    OrderResponse getMyOrder(Long orderId, Long userId);
    int getTotalPurchasePrice(Long userId);

}
