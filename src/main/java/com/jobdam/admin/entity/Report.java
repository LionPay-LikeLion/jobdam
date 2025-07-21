package com.jobdam.admin.entity;

import com.jobdam.code.entity.ReportTypeCode;
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
    private Integer reportTypeCodeId;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // User FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // ReportTypeCode FK (코드 명칭 join 하고 싶을 때)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_type_code_id", insertable = false, updatable = false)
    private ReportTypeCode reportTypeCode;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    @Column(name = "status", nullable = false)
    private Integer status = 0; // 0: 대기, 1: 허용, 2: 정지, 3: 처리완료

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
