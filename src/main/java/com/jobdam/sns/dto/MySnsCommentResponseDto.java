package com.jobdam.sns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MySnsCommentResponseDto {
    private Integer commentId;
    private Integer postId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
