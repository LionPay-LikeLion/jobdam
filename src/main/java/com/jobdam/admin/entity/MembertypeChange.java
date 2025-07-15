package com.jobdam.admin.entity;

import com.jobdam.code.domain.AdminStatusCode;
import com.jobdam.code.domain.MemberTypeCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "membertype_change")
public class MembertypeChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer membertypeChangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 현재 회원 유형
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_member_id", nullable = false)
    private MemberTypeCode currentMember;

    // 요청 회원 유형
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_member_id", nullable = false)
    private MemberTypeCode requestedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_status_id", nullable = false)
    private AdminStatusCode requestStatus;

    @Column
    private LocalDateTime requestedAt;

    @Column
    private LocalDateTime processedAt;
}

