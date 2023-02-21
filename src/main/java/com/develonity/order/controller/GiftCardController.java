package com.develonity.order.controller;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.dto.PageDTO;
import com.develonity.order.service.GiftCardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN')") //어드민만 접근 가능, 기프트카드 조회, 단건 조회는 일반유저도 접근 가능
public class GiftCardController {
    private final GiftCardServiceImpl giftCardService;

    //기프트 카드 등록
    @PostMapping("/gift-cards")
    public Long registerGiftCard(@RequestBody GiftCardRegister giftCardRegister) {
        return giftCardService.registerGiftCard(giftCardRegister);
    }

    //기프트 카드 전체 조회(페이징)
    @GetMapping("/gift-cards")
    public Page<GiftCardResponse> getGiftCardList(PageDTO pageDTO) {
        return giftCardService.getGiftCardList(pageDTO);
    }

    //카테고리 별 기프트 카드 조회(페이징)
    @GetMapping("/gift-cards/category")
    public Page<GiftCardResponse> getCategorizedGiftCardList(@RequestParam Long categoryId, PageDTO pageDTO) {
        return giftCardService.getCategorizedGiftCardList(categoryId, pageDTO);
    }

    //기프트 카드 단건 조회
    @GetMapping("/gift-cards/{giftCardId}")
    public GiftCardResponse getGiftCard(@PathVariable Long giftCardId) {
        return giftCardService.getGiftCard(giftCardId);
    }

    //기프트 카드 수정
    @PutMapping("/gift-cards/{giftCardId}")
    public GiftCardResponse updateGiftCard(@PathVariable Long giftCardId, @RequestBody GiftCardRegister giftCardRegister) {
        return giftCardService.updateGiftCard(giftCardId, giftCardRegister);
    }

    //기프트 카드 삭제
    @DeleteMapping("/gift-cards/{giftCardId}")
    public Long deleteGiftCard(@PathVariable Long giftCardId) {
        return giftCardService.deleteGiftCard(giftCardId);
    }
}
