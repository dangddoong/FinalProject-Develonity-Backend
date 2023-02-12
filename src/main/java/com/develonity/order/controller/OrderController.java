package com.develonity.order.controller;

import com.develonity.common.security.users.UserDetailsImpl;
import com.develonity.order.dto.OrderRequest;
import com.develonity.order.dto.OrderResponse;
import com.develonity.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderServiceImpl orderService;

    //기프트 카드 구매(주문하기)
    @PostMapping("/order")
    public Long orderGiftCard(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.order(orderRequest, userDetails.getUser().getId());
    }

    //유저 주문 내역 조회
    @GetMapping("/user/orders")
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getMyOrders(userDetails.getUser().getId());
    }

    //유저 주문 상세 내역 조회
    @GetMapping("/user/orders/{orderId}")
    public OrderResponse getMyOrder(@PathVariable Long orderId /*, @AuthenticationPrincipal UserDetailsImpl userDetails*/) {
        return orderService.getMyOrder(orderId/*, userDetails.getUser().getId()*/);
    }

}
