package com.jobdam.community.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityMemberListResponseDto {

    private Integer userId;
    private String nickname;
    private LocalDateTime joinedAt;
    private String profileImageUrl;
    private String communityMemberRoleCode;
    private String memberTypeCode;

}
