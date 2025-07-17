package com.jobdam.payment.history.dto;

import com.jobdam.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentHistoryResponseDto {

    private Long paymentId;
    private Integer point;           // 거래 포인트 증감 (충전/사용/적립 등)
    private Integer amount;          // 결제 금액
    private Integer paymentTypeCodeId;
    private Integer paymentStatusCodeId;
    private String method;
    private LocalDateTime createdAt;
    private String impUid;
    private String merchantUid;
    private Integer cumulativePoint; // 누적포인트(중간값)
    private String statusColor;      // 상태 플래그에 따른 색상명 (초록/주황/빨강)

    public static PaymentHistoryResponseDto fromEntity(Payment payment, Integer cumulativePoint) {
        return PaymentHistoryResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .point(payment.getPoint())
                .amount(payment.getAmount())
                .paymentTypeCodeId(payment.getPaymentTypeCodeId())
                .paymentStatusCodeId(payment.getPaymentStatusCodeId())
                .method(payment.getMethod())
                .createdAt(payment.getCreatedAt())
                .impUid(payment.getImpUid())
                .merchantUid(payment.getMerchantUid())
                .cumulativePoint(cumulativePoint)
                .statusColor(getStatusColor(payment.getPaymentStatusCodeId()))
                .build();
    }

    // 상태코드에 따른 색상 반환
    private static String getStatusColor(Integer code) {
        if (code == null) return "gray";
        switch (code) {
            case 1: return "green";   // SUCCESS
            case 2: return "orange";  // FAILED
            case 3: return "red";     // CANCELLED
            default: return "gray";
        }
    }
}
