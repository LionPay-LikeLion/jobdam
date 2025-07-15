// src/main/java/com/jobdam/payment/service/PaymentService.java
package com.jobdam.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /** [1] 결제 준비 시 PortOne API에 ready 요청 후 DB에 기본 정보 저장 */
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        String merchantUid = (dto.getMerchantUid() == null || dto.getMerchantUid().isBlank())
                ? UUID.randomUUID().toString()
                : dto.getMerchantUid();

        // → 실제 PortOne REST 호출 코드는 여기에 추가
        // 예: portOneClient.ready(...)

        Payment p = Payment.builder()
                .merchantUid(merchantUid)
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .userId(dto.getUserId())
                .point(dto.getPoint())
                .paymentTypeCodeId(dto.getPaymentTypeCodeId())
                .paymentStatusCodeId(1)    // 1: SUCCESS (PortOne ready 응답 기준)
                .build();

        Payment saved = paymentRepository.save(p);
        return toDto(saved);
    }

    /** [2] 승인 처리 (PortOne approve 콜백) */
    @Transactional
    public PaymentResponseDto confirmPayment(String impUid, String merchantUid, JsonNode pgResult) {
        Payment p = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("결제 내역 없음: " + merchantUid));
        p.setImpUid(impUid);
        p.setPaymentStatusCodeId(1); // 1: SUCCESS
        return toDto(paymentRepository.save(p));
    }

    /** [3] 실패 처리 */
    @Transactional
    public PaymentResponseDto failPayment(String merchantUid) {
        return changeStatus(merchantUid, 2);  // 2: FAILED
    }

    /** [4] 취소 처리 */
    @Transactional
    public PaymentResponseDto cancelPayment(String merchantUid) {
        return changeStatus(merchantUid, 3);  // 3: CANCELLED
    }

    private PaymentResponseDto changeStatus(String merchantUid, int statusCode) {
        Payment p = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("결제 내역 없음: " + merchantUid));
        p.setPaymentStatusCodeId(statusCode);
        return toDto(paymentRepository.save(p));
    }

    public List<PaymentResponseDto> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
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
