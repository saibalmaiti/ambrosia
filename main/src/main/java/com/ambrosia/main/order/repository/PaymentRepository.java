package com.ambrosia.main.order.repository;

import com.ambrosia.main.order.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentDetails, Long> {
}
