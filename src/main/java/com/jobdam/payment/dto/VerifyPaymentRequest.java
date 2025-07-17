// src/main/java/com/jobdam/payment/dto/VerifyPaymentRequest.java
package com.jobdam.payment.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VerifyPaymentRequest {
    private String impUid;                // 아임포트 결제 고유번호
    private String merchantUid;           // 상점 주문번호
}
