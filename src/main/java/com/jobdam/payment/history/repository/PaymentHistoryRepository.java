package com.jobdam.payment.history.repository;

import com.jobdam.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Integer userId);
}
