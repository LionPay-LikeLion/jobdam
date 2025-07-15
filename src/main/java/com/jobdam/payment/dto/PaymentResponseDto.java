package com.jobdam.payment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private String merchantUid;
    private String impUid;
    private Integer amount;
    private String status;
    private String method;
    private LocalDateTime paidAt;
    private String failReason;
}