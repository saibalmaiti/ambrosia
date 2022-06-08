package com.ambrosia.main.order.repository;

import com.ambrosia.main.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByRazorpayOrderId(String id);
    Optional<List<Order>> findAllByUserIdOrderByCreatedAt(Long userId);
}
