package com.develonity.order.controller;

import com.develonity.common.security.users.UserDetailsImpl;
import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.service.GiftCardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN')") //어드민만 접근 가능
public class GiftCardController {
    private final GiftCardServiceImpl giftCardService;

    //기프트 카드 등록
    @PostMapping("/giftcards/register")
    public Long registerGiftCard(@RequestBody GiftCardRegister giftCardRegister) {
        return giftCardService.registerGiftCard(giftCardRegister);
    }

    //기프트 카드 수정
    @PutMapping("/giftcards/{giftCardId}")
    public Long updateGiftCard(@PathVariable Long giftCardId, @RequestBody GiftCardRegister giftCardRegister) {
        return giftCardService.updateGiftCard(giftCardId, giftCardRegister);
    }

    //기프트 카드 삭제
    //@DeleteMapping("/giftcards/{giftCardId}")

}
