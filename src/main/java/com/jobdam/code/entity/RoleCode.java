package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_code")
public class RoleCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_code_id")
    private Integer roleCodeId;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 30)
    private String name;
}
