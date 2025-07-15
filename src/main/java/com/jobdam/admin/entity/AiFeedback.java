package com.jobdam.admin.entity;

import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "ai_feedback")
public class AiFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_feedback_id")
    private Integer aiFeedbackId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "ai_feedback_type_code_id", nullable = false)
    private Integer aiFeedbackTypeCodeId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String inputText;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String outputText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // user 객체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
