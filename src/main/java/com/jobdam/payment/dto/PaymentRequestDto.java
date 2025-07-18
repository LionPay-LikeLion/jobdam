//package com.jobdam.payment.dto;
//
//import lombok.*;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class PaymentRequestDto {
//    private Integer userId;
//    private Integer point;
//    private Integer amount;
//    private Integer paymentTypeCodeId;
//    private Integer paymentStatusCodeId;
//    private String merchantUid;
//    private String method;
//}
package com.jobdam.payment.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequestDto {

    private String chargeOption; // 프론트에서 amount/point 대신 옵션코드만 전달
    private String method;
    private String merchantUid;
    private Integer paymentTypeCodeId;

}