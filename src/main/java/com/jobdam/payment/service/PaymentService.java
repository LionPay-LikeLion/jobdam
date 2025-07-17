package com.jobdam.payment.service;

import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.entity.PaymentStatusCode;
import com.jobdam.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        Payment payment = Payment.builder()
                .userId(dto.getUserId())
                .point(dto.getPoint())
                .amount(dto.getAmount())
                .paymentTypeCodeId(dto.getPaymentTypeCodeId())
                .paymentStatusCodeId(PaymentStatusCode.FAILED.getCode()) // 기본 실패로 생성
                .method(dto.getMethod())
                .merchantUid(dto.getMerchantUid())
                .createdAt(LocalDateTime.now())
                .build();
        Payment saved = paymentRepository.save(payment);
        return toDto(saved);
    }

    @Transactional
    public PaymentResponseDto confirmPayment(String merchantUid, String impUid, Integer amount) {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));
        payment.setImpUid(impUid);
        payment.setAmount(amount);
        payment.setPaymentStatusCodeId(PaymentStatusCode.SUCCESS.getCode());
        paymentRepository.save(payment);
        return toDto(payment);
    }

    @Transactional
    public PaymentResponseDto cancelPayment(String merchantUid) {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));
        payment.setPaymentStatusCodeId(PaymentStatusCode.CANCELLED.getCode());
        paymentRepository.save(payment);
        return toDto(payment);
    }

    public List<PaymentResponseDto> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PaymentResponseDto toDto(Payment p) {
        return PaymentResponseDto.builder()
                .merchantUid(p.getMerchantUid())
                .impUid(p.getImpUid())
                .amount(p.getAmount())
                .paymentStatusCodeId(p.getPaymentStatusCodeId())
                .method(p.getMethod())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
