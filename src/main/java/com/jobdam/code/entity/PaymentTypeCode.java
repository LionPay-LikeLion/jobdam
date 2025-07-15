package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_type_code")
public class PaymentTypeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_type_code_id")
    private Integer paymentTypeCodeId;

    @Column(length = 30, nullable = false, unique = true)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;
}
