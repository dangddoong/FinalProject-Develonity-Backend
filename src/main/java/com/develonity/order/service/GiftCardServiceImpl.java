package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.entity.GiftCard;
import com.develonity.order.repository.GiftCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCardServiceImpl implements GiftCardService{

    private final GiftCardRepository giftCardRepository;

    @Override
    @Transactional
    public Long registerGiftCard(GiftCardRegister giftCardRegister) {

        // 기프트카드 중복 확인
        Optional<GiftCard> found = giftCardRepository.findByName(giftCardRegister.getName());
        if (found.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 기프트카드 입니다.");
        }

        GiftCard giftCard = new GiftCard(giftCardRegister.getName(), giftCardRegister.getDetails(), giftCardRegister.getImageUrl(), giftCardRegister.getPrice(), giftCardRegister.getStockQuantity());
        giftCardRepository.save(giftCard);

        return giftCard.getId();
    }

    @Override
    @Transactional
    public Long updateGiftCard(Long id, GiftCardRegister giftCardRegister) {

        GiftCard foundGiftCard = giftCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기프트카드 입니다. 등록이 필요합니다."));

        foundGiftCard.update(giftCardRegister.getName(), giftCardRegister.getDetails(), giftCardRegister.getImageUrl(), giftCardRegister.getPrice(), giftCardRegister.getStockQuantity());

        return foundGiftCard.getId();
    }
}
