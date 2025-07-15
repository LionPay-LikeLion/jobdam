package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_type_code")
public class MemberTypeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_type_code_id")
    private Integer memberTypeCodeId;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 30)
    private String name;
}
