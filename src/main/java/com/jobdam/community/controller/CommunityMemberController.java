package com.jobdam.community.controller;

import com.jobdam.community.dto.CommunityMemberListResponseDto;
import com.jobdam.community.repository.CommunityMemberRepository;
import com.jobdam.community.service.CommunityMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityMemberController {

    private final CommunityMemberService communityMemberService;
    private final CommunityMemberRepository communityMemberRepository;

    @PostMapping("/{communityId}/members")
    public ResponseEntity<Void> joinCommunity(
            @PathVariable Integer communityId,
            @RequestParam Integer userId) {

        communityMemberService.joinCommunity(userId, communityId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{communityId}/members")
    public ResponseEntity<List<CommunityMemberListResponseDto>> getCommunityMembers(
            @PathVariable Integer communityId
    ) {
        List<CommunityMemberListResponseDto> members = communityMemberService.getCommunityMembers(communityId);

        return ResponseEntity.ok(members);
    }

    @GetMapping("/{communityId}/members/{userId}/exist")

    public ResponseEntity<Boolean> checkMembership(@PathVariable Integer communityId,
                                                   @PathVariable Integer userId) {

        boolean exists = communityMemberRepository.existsByCommunityIdAndUserId(communityId, userId);

        return ResponseEntity.ok(exists);
    }
}

