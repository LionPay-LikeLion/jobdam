package com.jobdam.sns.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SnsPostDetailResponseDto {
    private Integer snsPostId;
    private Integer userId;
    private String nickname;
    private String title;
    private String content;
    private String imageUrl;
    private String attachmentUrl;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private String boardStatusCode;
    private String profileImageUrl;
    private boolean liked;
    private boolean bookmarked;
}
