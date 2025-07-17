package com.jobdam.admin.entity;

import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "report_type_code_id", nullable = false)
    private Integer reportTypeCodeId; // 신고 타입 (1: POST, 2: COMMENT, 3: USER)

    @Column(name = "target_id", nullable = false)
    private Long targetId; // 어떤 대상에 대한 신고인지 (예: post_id, comment_id 등)

    @Column(name = "user_id", nullable = false)
    private Integer userId; // 신고자 ID

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason; // 신고 사유

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 신고자 연관 관계 (User와의 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
