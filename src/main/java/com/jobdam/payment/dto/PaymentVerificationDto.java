package com.jobdam.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVerificationDto {

    private String impUid;       // PortOne 결제 고유 ID
    private String merchantUid;  // 내가 지정한 주문번호


    //결제 교차검증용 DTO

    //이 DTO 이용한 웹훅은 지피티한테 물어보고 필요시 만들어보겠습니다
}
