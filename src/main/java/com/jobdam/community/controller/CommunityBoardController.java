package com.jobdam.community.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.community.dto.CommunityBoardCreateRequestDto;
import com.jobdam.community.dto.CommunityBoardListResponseDto;
import com.jobdam.community.dto.CommunityListResponseDto;
import com.jobdam.community.service.CommunityBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities/{communityId}/boards")
@RequiredArgsConstructor
public class CommunityBoardController {

    private final CommunityBoardService communityBoardService;

   /* @PostMapping
    public ResponseEntity<Integer> createBoard(
            @PathVariable Integer communityId,
            @RequestBody CommunityBoardCreateRequestDto dto
    ) {
        Integer boardId = communityBoardService.createBoard(dto, communityId);
        return ResponseEntity.ok(boardId);
    }*/

    @PostMapping
    public ResponseEntity<String> createBoard(
            @PathVariable Integer communityId,
            @RequestBody CommunityBoardCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user // 현재 로그인한 사용자 정보
    ) {

        communityBoardService.createBoard(communityId, dto, user.getUserId());
        return ResponseEntity.ok("게시판이 생성되었습니다.");
    }


    @GetMapping
    public ResponseEntity<List<CommunityBoardListResponseDto>> getAllCommunities(@PathVariable Integer communityId) {
        List<CommunityBoardListResponseDto> communities = communityBoardService.getAllBoardsByCommunityId(communityId);
        return ResponseEntity.ok(communities);
    }
}
