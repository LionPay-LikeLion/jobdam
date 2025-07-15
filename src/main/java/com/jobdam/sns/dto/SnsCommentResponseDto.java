package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SnsCommentResponseDto {

    // 댓글 조회 시 프론트로 전달하는 응답 데이터

    private Integer snsCommentId;     // 댓글 ID
    private Integer snsPostId;        // 게시글 ID
    private Integer userId;           // 작성자 ID
    private String nickname;          // 작성자 닉네임
    private String content;           // 댓글 내용
    private LocalDateTime createdAt;  // 작성 시간
    private LocalDateTime updatedAt;  // 수정 시간
}