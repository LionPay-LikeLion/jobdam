package com.jobdam.payment.repository;

import com.jobdam.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Integer userId);
    Optional<Payment> findByMerchantUid(String merchantUid);
}