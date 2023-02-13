package com.develonity.order.repository;

import com.develonity.order.entity.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByName(String name);

    boolean existsByName(String name);
}
