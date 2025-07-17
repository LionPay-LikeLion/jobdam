package com.jobdam.payment.history.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import com.jobdam.payment.entity.Payment;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistoryResponseDto {
    private Long paymentId;
    private String merchantUid;
    private String impUid;
    private Integer amount;
    private Integer point;
    private Integer paymentTypeCodeId;
    private Integer paymentStatusCodeId;
    private String method;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static PaymentHistoryResponseDto fromEntity(Payment payment) {
        return PaymentHistoryResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .merchantUid(payment.getMerchantUid())
                .impUid(payment.getImpUid())
                .amount(payment.getAmount())
                .point(payment.getPoint())
                .paymentTypeCodeId(payment.getPaymentTypeCodeId())
                .paymentStatusCodeId(payment.getPaymentStatusCodeId())
                .method(payment.getMethod())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
