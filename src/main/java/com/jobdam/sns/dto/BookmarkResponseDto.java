package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkResponseDto {

    private Integer bookmarkId;         // 북마크 고유 ID
    private Integer snsPostId;          // 게시글 ID
    private String title;               // 게시글 제목
    private String thumbnailImageUrl;   // 게시글 대표 이미지
    private LocalDateTime bookmarkedAt; // 북마크한 시각

    private Integer userId;             // 북마크한 유저 ID (선택)
    private String nickname;            // 작성자 닉네임 (선택)
}
