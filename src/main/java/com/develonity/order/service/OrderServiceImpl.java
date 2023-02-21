package com.develonity.order.service;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.order.dto.OrderRequest;
import com.develonity.order.dto.OrderResponse;
import com.develonity.order.dto.PageDTO;
import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.Order;
import com.develonity.order.repository.GiftCardRepository;
import com.develonity.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final GiftCardRepository giftCardRepository;

  /**
   * 주문 하기
   */
  @Override
  @Transactional
  public Long order(OrderRequest orderRequest, Long userId) {

    String recipientName = orderRequest.getRecipientName();
    String phoneNumber = orderRequest.getPhoneNumber();
    Long giftCardId = orderRequest.getGiftCardId();

    GiftCard giftCard = giftCardRepository.findById(giftCardId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST));
    giftCard.removeStock(1);
    //여기서 포인트를 차감하는 메서드가 필요한지..?
    String image = giftCard.getImagePath();
    String giftCardName = giftCard.getName();

    //주문 생성
    Order order = Order.builder()
        .image(image)
        .userId(userId)
        .recipientName(recipientName)
        .phoneNumber(phoneNumber)
        .giftCardId(giftCardId)
        .giftCardName(giftCardName)
        .purchasePrice(giftCard.getPrice())
        .build();

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

    if (orders == null || orders.isEmpty()) {
      throw new CustomException(ExceptionStatus.ORDER_IS_NOT_EXIST);
    }

    return orders.stream().map(OrderResponse::new).collect(Collectors.toList());
  }

  @Override
  public Page<OrderResponse> getMyOrdersByPaging(Long userId, PageDTO pageDTO) {

    Page<Order> orderList = orderRepository.findAllByUserId(userId, pageDTO.toPageable());

    return new OrderResponse().toDtoList(orderList);
  }

  /**
   * 주문 내역 상세 조회
   */
  @Override
  public OrderResponse getMyOrder(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.ORDER_IS_NOT_EXIST));
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
    for (Order order : orders) {
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
