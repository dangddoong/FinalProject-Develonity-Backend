package com.develonity.order.repository;

import com.develonity.order.entity.GiftCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    boolean existsByName(String name);

    Page<GiftCard> findAll(Pageable pageable);
}
