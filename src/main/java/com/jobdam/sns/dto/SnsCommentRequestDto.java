package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnsCommentRequestDto {

    // 댓글 작성/수정 시 클라이언트가 전달하는 데이터

    private Integer snsPostId;  // 댓글이 달릴 게시글 ID
    private String content;     // 댓글 내용
}
