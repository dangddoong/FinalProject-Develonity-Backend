package com.develonity.order.repository;

import com.develonity.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllById(Long id);

    Optional<Order> findById(Long id);

}
