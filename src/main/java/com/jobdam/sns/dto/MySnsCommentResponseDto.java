package com.jobdam.sns.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MySnsCommentResponseDto {
    private Integer commentId;
    private Integer postId;
    private String content;
    private String boardStatusCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
