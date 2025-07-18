package com.jobdam.community.controller;


import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.community.dto.CommunityCommentCreateRequestDto;
import com.jobdam.community.dto.CommunityCommentListResponseDto;
import com.jobdam.community.dto.CommunityPostListResponseDto;
import com.jobdam.community.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/boards/{boardId}/posts/{postId}/comments")
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @PostMapping
    public ResponseEntity<Integer> createComment(
            @PathVariable Integer postId,
            @RequestBody CommunityCommentCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer commentId = communityCommentService.createComment(dto, postId, user.getUserId());
        return ResponseEntity.ok(commentId);
    }

    @GetMapping
    public ResponseEntity<List<CommunityCommentListResponseDto>> getCommentsByPostId(
            @PathVariable Integer postId
    ) {
        List<CommunityCommentListResponseDto> dto = communityCommentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my")
    public ResponseEntity<List<CommunityCommentListResponseDto>> getMyComments(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<CommunityCommentListResponseDto> dto = communityCommentService.getCommentsByUserId(user.getUserId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Integer commentId,
            @RequestBody CommunityCommentCreateRequestDto dto,
            @RequestParam Integer userId
    ) {
        communityCommentService.updateComment(dto, commentId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer commentId,
            @RequestParam Integer userId
    ) {
        communityCommentService.softDeleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }


}
