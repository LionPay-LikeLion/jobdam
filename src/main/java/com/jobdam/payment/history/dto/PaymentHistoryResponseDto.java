// src/main/java/com/jobdam/payment/history/dto/PaymentHistoryResponseDto.java
package com.jobdam.payment.history.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.entity.PaymentStatusCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentHistoryResponseDto {
    private Long paymentId;
    private Integer point;        // <-- 반드시 실제 값이 채워져야 함
    private Integer amount;
    private Integer paymentTypeCodeId;
    private Integer paymentStatusCodeId;
    private String paymentStatus; // "SUCCESS", "FAILED", "CANCELLED"
    private String method;
    private LocalDateTime createdAt;
    private String impUid;
    private String merchantUid;
    private Integer cumulativePoint;
    private String statusColor;
    private String statusLabel;   // 상태 라벨 (성공/실패/취소 등)

    public static PaymentHistoryResponseDto fromEntity(Payment payment, Integer cumulativePoint) {
        PaymentStatusCode statusEnum = PaymentStatusCode.fromCode(payment.getPaymentStatusCodeId());
        return PaymentHistoryResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .point(payment.getPoint())
                .amount(payment.getAmount())
                .paymentTypeCodeId(payment.getPaymentTypeCodeId())
                .paymentStatus(statusEnum.name())  // Enum의 "SUCCESS" 등 문자열
                .statusColor(getStatusColor(statusEnum))
                .method(payment.getMethod())
                .createdAt(payment.getCreatedAt())
                .impUid(payment.getImpUid())
                .merchantUid(payment.getMerchantUid())
                .cumulativePoint(cumulativePoint)
                .build();
    }

    private static String getStatusColor(PaymentStatusCode code) {
        switch (code) {
            case SUCCESS:   return "green";
            case FAILED:    return "orange";
            case CANCELLED: return "red";
            default:        return "gray";
        }
    }
}
