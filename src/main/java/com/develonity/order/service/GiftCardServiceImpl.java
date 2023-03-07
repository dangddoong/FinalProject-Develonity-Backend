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
      String imagePath = uploadImage(multipartFile, giftCard.getId());
      giftCard.addImagePath(imagePath);
    }

    return giftCard.getId();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<GiftCardResponse> getGiftCardList(GiftCardCategory category, PageDTO pageDTO) {
    Pageable pageable = pageDTO.toPageable();
    Page<GiftCard> giftCardList;
    if (category.equals(GiftCardCategory.ALL)) {
      giftCardList = giftCardRepository.findAll(pageDTO.toPageable());
    } else {
      giftCardList = giftCardRepository.findAllByCategory(category, pageable);

      if (giftCardList.isEmpty()) {
        throw new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST);
      }
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
        giftCardRegister.getDetails(), giftCardRegister.getPrice(),
        giftCardRegister.getStockQuantity());

    if (multipartFile != null) {
      deleteGiftCardImage(id);
      String uploadImagePath = uploadImage(multipartFile, id);
      foundGiftCard.addImagePath(uploadImagePath);
    }

  }

  //기프트카드 삭제
  @Override
  @Transactional
  public Long deleteGiftCard(Long giftCardId) {

    if (!giftCardRepository.existsById(giftCardId)) {
      throw new CustomException(ExceptionStatus.GIFT_CARD_IS_NOT_EXIST);
    }

    deleteGiftCardImage(giftCardId);
    giftCardImageRepository.deleteByGiftCardId(giftCardId);
    giftCardRepository.deleteById(giftCardId);
    return giftCardId;
  }

  //이미지 단일 파일 업로드
  @Override
  public String uploadImage(MultipartFile multipartFile, Long giftCardId) throws IOException {

    String uploadImagePath;
    String dir = "/order/giftCardImage";

    uploadImagePath = awsS3Service.uploadOne(multipartFile, dir);
    GiftCardImage giftCardImage = new GiftCardImage(uploadImagePath, giftCardId);
    giftCardImageRepository.save(giftCardImage);
    return uploadImagePath;
  }

  //이미지 경로 반환
  @Override
  public String getImagePath(Long giftCardId) {
    GiftCardImage giftCardImage = giftCardImageRepository.findByGiftCardId(giftCardId)
        .orElse(new GiftCardImage(
            "https://pbs.twimg.com/profile_images/1121253455333474304/SzW8OOtq_400x400.jpg",
            giftCardId));
//            () -> new CustomException(ExceptionStatus.GIFT_CARD_IMAGE_IS_NOT_EXIST));
    return giftCardImage.getImagePath();
  }

  //이미지 파일 삭제
  @Override
  public void deleteGiftCardImage(Long id) {
    String imagePath = getImagePath(id);
    if (!imagePath.equals(
        "https://pbs.twimg.com/profile_images/1121253455333474304/SzW8OOtq_400x400.jpg")) {
      awsS3Service.deleteFile(imagePath);
      giftCardImageRepository.deleteByGiftCardId(id);
    }
  }
}
