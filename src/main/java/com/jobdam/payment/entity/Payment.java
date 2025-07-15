package com.jobdam.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "point", nullable = false)
    private Integer point; // 충전 or 차감

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "payment_type_code_id", nullable = false)
    private Integer paymentTypeCodeId;

    @Column(name = "payment_status_code_id", nullable = false)
    private Integer paymentStatusCodeId;

    @Column(name = "method", nullable = false, length = 50)
    private String method;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "imp_uid", unique = true, length = 100)
    private String impUid;

    @Column(name = "merchant_uid", unique = true, length = 100)
    private String merchantUid;

    // 결제 상태 (예: READY, PAID, FAILED)
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    // 실제 결제 완료 시간
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // 실패 사유 (optional)
    @Column(name = "fail_reason")
    private String failReason;

}