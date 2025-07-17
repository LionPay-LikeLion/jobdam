package com.jobdam.community.entity;

import com.jobdam.code.entity.CommunityMemberRoleCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_member")
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_member_id")
    private Integer communityMemberId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "community_id", nullable = false)
    private Integer communityId;

    @Column(name = "community_member_role_code_id", nullable = false)
    private Integer communityMemberRoleCodeId;

    @Column(name = "paid_point")
    private Integer paidPoint;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_member_role_code_id", insertable = false, updatable = false)
    private CommunityMemberRoleCode communityMemberRoleCode;
}
