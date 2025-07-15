// PaymentService.java
package com.jobdam.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
        String merchantUid = requestDto.getMerchantUid();
        if (merchantUid == null || merchantUid.isBlank()) {
            merchantUid = UUID.randomUUID().toString();
        }

        Payment payment = Payment.builder()
                .merchantUid(merchantUid)
                .amount(requestDto.getAmount())
                .method(requestDto.getMethod())
                .userId(requestDto.getUserId())
                .point(requestDto.getPoint())
                .paymentTypeCodeId(requestDto.getPaymentTypeCodeId())
                // paymentStatusCodeId defaults to 1 (SUCCESS)
                .build();

        Payment saved = paymentRepository.save(payment);

        return PaymentResponseDto.builder()
                .merchantUid(saved.getMerchantUid())
                .impUid(saved.getImpUid())
                .amount(saved.getAmount())
                .method(saved.getMethod())
                .paymentStatusCodeId(saved.getPaymentStatusCodeId())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional
    public void failPayment(String merchantUid) {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatusCodeId(2); // 2: FAILED
        paymentRepository.save(payment);
    }

    @Transactional
    public void cancelPayment(String merchantUid) {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatusCodeId(3); // 3: CANCELLED
        paymentRepository.save(payment);
    }

    @Transactional
    public void confirmPayment(String impUid, String merchantUid, JsonNode iamportResult) {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setImpUid(impUid);
        payment.setPaymentStatusCodeId(1); // 1: SUCCESS
        paymentRepository.save(payment);
    }

    public List<PaymentResponseDto> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(p -> PaymentResponseDto.builder()
                        .merchantUid(p.getMerchantUid())
                        .impUid(p.getImpUid())
                        .amount(p.getAmount())
                        .method(p.getMethod())
                        .paymentStatusCodeId(p.getPaymentStatusCodeId())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();
    }
}
