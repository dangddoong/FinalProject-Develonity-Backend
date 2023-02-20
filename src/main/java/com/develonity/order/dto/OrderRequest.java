package com.develonity.order.dto;

import com.develonity.order.entity.Order;
import lombok.Getter;

@Getter //주문할 때 필요한 정보
public class OrderRequest {
    private String realName;
    private String phoneNumber;
    private Long giftCardId;
}
