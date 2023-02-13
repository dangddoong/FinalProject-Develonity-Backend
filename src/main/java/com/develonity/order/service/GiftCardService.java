package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.entity.GiftCard;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface GiftCardService {
    @Transactional
    Long registerGiftCard(GiftCardRegister giftCardRegister);

    @Transactional(readOnly = true)
    List<GiftCardResponse> retrieveGiftCardList();

    @Transactional(readOnly = true)
    GiftCardResponse retrieveGiftCard(Long giftCardId);

    @Transactional
    Long updateGiftCard(Long id, GiftCardRegister giftCardRegister);

    @Transactional
    Long deleteGiftCard(Long giftCardId);
}
