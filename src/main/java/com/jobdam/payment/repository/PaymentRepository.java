package com.jobdam.payment.repository;

import com.jobdam.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    // imp_uid로 중복 결제 방지용 조회
    Optional<Payment> findByImpUid(String impUid);

    // merchant_uid로 결제 조회
    Optional<Payment> findByMerchantUid(String merchantUid);

    // 유저의 모든 결제 내역 조회 (포인트 내역 조회에 사용)
    List<Payment> findByUserId(Integer userId);
}
