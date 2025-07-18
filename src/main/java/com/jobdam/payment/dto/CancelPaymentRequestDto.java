// src/main/java/com/jobdam/payment/dto/CancelPaymentRequestDto.java
package com.jobdam.payment.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CancelPaymentRequestDto {

    private String impUid;                // 아임포트 결제 고유번호
    private String merchantUid;           // 상점 주문번호
    private Integer amount;               // 취소 금액
    private String reason;                // 취소 사유     // 취소 사용자
}
