package com.jobdam.community.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityAdminManageResponseDto {

    // 커뮤니티 기본 정보
    private Integer communityId;
    private String name;
    private String description;
    private String profileImageUrl;

    // 플랜(구독 등급) 정보
    private String subscriptionLevelCode; // "BASIC", "PREMIUM" 등
    private LocalDateTime planExpireAt;   // 만료일(프리미엄만 노출)

    // 멤버 리스트
    private List<MemberDto> members;

    // 게시판 리스트
    private List<BoardDto> boards;

    // 내부 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MemberDto {
        private Integer userId;
        private String nickname;
        private String profileImageUrl;
        private LocalDateTime joinedAt;
        private String communityMemberRoleCode;
        private String memberTypeCode;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BoardDto {
        private Integer communityBoardId;
        private String name;
        private String description;
        private String boardTypeCode;
        private String boardStatusCode;
    }
}
