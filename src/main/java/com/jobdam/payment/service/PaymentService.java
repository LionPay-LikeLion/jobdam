package com.jobdam.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jobdam.payment.client.PortOneClient;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PortOneClient portOneClient;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        String merchantUid = Optional.ofNullable(dto.getMerchantUid())
                .filter(s -> !s.isBlank())
                .orElse("order_" + UUID.randomUUID());

        ObjectNode payload = JsonNodeFactory.instance.objectNode()
                .put("merchant_uid", merchantUid)
                .put("amount", dto.getAmount());
        portOneClient.readyPayment(payload);

        Payment p = Payment.builder()
                .merchantUid(merchantUid)
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .userId(dto.getUserId())
                .point(dto.getPoint())
                .paymentTypeCodeId(dto.getPaymentTypeCodeId())
                .paymentStatusCodeId(1)
                .build();
        return toDto(paymentRepository.save(p));
    }

    @Transactional
    public PaymentResponseDto confirmPayment(String impUid, String merchantUid, JsonNode pgResult) {
        portOneClient.approvePayment(pgResult);
        Payment p = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("결제 내역 없음: " + merchantUid));
        p.setImpUid(impUid);
        p.setPaymentStatusCodeId(1);
        return toDto(paymentRepository.save(p));
    }

    @Transactional
    public PaymentResponseDto failPayment(String merchantUid) {
        ObjectNode payload = JsonNodeFactory.instance.objectNode()
                .put("imp_uid", paymentRepository.findByMerchantUid(merchantUid)
                        .orElseThrow().getImpUid());
        portOneClient.failPayment(payload);
        return changeStatus(merchantUid, 2);
    }

    @Transactional
    public PaymentResponseDto cancelPayment(String merchantUid) {
        ObjectNode payload = JsonNodeFactory.instance.objectNode()
                .put("imp_uid", paymentRepository.findByMerchantUid(merchantUid)
                        .orElseThrow().getImpUid());
        portOneClient.cancelPayment(payload);
        return changeStatus(merchantUid, 3);
    }

    public List<PaymentResponseDto> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    private PaymentResponseDto changeStatus(String merchantUid, int code) {
        Payment p = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new RuntimeException("결제 내역 없음: " + merchantUid));
        p.setPaymentStatusCodeId(code);
        return toDto(paymentRepository.save(p));
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
