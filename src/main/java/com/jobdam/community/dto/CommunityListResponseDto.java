package com.jobdam.community.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityListResponseDto {

    private Integer communityId;
    private String name;
    private String description;
    private String subscriptionLevelCode;
    private String ownerNickname;
    private Integer maxMember;
    private Integer currentMember;
    private Integer enterPoint;

}
