package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.dto.PageDTO;
import com.develonity.order.entity.GiftCardCategory;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


public interface GiftCardService {

  @Transactional
  Long registerGiftCard(GiftCardRegister giftCardRegister, MultipartFile multipartFile)
      throws IOException;

  // 카테고리 별 기프트카드 페이징 해서 가져오기
  @Transactional(readOnly = true)
  Page<GiftCardResponse> getGiftCardList(GiftCardCategory category, PageDTO pageDTO);

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

  void updateGiftCardByPreSignedUrl(Long id, GiftCardRegister giftCardRegister, String imagePath);

  Long registerGiftCardByPreSignedUrl(GiftCardRegister giftCardRegister, String imagePath);
}
