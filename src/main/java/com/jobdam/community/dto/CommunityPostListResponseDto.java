package com.jobdam.community.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostListResponseDto {

    private Integer postId;
    private Integer boardId;
    private String title;
    private String content;
    private String userNickname;
    private LocalDateTime createdAt;
    private Integer commentCount;
    private Integer viewCount;
    private String postTypeCode;
    private String boardStatusCode;


}
