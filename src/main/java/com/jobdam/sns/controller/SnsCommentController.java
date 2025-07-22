package com.jobdam.sns.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;
import com.jobdam.sns.service.SnsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.jobdam.sns.dto.MySnsCommentResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/sns/comments")
@RequiredArgsConstructor
public class SnsCommentController {

    private final SnsCommentService snsCommentService;

    @GetMapping("/{postId}")
    public List<SnsCommentResponseDto> getComments(@PathVariable Integer postId) {
        return snsCommentService.getCommentsByPostId(postId);
    }

    @PostMapping
    public Integer createComment(@RequestBody SnsCommentRequestDto requestDto,
                                 @AuthenticationPrincipal CustomUserDetails user) {
        return snsCommentService.createComment(requestDto, user.getUserId());
    }

    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable Integer commentId,
                              @RequestBody SnsCommentRequestDto requestDto,
                              @AuthenticationPrincipal CustomUserDetails user) {
        snsCommentService.updateComment(commentId, requestDto, user.getUserId());
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId,
                              @AuthenticationPrincipal CustomUserDetails user) {
        snsCommentService.deleteComment(commentId, user.getUserId());
    }

    @GetMapping("/my")
    public List<MySnsCommentResponseDto> getMyComments(@AuthenticationPrincipal CustomUserDetails user) {
        return snsCommentService.getMyComments(user.getUserId());
    }


}
