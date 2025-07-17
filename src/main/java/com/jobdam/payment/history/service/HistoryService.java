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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

        List<Payment> paymentList = historyRepository.findByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Payment::getCreatedAt))
                .collect(Collectors.toList());

        List<PaymentHistoryResponseDto> payments = new ArrayList<>();
        List<PaymentHistoryResponseDto> points = new ArrayList<>();

        int cumulativePoint = 0;
        for (Payment p : paymentList) {
            // 누적포인트는 1(SUCCESS), 3(CANCELLED)만 반영
            boolean isSuccessOrCancelled =
                    p.getPaymentStatusCodeId() != null &&
                            (p.getPaymentStatusCodeId() == 1 || p.getPaymentStatusCodeId() == 3);

            if (isSuccessOrCancelled) {
                cumulativePoint += (p.getPoint() == null ? 0 : p.getPoint());
            }

            if (p.getAmount() != null && p.getAmount() > 0) {
                payments.add(PaymentHistoryResponseDto.fromEntity(p, cumulativePoint));
            } else {
                points.add(PaymentHistoryResponseDto.fromEntity(p, cumulativePoint));
            }
        }

        return HistoryResponseDto.builder()
                .currentPoint(currentPoint)
                .paymentList(payments)
                .pointList(points)
                .build();
    }
}
