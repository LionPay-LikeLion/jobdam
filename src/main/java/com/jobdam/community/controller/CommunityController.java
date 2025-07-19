package com.jobdam.community.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.community.dto.CommunityCreateRequestDto;
import com.jobdam.community.dto.CommunityListResponseDto;
import com.jobdam.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CommunityUpgradeRequestDto dto) {

        communityService.upgradeCommunityToPremium(user.getUserId(), dto);
        return ResponseEntity.ok("커뮤니티가 프리미엄으로 업그레이드되었습니다.");
    }

    @PostMapping
    public ResponseEntity<Integer> createCommunity(
            @ModelAttribute CommunityCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer id = communityService.createCommunity(dto, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping
    public ResponseEntity<List<CommunityListResponseDto>> getAllCommunities() {
        List<CommunityListResponseDto> communities = communityService.getAllCommunities();
        return ResponseEntity.ok(communities);
    }

    @GetMapping("/my")
    public ResponseEntity<List<CommunityListResponseDto>> getMyCommunities(
            @AuthenticationPrincipal CustomUserDetails user) {
        List<CommunityListResponseDto> communities = communityService.getCommunitiesByUserId(user.getUserId());
        return ResponseEntity.ok(communities);
    }

    @PostMapping("/{communityId}/join")
    public ResponseEntity<String> joinCommunity(
            @PathVariable Integer communityId,
            @AuthenticationPrincipal CustomUserDetails user) {
        communityService.joinCommunity(user.getUserId(), communityId);
        return ResponseEntity.ok("커뮤니티에 가입되었습니다.");
    }

}
