package com.jobdam.community.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityDetailResponseDto {

    private Long communityId;
    private String name;
    private String description;
    private String bannerImageUrl;
    private String subscriptionLevelCode;
    private String ownerNickname;
    private Integer currentMember;
    private Integer currentBoard;
    private Integer enterPoint;
    private String profileImageUrl;

    private List<CommunityBoardListResponseDto> popularBoards;

}
