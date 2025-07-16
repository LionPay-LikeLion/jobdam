package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report_type_code")
public class ReportTypeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_type_code_id")
    private Integer reportTypeCodeId;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 30, nullable = false)
    private String name;
}
