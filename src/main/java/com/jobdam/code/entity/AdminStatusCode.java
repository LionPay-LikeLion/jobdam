package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_status_code")
public class AdminStatusCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_status_code_id")
    private Integer adminStatusCodeId;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 30, nullable = false)
    private String name;
}
