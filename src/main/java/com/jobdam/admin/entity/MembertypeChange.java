package com.jobdam.admin.entity;

import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
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
}
