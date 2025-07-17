// src/main/java/com/jobdam/payment/history/service/HistoryService.java
package com.jobdam.payment.history.service;

import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.history.dto.HistoryResponseDto;
import com.jobdam.payment.history.dto.PaymentHistoryResponseDto;
import com.jobdam.payment.history.repository.PaymentHistoryRepository;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final PaymentHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public HistoryResponseDto getUserHistory(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        int currentPoint = user.getPoint() == null ? 0 : user.getPoint();

        List<Payment> paymentList = historyRepository.findByUserIdOrderByCreatedAtAsc(userId);

        // 누적포인트 계산용 변수
        List<PaymentHistoryResponseDto> paymentDtos = new ArrayList<>();
        List<PaymentHistoryResponseDto> pointDtos = new ArrayList<>();

        int cumulative = 0;
        for (Payment payment : paymentList) {
            // 누적포인트 계산은 결제 성공(SUCCESS)과 환불(CANCELLED)만 반영 (FAILED는 무시)
            boolean isCountable =
                    payment.getPaymentStatusCodeId() == 1 || payment.getPaymentStatusCodeId() == 3;

            if (isCountable && payment.getPoint() != null) {
                cumulative += payment.getPoint();
            }

            PaymentHistoryResponseDto dto = PaymentHistoryResponseDto.fromEntity(payment, cumulative);

            // amount가 0이면 포인트 내역(포인트 사용/충전), 0보다 크면 결제 내역
            if (payment.getAmount() != null && payment.getAmount() > 0) {
                paymentDtos.add(dto); // 결제 내역
            }
            // 포인트 사용/적립/충전 내역은 모두 포함 (이제 충전도 표시!)
            if (payment.getPoint() != null) {
                pointDtos.add(dto);
            }
        }

        return HistoryResponseDto.builder()
                .currentPoint(currentPoint)
                .paymentList(paymentDtos)
                .pointList(pointDtos)
                .build();
    }

    // 결제내역만 반환
    public List<PaymentHistoryResponseDto> getPaymentList(Integer userId) {
        List<Payment> paymentList = historyRepository.findByUserIdOrderByCreatedAtAsc(userId);
        List<PaymentHistoryResponseDto> paymentDtos = new ArrayList<>();
        int cumulative = 0;
        for (Payment payment : paymentList) {
            boolean isCountable = payment.getPaymentStatusCodeId() == 1 || payment.getPaymentStatusCodeId() == 3;
            if (isCountable && payment.getPoint() != null) {
                cumulative += payment.getPoint();
            }
            if (payment.getAmount() != null && payment.getAmount() > 0) {
                paymentDtos.add(PaymentHistoryResponseDto.fromEntity(payment, cumulative));
            }
        }
        return paymentDtos;
    }

    // 포인트내역만 반환
    public List<PaymentHistoryResponseDto> getPointList(Integer userId) {
        List<Payment> paymentList = historyRepository.findByUserIdOrderByCreatedAtAsc(userId);
        List<PaymentHistoryResponseDto> pointDtos = new ArrayList<>();
        int cumulative = 0;
        for (Payment payment : paymentList) {
            boolean isCountable = payment.getPaymentStatusCodeId() == 1 || payment.getPaymentStatusCodeId() == 3;
            if (isCountable && payment.getPoint() != null) {
                cumulative += payment.getPoint();
            }
            if (payment.getPoint() != null) {
                pointDtos.add(PaymentHistoryResponseDto.fromEntity(payment, cumulative));
            }
        }
        return pointDtos;
    }
}
