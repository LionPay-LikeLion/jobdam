// src/main/java/com/jobdam/payment/dto/PreparePaymentRequest.java
package com.jobdam.payment.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PreparePaymentRequest {
    private Integer userId;               // 사용자 ID
    private Integer amount;               // 결제 금액
    private Integer point;                // 적립 포인트
    private Integer paymentTypeCodeId;    // 결제 타입 코드
    private String merchantUid;           // 상점 주문번호
    private String method;                // 결제 수단 (예: CARD)
}
