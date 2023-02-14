package com.develonity.order.repository;

import com.develonity.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId); //JPA의 Projection 사용하기

    Optional<Order> findById(Long orderId);

    Page<Order> findAllByUserId(Long userId, Pageable pageable);

}
