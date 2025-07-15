package com.jobdam.payment.entity;

import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer point;

    @Column
    private Integer amount;

    @Column(name = "payment_type_code_id", nullable = false)
    private Integer paymentTypeCodeId;

    @Column(name = "payment_status_code_id", nullable = false)
    private Integer paymentStatusCodeId;

    @Column(length = 50, nullable = false)
    private String method;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "imp_uid", length = 100, unique = true)
    private String impUid;

    @Column(name = "merchant_uid", length = 100, unique = true)
    private String merchantUid;

    // user 객체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
