package com.develonity.order.repository;

import com.develonity.order.entity.GiftCard;
import com.develonity.order.entity.GiftCardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    boolean existsByName(String name);

    Page<GiftCard> findAll(Pageable pageable);

    List<GiftCard> findAllByCategory(GiftCardCategory  category);
}
