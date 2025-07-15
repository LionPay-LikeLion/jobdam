package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SnsPostResponseDto {

    // 게시글 조회 시 프론트로 전달하는 응답 데이터

    private Integer snsPostId;
    private Integer userId;
    private String nickname;       // 작성자 닉네임
    private String title;
    private String content;
    private String imageUrl;
    private String attachmentUrl;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;

    private boolean isLiked;
    private boolean isBookmarked;
}
