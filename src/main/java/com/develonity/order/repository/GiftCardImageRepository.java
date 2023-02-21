package com.develonity.order.repository;

import com.develonity.order.entity.GiftCardImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftCardImageRepository extends JpaRepository<GiftCardImage, Long> {

  Optional<GiftCardImage> findByGiftCardId(Long giftCardId);

//  ProfileImage findProfileImageByUserId(Long userId);

  void deleteByGiftCardId(Long giftCardId);

  boolean existsByGiftCardId(Long giftCardId);
}
