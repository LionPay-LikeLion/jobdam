package com.jobdam.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "community_member_role_code")
public class CommunityMemberRoleCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_member_role_code_id")
    private Integer communityMemberRoleCodeId;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 30, nullable = false)
    private String name;
}
