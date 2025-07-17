package com.jobdam.payment.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequestDto {
    private Integer userId;
    private Integer point;
    private Integer amount;
    private Integer paymentTypeCodeId;
    private Integer paymentStatusCodeId;
    private String merchantUid;
    private String method;
}