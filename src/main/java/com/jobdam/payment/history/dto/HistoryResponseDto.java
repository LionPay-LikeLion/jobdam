package com.jobdam.payment.history.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HistoryResponseDto {
    private Integer currentPoint;                         // 현재 보유 포인트
    private List<PaymentHistoryResponseDto> paymentList;  // 결제 내역
    private List<PaymentHistoryResponseDto> pointList;    // 포인트 사용/적립/충전 내역
}
