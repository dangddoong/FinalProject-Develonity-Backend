package com.develonity.order.entity;

public enum OrderStatus {
    // 결제중, 결제완료.... 결제 취소가 존재해야하나..?
    // 발송실패 발송완료
    PAYMENT_COMPLETED,
    SEND_FAIL, SEND_SUCCESS
}
