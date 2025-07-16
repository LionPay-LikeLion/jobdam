// src/main/java/com/jobdam/payment/dto/PaymentRequestDto.java
package com.jobdam.payment.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequestDto {
    private Integer userId;
    private Integer point;
    private Integer paymentTypeCodeId;
    private String merchantUid;
    private Integer amount;
    private String method;
}
