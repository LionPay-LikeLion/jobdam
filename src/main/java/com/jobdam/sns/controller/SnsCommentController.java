package com.jobdam.sns.controller;

import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;
import com.jobdam.sns.service.SnsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
                                 @RequestParam Integer userId) {
        return snsCommentService.createComment(requestDto, userId);
    }

    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable Integer commentId,
                              @RequestBody SnsCommentRequestDto requestDto,
                              @RequestParam Integer userId) {
        snsCommentService.updateComment(commentId, requestDto, userId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId,
                              @RequestParam Integer userId) {
        snsCommentService.deleteComment(commentId, userId);
    }
}
