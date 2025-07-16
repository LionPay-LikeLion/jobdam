package com.jobdam.community.controller;

import com.jobdam.community.dto.CommunityCreateRequestDto;
import com.jobdam.community.dto.CommunityListResponseDto;
import com.jobdam.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<Integer> createCommunity(@RequestBody CommunityCreateRequestDto dto) {

        Integer id = communityService.createCommunity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping
    public ResponseEntity<List<CommunityListResponseDto>> getAllCommunities() {
        List<CommunityListResponseDto> communities = communityService.getAllCommunities();
        return ResponseEntity.ok(communities);
    }

    @GetMapping("/my")
    public ResponseEntity<List<CommunityListResponseDto>> getMyCommunities(
            @RequestParam Integer userId) {
        List<CommunityListResponseDto> communities = communityService.getCommunitiesByUserId(userId);
        return ResponseEntity.ok(communities);
    }

}
