package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SnsCommentResponseDto {


    private Integer snsCommentId;
    private Integer snsPostId;
    private Integer userId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}