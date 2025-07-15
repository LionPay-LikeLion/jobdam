package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;

import java.util.List;

public interface SnsCommentService {

    // 게시글 ID 기준 전체 댓글 조회
    List<SnsCommentResponseDto> getCommentsByPostId(Integer snsPostId);

    // 댓글 작성
    Integer createComment(SnsCommentRequestDto requestDto, Integer userId);

    // 댓글 수정
    void updateComment(Integer commentId, SnsCommentRequestDto requestDto, Integer userId);

    // 댓글 삭제
    void deleteComment(Integer commentId, Integer userId);
}
