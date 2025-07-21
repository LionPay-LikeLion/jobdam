package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SnsPostResponseDto {


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
    private String memberTypeCode;
    private String subscriptionLevelCode;


    private boolean isLiked;
    private boolean isBookmarked;
}
