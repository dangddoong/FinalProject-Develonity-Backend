package com.develonity.order.controller;

import com.develonity.common.security.users.UserDetailsImpl;
import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.service.GiftCardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN')") //어드민만 접근 가능
public class GiftCardController {
    private final GiftCardServiceImpl giftCardService;

    //기프트 카드 등록
    @PostMapping("/gift-cards/register")
    public Long registerGiftCard(@RequestBody GiftCardRegister giftCardRegister) {
        return giftCardService.registerGiftCard(giftCardRegister);
    }

    //기프트 카드 전체 조회
    @GetMapping("/gift-cards")
    public List<GiftCardResponse> retrieveGiftCardList() {
        return giftCardService.retrieveGiftCardList();
    }

    //기프트 카드 단건 조회
    @GetMapping("/gift-cards/{giftCardId}")
    public GiftCardResponse retrieveGiftCard(@PathVariable Long giftCardId) {
        return giftCardService.retrieveGiftCard(giftCardId);
    }

    //기프트 카드 수정
    @PutMapping("/gift-cards/{giftCardId}")
    public Long updateGiftCard(@PathVariable Long giftCardId, @RequestBody GiftCardRegister giftCardRegister) {
        return giftCardService.updateGiftCard(giftCardId, giftCardRegister);
    }

    //기프트 카드 삭제
    @DeleteMapping("/gift-cards/{giftCardId}")
    public Long deleteGiftCard(@PathVariable Long giftCardId) {
        return giftCardService.deleteGiftCard(giftCardId);
    }
}
