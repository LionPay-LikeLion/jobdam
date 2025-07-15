// Payment.java
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
    private Integer point;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "payment_type_code_id", nullable = false)
    private Integer paymentTypeCodeId;

    @Column(name = "payment_status_code_id", nullable = false)
    @Builder.Default
    private Integer paymentStatusCodeId = 1;  // 1: SUCCESS

    @Column(name = "method", nullable = false, length = 50)
    private String method;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "imp_uid", unique = true, length = 100)
    private String impUid;

    @Column(name = "merchant_uid", unique = true, length = 100)
    private String merchantUid;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.paymentStatusCodeId == null) {
            this.paymentStatusCodeId = 1;
        }
    }
}
