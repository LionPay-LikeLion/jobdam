package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;
import com.jobdam.sns.dto.MySnsCommentResponseDto;

import java.util.List;

public interface SnsCommentService {

    List<SnsCommentResponseDto> getCommentsByPostId(Integer snsPostId);
    Integer createComment(SnsCommentRequestDto requestDto, Integer userId);
    void updateComment(Integer commentId, SnsCommentRequestDto requestDto, Integer userId);
    void deleteComment(Integer commentId, Integer userId);
    List<MySnsCommentResponseDto> getMyComments(Integer userId);

}
