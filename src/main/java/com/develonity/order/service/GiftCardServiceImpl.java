package com.develonity.order.service;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
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
        if (giftCardRepository.existsByName(giftCardRegister.getName())) {
            throw new CustomException(ExceptionStatus.GIFTCARD_IS_EXIST);
        }

        GiftCard giftCard = GiftCard.builder()
                .category(giftCardRegister.getCategory())
                .name(giftCardRegister.getName())
                .details(giftCardRegister.getDetails())
                .imageUrl(giftCardRegister.getImageUrl())
                .price(giftCardRegister.getPrice())
                .stockQuantity(giftCardRegister.getStockQuantity())
                .build();

        giftCardRepository.save(giftCard);

        return giftCard.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCardResponse> retrieveGiftCardList() {
        List<GiftCard> giftCardList = giftCardRepository.findAll();

        if(giftCardList.isEmpty())
            throw new CustomException(ExceptionStatus.GIFTCARD_IS_NOT_EXIST);

        return giftCardList.stream().map(GiftCardResponse::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCardResponse retrieveGiftCard(Long giftCardId) {
        GiftCard giftCard = giftCardRepository.findById(giftCardId).orElseThrow(() -> new CustomException(ExceptionStatus.GIFTCARD_IS_NOT_EXIST));
        return new GiftCardResponse(giftCard);
    }

    @Override
    @Transactional
    public Long updateGiftCard(Long id, GiftCardRegister giftCardRegister) {

        GiftCard foundGiftCard = giftCardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionStatus.GIFTCARD_IS_NOT_EXIST));

        if(foundGiftCard.getName().equals(giftCardRegister.getName())){
            throw new CustomException(ExceptionStatus.GIFTCARD_IS_EXIST);
        }

        foundGiftCard.update(giftCardRegister.getName(), giftCardRegister.getDetails(), giftCardRegister.getImageUrl(), giftCardRegister.getPrice(), giftCardRegister.getStockQuantity());

        return foundGiftCard.getId();
    }

    @Override
    @Transactional
    public Long deleteGiftCard(Long giftCardId) {

        if(!giftCardRepository.existsById(giftCardId)) {
            throw new CustomException(ExceptionStatus.GIFTCARD_IS_NOT_EXIST);
        }
        giftCardRepository.deleteById(giftCardId);
        return giftCardId;
    }

}
