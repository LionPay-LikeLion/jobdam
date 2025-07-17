package com.jobdam.community.controller;

import com.jobdam.community.dto.CommunityCreateRequestDto;
import com.jobdam.community.dto.CommunityListResponseDto;
import com.jobdam.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.jobdam.community.dto.CommunityUpgradeRequestDto;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/upgrade")
    public ResponseEntity<String> upgradeCommunity(
            @RequestParam Integer userId,
            @RequestBody CommunityUpgradeRequestDto dto) {

        communityService.upgradeCommunityToPremium(userId, dto);
        return ResponseEntity.ok("커뮤니티가 프리미엄으로 업그레이드되었습니다.");
    }

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

    @PostMapping("/{communityId}/join")
    public ResponseEntity<String> joinCommunity(
            @PathVariable Integer communityId,
            @RequestParam Integer userId) {
        communityService.joinCommunity(userId, communityId);
        return ResponseEntity.ok("커뮤니티에 가입되었습니다.");
    }

}
