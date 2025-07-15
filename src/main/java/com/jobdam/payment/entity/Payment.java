package com.jobdam.payment.entity;

import com.jobdam.code.domain.PaymentStatusCode;
import com.jobdam.code.domain.PaymentTypeCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

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
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer point;

    @Column
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id", nullable = false)
    private PaymentTypeCode paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private PaymentStatusCode status;

    @Column(length = 50, nullable = false)
    private String method;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 100, unique = true)
    private String impUid;

    @Column(length = 100, unique = true)
    private String merchantUid;
}


