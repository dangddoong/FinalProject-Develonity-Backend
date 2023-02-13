package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.entity.GiftCard;
import org.springframework.transaction.annotation.Transactional;


public interface GiftCardService {
    Long registerGiftCard(GiftCardRegister giftCardRegister);
    Long updateGiftCard(Long id, GiftCardRegister giftCardRegister);

}
