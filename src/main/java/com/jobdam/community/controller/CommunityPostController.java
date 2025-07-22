package com.jobdam.community.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.community.dto.*;
import com.jobdam.community.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/boards/{boardId}/posts")
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    @GetMapping
    public ResponseEntity<List<CommunityPostListResponseDto>> getPosts(
            @PathVariable Integer boardId,
            @RequestParam(required = false) String postTypeCode,
            @RequestParam(required = false) String keyword
    ) {
        List<CommunityPostListResponseDto> dto = communityPostService.getPostsByBoard(boardId, postTypeCode, keyword);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommunityPostDetailResponseDto> getPostDetail(
            @PathVariable Integer postId
    ) {
        CommunityPostDetailResponseDto dto = communityPostService.getPostDetail(postId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Integer> createPost(
            @PathVariable Integer boardId,
            @RequestBody CommunityPostCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer postId = communityPostService.createPost(dto, boardId, user.getUserId());
        return ResponseEntity.ok(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Integer postId,
            @RequestBody CommunityPostUpdateRequestDto dto,
            @RequestParam Integer userId
    ) {
        communityPostService.updatePost(dto, postId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Integer postId,
            @RequestParam Integer userId
    ) {
        communityPostService.softDelete(postId, userId);
        return ResponseEntity.ok().build();
    }



}
