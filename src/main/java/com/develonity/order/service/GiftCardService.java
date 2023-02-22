package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.dto.PageDTO;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


public interface GiftCardService {

  @Transactional
  Long registerGiftCard(GiftCardRegister giftCardRegister, MultipartFile multipartFile)
      throws IOException;

  @Transactional(readOnly = true)
  Page<GiftCardResponse> getGiftCardList(PageDTO pageDTO);

  @Transactional(readOnly = true)
  Page<GiftCardResponse> getCategorizedGiftCardList(Long categoryId, PageDTO pageDTO);

  @Transactional(readOnly = true)
  GiftCardResponse getGiftCard(Long giftCardId);

  @Transactional
  void updateGiftCard(Long id, GiftCardRegister giftCardRegister, MultipartFile multipartFile)
      throws IOException;

  @Transactional
  Long deleteGiftCard(Long giftCardId);

  String uploadImage(MultipartFile multipartFile, Long giftCardId) throws IOException;

  void deleteGiftCardImage(Long giftCardId);

  String getImagePath(Long giftCardId);


}
