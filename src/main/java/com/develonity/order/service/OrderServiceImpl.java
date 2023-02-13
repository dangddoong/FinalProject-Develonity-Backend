package com.develonity.order.service;

import com.develonity.order.dto.OrderRequest;
import com.develonity.order.dto.OrderResponse;
import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.Order;
import com.develonity.order.repository.GiftCardRepository;
import com.develonity.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final GiftCardRepository giftCardRepository;

    /**
     * 주문 하기
     */
    @Override
    @Transactional
    public Long order(OrderRequest orderRequest, Long userId) {

        String realName = orderRequest.getRealName();
        String phoneNumber = orderRequest.getPhoneNumber();
        Long giftCardId = orderRequest.getGiftCardId();

        GiftCard giftCard = giftCardRepository.findById(giftCardId).orElseThrow(() -> new IllegalArgumentException("해당 기프트카드가 존재하지 않습니다."));
        giftCard.removeStock(1);
        //여기서 포인트를 차감하는 메서드가 필요한지..?

        //주문 생성
        Order order = Order.createOrder(userId, realName, phoneNumber, giftCardId, giftCard.getPrice());

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 내역 조회
     */
    @Override
    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);

        if(orders == null || orders.isEmpty())
            throw new IllegalArgumentException("주문 내역이 존재하지 않습니다.");

        return orders.stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    /**
     * 주문 내역 상세 조회
     */
    @Override
    public OrderResponse getMyOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문 내역이 존재하지 않습니다."));
        order.checkUser(order, userId);
        return new OrderResponse(order);
    }

    /**
     * 주문 내역 총 금액 계산
     */
    @Override
    public int getTotalPurchasePrice(Long userId) {
        int totalPurchasePrice = 0;
        List<Order> orders = orderRepository.findAllByUserId(userId);
        for(Order order : orders) {
            totalPurchasePrice += order.getPurchasePrice();
        }
        return totalPurchasePrice;
    }


    /**
     * 주문 취소
     */


    /**
     * 주문 검색
     */

}
