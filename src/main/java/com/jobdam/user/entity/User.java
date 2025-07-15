package com.jobdam.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(name = "subscription_level_code_id", nullable = false)
    private Integer subscriptionLevelCodeId;

    @Column(name = "role_code_id", nullable = false)
    private Integer roleCodeId;

    @Column(name = "member_type_code_id", nullable = false)
    private Integer memberTypeCodeId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private Integer point;

    @Column(length = 20)
    private String phone;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;
}
