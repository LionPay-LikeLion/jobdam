package com.jobdam.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Recommended for JPA
import java.time.LocalDateTime;

import com.jobdam.code.entity.MemberTypeCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor // Explicitly include this
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

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private Integer point;

    @Column(length = 20)
    private String phone;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "member_type_code_id",
        referencedColumnName = "member_type_code_id",
        insertable = false,
        updatable = false
    )
    private MemberTypeCode memberTypeCode;

    /*
    // Re-enable this if you want relation to post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_post_id", insertable = false, updatable = false)
    private SnsPost post;
    */
}