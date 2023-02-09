package com.develonity.order.service;

import com.develonity.order.dto.OrderRequest;
import com.develonity.order.dto.OrderResponse;
import com.develonity.order.entity.Order;
import com.develonity.order.repository.GiftCardRepository;
import com.develonity.order.repository.OrderRepository;
import com.develonity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCardRepository giftCardRepository;

    /**
     * 주문 하기
     */
    @Transactional
    public Long order(OrderRequest orderRequest, Long userId) {

        String realName = orderRequest.getRealName();
        String phoneNumber = orderRequest.getPhoneNumber();
        Long giftCardId = orderRequest.getGiftCardId();
        int purchasePrice = orderRequest.getPurchasePrice();

        //주문 생성
        Order order = Order.createOrder(userId, realName, phoneNumber, giftCardId, purchasePrice);

        //주문 저장
        orderRepository.save(order);

        //여기서 포인트를 차감하는 메서드가 필요한지..?

        return order.getId();
    }

    /**
     * 주문 취소
     */


    /**
     * 주문 검색
     */


    /**
     * 주문 내역 조회
     */
    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findAllById(userId);
        List<OrderResponse> myOrders = orders.stream().map(OrderResponse::new).collect(Collectors.toList());
        return myOrders;
    }

    /**
     * 주문 내역 상세 조회
     */
    public OrderResponse getMyOrder(Long orderId/*, Long userId*/) {
        Order order = orderRepository.findById(orderId).get();
        return new OrderResponse(order);
    }
}
