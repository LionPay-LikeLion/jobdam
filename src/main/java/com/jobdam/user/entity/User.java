package com.jobdam.user.entity;

import com.jobdam.code.domain.MemberTypeCode;
import com.jobdam.code.domain.RoleCode;
import com.jobdam.code.domain.SubscriptionLevelCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(name = "user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    // Lookup 테이블 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_level_id", nullable = false)
    private SubscriptionLevelCode subscriptionLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleCode role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_type_id", nullable = false)
    private MemberTypeCode memberType;

    @Column
    private Integer point;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String profileImageUrl;

    @Column
    private java.sql.Timestamp createdAt;
}

