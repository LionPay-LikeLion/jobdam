package com.jobdam.community.controller;

import com.jobdam.community.dto.CommunityBoardCreateRequestDto;
import com.jobdam.community.dto.CommunityBoardListResponseDto;
import com.jobdam.community.dto.CommunityListResponseDto;
import com.jobdam.community.service.CommunityBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities/{communityId}/boards")
@RequiredArgsConstructor
public class CommunityBoardController {

    private final CommunityBoardService communityBoardService;

    @PostMapping
    public ResponseEntity<Integer> createBoard(
            @PathVariable Integer communityId,
            @RequestBody CommunityBoardCreateRequestDto dto
    ) {
        Integer boardId = communityBoardService.createBoard(dto, communityId);
        return ResponseEntity.ok(boardId);
    }

    @GetMapping
    public ResponseEntity<List<CommunityBoardListResponseDto>> getAllCommunities(@PathVariable Integer communityId) {
        List<CommunityBoardListResponseDto> communities = communityBoardService.getAllBoardsByCommunityId(communityId);
        return ResponseEntity.ok(communities);
    }
}
