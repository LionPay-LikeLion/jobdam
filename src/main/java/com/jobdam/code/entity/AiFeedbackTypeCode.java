package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ai_feedback_type_code")
public class AiFeedbackTypeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_feedback_type_code_id")
    private Integer aiFeedbackTypeCodeId;

    @Column(length = 30, nullable = false, unique = true)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;
}
