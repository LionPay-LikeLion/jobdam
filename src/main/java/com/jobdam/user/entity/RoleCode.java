package com.jobdam.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_code")
@Getter
@NoArgsConstructor
public class RoleCode {

    @Id
    private Integer role_code_id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;
}
