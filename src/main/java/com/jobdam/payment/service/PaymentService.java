// src/main/java/com/jobdam/payment/service/PaymentService.java
package com.jobdam.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jobdam.payment.client.PortOneClient;
import com.jobdam.payment.dto.CancelPaymentRequestDto;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.entity.PaymentStatusCode;
import com.jobdam.payment.entity.ChargeOption;
import com.jobdam.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PointService pointService;
    private final PortOneClient portOneClient;
    private final ObjectMapper objectMapper;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        ChargeOption option = ChargeOption.fromCode(dto.getChargeOption());
        String merchantUid = "order-" + UUID.randomUUID();  // 여기서만 UID 생성
        Payment payment = Payment.builder()
                .userId(dto.getUserId())
                .point(option.getPoint())
                .amount(option.getAmount())
                .paymentTypeCodeId(dto.getPaymentTypeCodeId())
                .paymentStatusCodeId(PaymentStatusCode.FAILED.getCode()) // 최초는 실패(미결)
                .method(dto.getMethod())
                .merchantUid(merchantUid)
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

        pointService.addPoint(payment.getUserId(), payment.getPoint());

        return toDto(payment);
    }

    @Transactional
    public PaymentResponseDto cancelPayment(CancelPaymentRequestDto dto) {
        // 1. 결제건 찾기 및 검증
        Payment payment = paymentRepository.findByMerchantUid(dto.getMerchantUid())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));

        if (!payment.getUserId().equals(dto.getUserId())) {
            throw new IllegalArgumentException("본인 결제만 환불 가능");
        }
        if (payment.getPaymentStatusCodeId() != PaymentStatusCode.SUCCESS.getCode()) {
            throw new IllegalArgumentException("성공한 결제만 환불 가능");
        }

        // 2. 포인트 잔액 검증 (당시 적립 포인트 이하, 그리고 현재 보유 포인트 이상인지)
        int userPoint = pointService.getUserPoint(dto.getUserId());
        int refundPoint = payment.getPoint();
        if (refundPoint > 0 && userPoint < refundPoint) {
            throw new IllegalArgumentException("현재 포인트 부족, 환불 불가");
        }

        // 3. 포트원(아임포트) 실제 취소 호출 (실제 취소 API 연동)
        // 포트원 연동 예시 (PortOneClient를 DI 받아서 사용)
        JsonNode result = portOneClient.cancelPayment(
                objectMapper.createObjectNode()
                        .put("imp_uid", dto.getImpUid())
                        .put("merchant_uid", dto.getMerchantUid())
                        .put("amount", dto.getAmount())
                        .put("reason", dto.getReason() != null ? dto.getReason() : "사용자 요청")
        );
        int code = result.path("code").asInt();
        if (code != 0) {
            throw new RuntimeException("외부 결제 취소 실패: " + result.path("message").asText());
        }

        // 4. DB 결제상태 환불처리 + 포인트 차감
        payment.setPaymentStatusCodeId(PaymentStatusCode.CANCELLED.getCode());
        paymentRepository.save(payment);

        if (refundPoint > 0) {
            pointService.subtractPoint(dto.getUserId(), refundPoint);
        }

        return toDto(payment);
    }


    public List<PaymentResponseDto> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PaymentResponseDto toDto(Payment p) {
        String status = PaymentStatusCode.fromCode(p.getPaymentStatusCodeId()).name();
        return PaymentResponseDto.builder()
                .merchantUid(p.getMerchantUid())
                .impUid(p.getImpUid())
                .amount(p.getAmount())
                .paymentStatus(status)
                .method(p.getMethod())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
