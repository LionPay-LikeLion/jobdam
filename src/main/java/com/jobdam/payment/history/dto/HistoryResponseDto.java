package com.jobdam.payment.history.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HistoryResponseDto {
    private int currentPoint;
    private List<PaymentHistoryResponseDto> paymentList; // 결제 내역 (amount > 0)
    private List<PaymentHistoryResponseDto> pointList;   // 포인트 사용 내역 (amount == 0 or null)
}
