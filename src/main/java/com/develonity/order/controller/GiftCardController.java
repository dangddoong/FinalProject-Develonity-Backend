package com.develonity.order.controller;

import com.develonity.common.aws.service.AwsPreSignedUrlService;
import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.dto.PageDTO;
import com.develonity.order.entity.GiftCardCategory;
import com.develonity.order.service.GiftCardServiceImpl;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN')") //어드민만 접근 가능, 기프트카드 조회, 단건 조회는 일반유저도 접근 가능
public class GiftCardController {

  private final GiftCardServiceImpl giftCardService;

  private final AwsPreSignedUrlService awsPreSignedUrlService;

  //기프트 카드 등록
  @PostMapping("/gift-cards")
  public Long registerGiftCard(
      @RequestPart("request") GiftCardRegister giftCardRegister,
      @RequestPart(required = false, name = "image") MultipartFile multipartFile)
      throws IOException {
//    if (multipartFile.isEmpty()) {
//      throw new CustomException(ExceptionStatus.IMAGE_IS_NOT_EXIST);
//    }
    return giftCardService.registerGiftCard(giftCardRegister, multipartFile);
  }


  //카테고리 별 기프트 카드 조회(페이징)
  @GetMapping("/gift-cards/category")
  public Page<GiftCardResponse> getGiftCardList(@RequestParam GiftCardCategory category,
      PageDTO pageDTO) {
    return giftCardService.getGiftCardList(category, pageDTO);
  }

  //기프트 카드 단건 조회
  @GetMapping("/gift-cards/{giftCardId}")
  public GiftCardResponse getGiftCard(@PathVariable Long giftCardId) {
    return giftCardService.getGiftCard(giftCardId);
  }

  //기프트 카드 수정
  @PutMapping("/gift-cards/{giftCardId}")
  public ResponseEntity<String> updateGiftCard(@PathVariable Long giftCardId,
      @RequestPart("request") GiftCardRegister giftCardRegister,
      @RequestPart(required = false, name = "image") MultipartFile multipartFile
  ) throws IOException {
    giftCardService.updateGiftCard(giftCardId, giftCardRegister, multipartFile);
    return new ResponseEntity<>("기프트 카드가 수정되었습니다.", HttpStatus.OK);
  }

  //preSignedURL 기프트카드 수정
  @PutMapping("/gift-cards/{giftCardId}/preSignedGiftCard/")
  public ResponseEntity<String> updateGiftCardByPreSignedUrl(@PathVariable Long giftCardId,
      @RequestBody GiftCardRegister giftCardRegister
  ) throws IOException {

    String imagePath = awsPreSignedUrlService.findByName();
    giftCardService.updateGiftCardByPreSignedUrl(giftCardId, giftCardRegister, imagePath);
    return new ResponseEntity<>("기프트 카드가 수정되었습니다.", HttpStatus.OK);
  }

  //preSignedURL 기프트카드 생성
  @PostMapping("/gift-cards/preSignedGiftCard")
  public ResponseEntity<String> registerGiftCardByPreSignedUrl(
      @RequestBody GiftCardRegister giftCardRegister
  ) throws IOException {

    String imagePath = awsPreSignedUrlService.findByName();
    giftCardService.registerGiftCardByPreSignedUrl(giftCardRegister, imagePath);
    return new ResponseEntity<>("기프트 카드가 생성되었습니다.", HttpStatus.OK);
  }

  //기프트 카드 삭제
  @DeleteMapping("/gift-cards/{giftCardId}")
  public Long deleteGiftCard(@PathVariable Long giftCardId) {
    return giftCardService.deleteGiftCard(giftCardId);
  }
}
