package com.jobdam.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    private String merchantUid; // 주문번호 (프론트에서 생성 or 백엔드에서 UUID 생성)
    private Integer amount;     // 결제 금액
    private String method;      // 결제수단 (예: CARD, KAKAOPAY, POINT 등)
}
