package com.jobdam.admin.entity;

import com.jobdam.code.entity.AdminStatusCode;
import com.jobdam.code.entity.MemberTypeCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "membertype_change")
public class MembertypeChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membertype_change_id")
    private Integer membertypeChangeId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "current_member_type_code_id", nullable = false)
    private Integer currentMemberTypeCodeId;

    @Column(name = "requested_member_type_code_id", nullable = false)
    private Integer requestedMemberTypeCodeId;

    @Column(name = "request_admin_status_code_id", nullable = false)
    private Integer requestAdminStatusCodeId;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "attachment_url", length = 255)
    private String attachmentUrl;

    @Column(name = "reference_link", length = 255)
    private String referenceLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_member_type_code_id", insertable = false, updatable = false)
    private MemberTypeCode currentMemberTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_member_type_code_id", insertable = false, updatable = false)
    private MemberTypeCode requestedMemberTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_admin_status_code_id", insertable = false, updatable = false)
    private AdminStatusCode requestAdminStatusCode;

}
