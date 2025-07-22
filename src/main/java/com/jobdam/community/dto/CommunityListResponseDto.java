package com.jobdam.community.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityListResponseDto {

    private Integer communityId;
    private String name;
    private String description;
    private String subscriptionLevelCode;
    private String ownerNickname;
    private String ownerProfileImageUrl;   // ⭐️ 추가!
    private Integer maxMember;
    private Integer currentMember;
    private Integer enterPoint;
    private String profileImageUrl; // 커뮤니티 자체 프로필

}

