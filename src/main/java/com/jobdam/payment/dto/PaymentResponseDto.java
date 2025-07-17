// src/main/java/com/jobdam/payment/dto/PaymentResponseDto.java
package com.jobdam.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentResponseDto {
    private String merchantUid;
    private String impUid;
    private Integer amount;
    private String paymentStatus; // "SUCCESS", "FAILED", "CANCELLED"
    private String method;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
