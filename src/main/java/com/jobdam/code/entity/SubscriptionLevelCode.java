package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscription_level_code")
public class SubscriptionLevelCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_level_code_id")
    private Integer subscriptionLevelCodeId;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 30)
    private String name;
}
