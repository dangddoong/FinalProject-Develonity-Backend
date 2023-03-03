//package com.develonity.order.service;
//
//import com.develonity.common.aws.AwsS3Service;
//import com.develonity.order.dto.GiftCardRegister;
//import com.develonity.order.entity.GiftCard;
//import com.develonity.order.entity.GiftCardCategory;
//import com.develonity.order.repository.GiftCardImageRepository;
//import com.develonity.order.repository.GiftCardRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//
//public class GiftCardServiceImplTest {
//
//    @Autowired GiftCardRepository giftCardRepository;
//    @Autowired GiftCardServiceImpl giftCardService;
//    @Autowired GiftCardImageRepository giftCardImageRepository;
//    @Autowired AwsS3Service awsS3Service;
//
//    @Test
//    @DisplayName("기프트 카드 등록")
//    public void registerGiftCard() throws Exception {
//        // given
//        GiftCardRegister giftCard = new GiftCardRegister(GiftCardCategory.CAFE, "커피1", "커피1", "이미지", 4500, 100);
//        List<MultipartFile> multipartFiles = new ArrayList<>();
//
//        MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
//                "<<jpeg data>>".getBytes());
//
//        multipartFiles.add(multipartFile);
//
//        // when
//        giftCardService.registerGiftCard(giftCard)
//
//        // then
//    }
//
//}
