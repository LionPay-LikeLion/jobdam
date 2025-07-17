package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkResponseDto {

    private Integer bookmarkId;
    private Integer snsPostId;
    private String title;
    private String thumbnailImageUrl;
    private LocalDateTime bookmarkedAt;

    private Integer userId;
    private String nickname;
}
