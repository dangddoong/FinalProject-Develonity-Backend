package com.develonity.order.service;

import com.develonity.common.aws.AwsS3Service;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.dto.PageDTO;
import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.GiftCardCategory;
import com.develonity.order.entity.GiftCardImage;
import com.develonity.order.repository.GiftCardImageRepository;
import com.develonity.order.repository.GiftCardRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GiftCardServiceImpl implements GiftCardService {

  private final GiftCardRepository giftCardRepository;

  private final GiftCardImageRepository giftCardImageRepository;

  private final AwsS3Service awsS3Service;

  @Override
  @Transactional
  public Long registerGiftCard(GiftCardRegister giftCardRegister, MultipartFile multipartFile)
      throws IOException {

    // 기프트카드 중복 확인
    if (giftCardRepository.existsByName(giftCardRegister.getName())) {
      throw new CustomException(ExceptionStatus.GIFT_CARD_IS_EXIST);
    }

    GiftCard giftCard = GiftCard.builder()
        .category(giftCardRegister.getCategory())
        .name(giftCardRegister.getName())
        .details(giftCardRegister.getDetails())
        .price(giftCardRegister.getPrice())
        .stockQuantity(giftCardRegister.getStockQuantity())
        .build();

    giftCardRepository.save(giftCard);
    if (multipartFile != null) {
      uploadOne(multipartFile, giftCard.getId());
      giftCard.addImagePath(getImagePath(giftCard.getId()));
    }

    return giftCard.getId();
  }


  @Override
  @Transactional(readOnly = true)
  public Page<GiftCardResponse> getGiftCardList(PageDTO pageDTO) {

    Page<GiftCard> giftCardList = giftCardRepository.findAll(pageDTO.toPageable());

    return new GiftCardResponse().toDtoList(giftCardList);
  }
  ///


  @Override // 카테고리 별 기프트카드 페이징 해서 가져오기
  @Transactional(readOnly = true)
  public Page<GiftCardResponse> getCategorizedGiftCardList(Long categoryId, PageDTO pageDTO) {
    GiftCardCategory category = GiftCardCategory.valueOfCategoryId(categoryId);
    Pageable pageable = pageDTO.toPageable();

    Page<GiftCard> giftCardList = giftCardRepository.findAllByCategory(category, pageable);

    if (giftCardList.isEmpty()) {
      throw new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST);
    }

    return new GiftCardResponse().toDtoList(giftCardList);
  }

  @Override
  @Transactional(readOnly = true)
  public GiftCardResponse getGiftCard(Long giftCardId) {
    GiftCard giftCard = giftCardRepository.findById(giftCardId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST));
    return new GiftCardResponse(giftCard);
  }

  @Override
  @Transactional
  public void updateGiftCard(Long id, GiftCardRegister giftCardRegister,
      MultipartFile multipartFile)
      throws IOException {

    GiftCard foundGiftCard = giftCardRepository.findById(id)
        .orElseThrow(() -> new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST));

    if (!foundGiftCard.getName().equals(giftCardRegister.getName())) {
      if (giftCardRepository.existsByName(giftCardRegister.getName())) {
        throw new CustomException(ExceptionStatus.GIFT_CARD_IS_EXIST);
      }
    }

    foundGiftCard.update(giftCardRegister.getCategory(), giftCardRegister.getName(),
        giftCardRegister.getDetails(), giftCardRegister.getImageUrl(), giftCardRegister.getPrice(),
        giftCardRegister.getStockQuantity());

    if (multipartFile != null) {
      deleteProfileImage(foundGiftCard.getId());
      uploadOne(multipartFile, foundGiftCard.getId());
    }
  }

  @Override
  @Transactional
  public Long deleteGiftCard(Long giftCardId) {

    if (!giftCardRepository.existsById(giftCardId)) {
      throw new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST);
    }
    giftCardRepository.deleteById(giftCardId);
    return giftCardId;
  }

  //이미지 단일 파일 업로드
  @Override
  @Transactional
  public void uploadOne(MultipartFile multipartFile, Long giftCardId) throws IOException {

    String uploadImagePath;
    String dir = "/order/giftCardImage";

    uploadImagePath = awsS3Service.uploadOne(multipartFile, dir);
    GiftCardImage giftCardImage = new GiftCardImage(uploadImagePath, giftCardId);
    giftCardImageRepository.save(giftCardImage);
  }

  //이미지 경로 반환
  @Override
  public String getImagePath(Long giftCardId) {
    GiftCardImage giftCardImage = giftCardImageRepository.findByGiftCardId(giftCardId)
        .orElseThrow(
            () -> new CustomException(ExceptionStatus.GIFT_CARD_IMAGE_IS_NOT_EXIST));

    return giftCardImage.getImagePath();
  }

  //이미지 파일 삭제
  @Override
  @Transactional
  public void deleteProfileImage(Long giftCardId) {

    String imagePath = getImagePath(giftCardId);

    awsS3Service.deleteFile(imagePath);
    giftCardImageRepository.deleteByGiftCardId(giftCardId);
  }

}
