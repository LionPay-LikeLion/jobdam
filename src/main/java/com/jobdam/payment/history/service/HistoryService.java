package com.jobdam.payment.history.service;

import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.history.dto.PaymentHistoryResponseDto;
import com.jobdam.payment.history.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PaymentHistoryRepository historyRepository;

    public List<PaymentHistoryResponseDto> getUserPaymentHistory(Integer userId) {
        List<Payment> paymentList = historyRepository.findByUserId(userId);

        return paymentList.stream()
                .map(payment -> PaymentHistoryResponseDto.builder()
                        .merchantUid(payment.getMerchantUid())
                        .impUid(payment.getImpUid())
                        .amount(payment.getAmount())
                        .point(payment.getPoint())
                        .paymentTypeCodeId(payment.getPaymentTypeCodeId())
                        .paymentStatusCodeId(payment.getPaymentStatusCodeId())
                        .method(payment.getMethod())
                        .createdAt(payment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
