package com.develonity.order.service;

import com.develonity.order.dto.GiftCardRegister;
import com.develonity.order.dto.GiftCardResponse;
import com.develonity.order.entity.GiftCard;
import com.develonity.order.repository.GiftCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCardServiceImpl implements GiftCardService{

    private final GiftCardRepository giftCardRepository;

    @Override
    @Transactional
    public Long registerGiftCard(GiftCardRegister giftCardRegister) {

        // 기프트카드 중복 확인
        boolean isExistGiftCard = giftCardRepository.existsByName(giftCardRegister.getName());
        if (isExistGiftCard) {
            throw new IllegalArgumentException("이미 등록된 기프트카드 입니다.");
        }

        GiftCard giftCard = new GiftCard(giftCardRegister.getCategory(), giftCardRegister.getName(), giftCardRegister.getDetails(), giftCardRegister.getImageUrl(), giftCardRegister.getPrice(), giftCardRegister.getStockQuantity());
        giftCardRepository.save(giftCard);

        return giftCard.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCardResponse> retrieveGiftCardList() {
        List<GiftCard> giftCardList = giftCardRepository.findAll();

        if(giftCardList.isEmpty())
            throw new IllegalArgumentException("기프트카드가 존재하지 않습니다.");

        return giftCardList.stream().map(GiftCardResponse::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCardResponse retrieveGiftCard(Long giftCardId) {
        GiftCard giftCard = giftCardRepository.findById(giftCardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기프트 카드 입니다."));
        return new GiftCardResponse(giftCard);
    }

    @Override
    @Transactional
    public Long updateGiftCard(Long id, GiftCardRegister giftCardRegister) {

        GiftCard foundGiftCard = giftCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기프트카드 입니다. 등록이 필요합니다."));

        if(foundGiftCard.getName().equals(giftCardRegister.getName())){
            throw new IllegalArgumentException("동일한 이름의 기프트카드가 이미 존재합니다");
        }

        foundGiftCard.update(giftCardRegister.getName(), giftCardRegister.getDetails(), giftCardRegister.getImageUrl(), giftCardRegister.getPrice(), giftCardRegister.getStockQuantity());

        return foundGiftCard.getId();
    }

    @Override
    @Transactional
    public Long deleteGiftCard(Long giftCardId) {
        boolean isExistGiftCard = giftCardRepository.existsById(giftCardId);
        if(!isExistGiftCard) {
            throw new IllegalArgumentException("존재하지 않는 기프트카드 입니다.");
        }
        giftCardRepository.deleteById(giftCardId);
        return giftCardId;
    }

}
