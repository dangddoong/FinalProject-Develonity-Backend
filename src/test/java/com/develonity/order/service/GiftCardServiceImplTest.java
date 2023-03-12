package com.develonity.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.common.aws.service.AwsS3Service;
import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.GiftCardCategory;
import com.develonity.order.entity.GiftCardImage;
import com.develonity.order.repository.GiftCardImageRepository;
import com.develonity.order.repository.GiftCardRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;


@SpringBootTest
public class GiftCardServiceImplTest {

  @Autowired
  GiftCardRepository giftCardRepository;
  @Autowired
  GiftCardServiceImpl giftCardService;
  @Autowired
  GiftCardImageRepository giftCardImageRepository;
  @Autowired
  AwsS3Service awsS3Service;

  @BeforeEach
  void allDeleteBefore() {
    giftCardRepository.deleteAll();
    giftCardImageRepository.deleteAll();
  }

  @AfterEach
  void allDeleteAfter() {
    giftCardRepository.deleteAll();
    giftCardImageRepository.deleteAll();
  }

  @Test
  @DisplayName("기프트 카드 등록")
  void registerGiftCard() throws Exception {

    // given
    GiftCardRegister giftCardRegister = new GiftCardRegister(GiftCardCategory.CAFE, "커피1", "커피1",
        "이미지1", 4500, 100);

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    // when
    Long giftCardId = giftCardService.registerGiftCard(giftCardRegister, multipartFile);
    Optional<GiftCard> foundGiftCard = giftCardRepository.findById(giftCardId);
    Optional<GiftCardImage> foundGiftCardImage = giftCardImageRepository.findByGiftCardId(
        giftCardId);
    String imagePath = foundGiftCardImage.get().getImagePath();

    // then
    assertThat(imagePath).isEqualTo(foundGiftCard.get().getImagePath());
    assertThat(giftCardId).isEqualTo(foundGiftCardImage.get().getGiftCardId());
    assertThat(foundGiftCard.get().getCategory()).isEqualTo(giftCardRegister.getCategory());
    assertThat(foundGiftCard.get().getName()).isEqualTo(giftCardRegister.getName());
    assertThat(foundGiftCard.get().getDetails()).isEqualTo(giftCardRegister.getDetails());
    assertThat(foundGiftCard.get().getPrice()).isEqualTo(giftCardRegister.getPrice());
    assertThat(foundGiftCard.get().getStockQuantity()).isEqualTo(
        giftCardRegister.getStockQuantity());
    assertThat(giftCardImageRepository.existsByGiftCardId(giftCardId)).isTrue();

  }

  @Test
  @DisplayName("기프트 카드 전체 조회 & 단건 조회")
  void getGiftCardList() throws Exception {

    // given
    GiftCardRegister giftCardRegister1 = new GiftCardRegister(GiftCardCategory.CAFE, "커피1", "커피1",
        "이미지1", 4500, 100);

    MockMultipartFile multipartFile1 = new MockMultipartFile("files", "imageFile1.jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());

    Long giftCardId1 = giftCardService.registerGiftCard(giftCardRegister1, multipartFile1);

    GiftCardRegister giftCardRegister2 = new GiftCardRegister(GiftCardCategory.CAFE, "커피2", "커피2",
        "이미지2", 5000, 50);

    MockMultipartFile multipartFile2 = new MockMultipartFile("files", "imageFile2.jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());

    Long giftCardId2 = giftCardService.registerGiftCard(giftCardRegister2, multipartFile2);

    // when
    int size = giftCardRepository.findAll().size();
    Optional<GiftCard> foundGiftCard1 = giftCardRepository.findById(giftCardId1);
    Optional<GiftCard> foundGiftCard2 = giftCardRepository.findById(giftCardId2);
    Optional<GiftCardImage> foundGiftCardImage1 = giftCardImageRepository.findByGiftCardId(
        giftCardId1);
    Optional<GiftCardImage> foundGiftCardImage2 = giftCardImageRepository.findByGiftCardId(
        giftCardId2);

    // then
    assertThat(size).isEqualTo(2);

    assertThat(foundGiftCard1.get().getCategory()).isEqualTo(giftCardRegister1.getCategory());
    assertThat(foundGiftCard1.get().getName()).isEqualTo(giftCardRegister1.getName());
    assertThat(foundGiftCard1.get().getDetails()).isEqualTo(giftCardRegister1.getDetails());
    assertThat(foundGiftCard1.get().getImagePath()).isEqualTo(
        foundGiftCardImage1.get().getImagePath());
    assertThat(foundGiftCard1.get().getPrice()).isEqualTo(giftCardRegister1.getPrice());
    assertThat(foundGiftCard1.get().getStockQuantity()).isEqualTo(
        giftCardRegister1.getStockQuantity());

    assertThat(foundGiftCard2.get().getCategory()).isEqualTo(giftCardRegister2.getCategory());
    assertThat(foundGiftCard2.get().getName()).isEqualTo(giftCardRegister2.getName());
    assertThat(foundGiftCard2.get().getDetails()).isEqualTo(giftCardRegister2.getDetails());
    assertThat(foundGiftCard2.get().getImagePath()).isEqualTo(
        foundGiftCardImage2.get().getImagePath());
    assertThat(foundGiftCard2.get().getPrice()).isEqualTo(giftCardRegister2.getPrice());
    assertThat(foundGiftCard2.get().getStockQuantity()).isEqualTo(
        giftCardRegister2.getStockQuantity());
  }


  @Test
  @DisplayName("기프트 카드 수정")
  void updateGiftCard() throws Exception {

    // given
    GiftCardRegister giftCardRegister1 = new GiftCardRegister(GiftCardCategory.CAFE, "커피1", "커피1",
        "이미지1", 4500, 100);

    MockMultipartFile multipartFile1 = new MockMultipartFile("files", "imageFile1.jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());

    Long giftCardId = giftCardService.registerGiftCard(giftCardRegister1, multipartFile1);

    GiftCardRegister giftCardRegister2 = new GiftCardRegister(GiftCardCategory.CAFE, "커피2", "커피2",
        "이미지2", 5000, 50);

    MockMultipartFile multipartFile2 = new MockMultipartFile("files", "imageFile2.jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());

    // when
    giftCardService.updateGiftCard(giftCardId, giftCardRegister2, multipartFile2);
    Optional<GiftCard> foundGiftCard = giftCardRepository.findById(giftCardId);
    Optional<GiftCardImage> foundGiftCardImage = giftCardImageRepository.findByGiftCardId(
        giftCardId);
    String imagePath = foundGiftCardImage.get().getImagePath();

    // then
    assertThat(imagePath).isEqualTo(foundGiftCard.get().getImagePath());
    assertThat(giftCardId).isEqualTo(foundGiftCardImage.get().getGiftCardId());
    assertThat(foundGiftCard.get().getCategory()).isEqualTo(giftCardRegister2.getCategory());
    assertThat(foundGiftCard.get().getName()).isEqualTo(giftCardRegister2.getName());
    assertThat(foundGiftCard.get().getDetails()).isEqualTo(giftCardRegister2.getDetails());
    assertThat(foundGiftCard.get().getPrice()).isEqualTo(giftCardRegister2.getPrice());
    assertThat(foundGiftCard.get().getStockQuantity()).isEqualTo(
        giftCardRegister2.getStockQuantity());

  }

  @Test
  @DisplayName("기프트 카드 삭제")
  void deleteGiftCard() throws Exception {

    // given
    GiftCardRegister giftCardRegister = new GiftCardRegister(GiftCardCategory.CAFE, "커피1", "커피1",
        "이미지", 4500, 100);

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    Long giftCardId = giftCardService.registerGiftCard(giftCardRegister, multipartFile);

    // when
    Long deletedGiftCardId = giftCardService.deleteGiftCard(giftCardId);

    int giftCardRepositorySize = giftCardRepository.findAll().size();
    int giftCardImageRepositorySize = giftCardRepository.findAll().size();

    // then
    assertThat(giftCardRepositorySize).isEqualTo(0);
    assertThat(giftCardImageRepositorySize).isEqualTo(0);
    assertThat(giftCardId).isEqualTo(deletedGiftCardId);
    assertThat(giftCardRepository.existsById(giftCardId)).isFalse();
    assertThat(giftCardImageRepository.existsByGiftCardId(giftCardId)).isFalse();
  }

}