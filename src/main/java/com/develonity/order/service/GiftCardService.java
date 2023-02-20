package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.dto.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;



public interface GiftCardService {
    @Transactional
    Long registerGiftCard(GiftCardRegister giftCardRegister);
    @Transactional(readOnly = true)
    Page<GiftCardResponse> getGiftCardList(PageDTO pageDTO);
    @Transactional(readOnly = true)
    Page<GiftCardResponse> getCategorizedGiftCardList(Long categoryId, PageDTO pageDTO);
    @Transactional(readOnly = true)
    GiftCardResponse getGiftCard(Long giftCardId);
    @Transactional
    Long updateGiftCard(Long id, GiftCardRegister giftCardRegister);
    @Transactional
    Long deleteGiftCard(Long giftCardId);

}
