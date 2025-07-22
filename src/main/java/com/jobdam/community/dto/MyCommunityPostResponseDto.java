package com.jobdam.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyCommunityPostResponseDto {

    private Integer postId;
    private String title;
    private Integer viewCount;
    private Long commentCount;
    private LocalDateTime createdAt;

}
