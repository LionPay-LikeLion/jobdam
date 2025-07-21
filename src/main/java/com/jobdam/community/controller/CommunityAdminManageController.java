package com.jobdam.community.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityBoard;
import com.jobdam.community.entity.CommunityMember;
import com.jobdam.community.entity.CommunitySubscription;
import com.jobdam.community.repository.CommunityBoardRepository;
import com.jobdam.community.repository.CommunityMemberRepository;
import com.jobdam.community.repository.CommunityRepository;
import com.jobdam.community.repository.CommunitySubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/admin")
public class CommunityAdminManageController {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final CommunityBoardRepository communityBoardRepository;
    private final CommunitySubscriptionRepository communitySubscriptionRepository;

    @GetMapping("/manage")
    public ResponseEntity<?> getAdminManagePage(
            @PathVariable Integer communityId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        // 1. 커뮤니티 기본정보 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("커뮤니티를 찾을 수 없습니다."));

        // 2. 소유자 체크
        if (!community.getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).body("접근 권한이 없습니다.");
        }

        // 3. 멤버 리스트 (강제퇴장/rejoin 불가 등 플래그는 프론트에서 처리)
        List<CommunityMember> memberList = communityMemberRepository.findByCommunityId(communityId);

        // 4. 게시판 리스트
        List<CommunityBoard> boardList = communityBoardRepository.findAllByCommunityIdOrderByCreatedAtDesc(communityId);

        // 5. 플랜 정보 (최근 프리미엄 구독)
        CommunitySubscription plan = communitySubscriptionRepository.findAll().stream()
                .filter(s -> s.getCommunityId().equals(communityId))
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .findFirst()
                .orElse(null);

        // 6. 데이터 조합
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("communityId", community.getCommunityId());
        result.put("name", community.getName());
        result.put("description", community.getDescription());
        result.put("ownerNickname", community.getUser().getNickname());
        result.put("currentMember", community.getCurrentMember());
        result.put("maxMember", community.getMaxMember());
        result.put("enterPoint", community.getEnterPoint());
        result.put("subscriptionLevelCode", community.getSubscriptionLevelCode().getCode());
        result.put("profileImageUrl", community.getProfileImageUrl());

        // 멤버 리스트 뽑기
        result.put("members", memberList.stream().map(m -> Map.of(
                "userId", m.getUserId(),
                "nickname", m.getUser().getNickname(),
                "profileImageUrl", m.getUser().getProfileImageUrl(),
                "role", m.getCommunityMemberRoleCode().getCode(),
                "joinedAt", m.getJoinedAt()
        )).toList());

        // 게시판 리스트 뽑기
        result.put("boards", boardList.stream().map(b -> Map.of(
                "boardId", b.getCommunityBoardId(),
                "name", b.getName(),
                "description", b.getDescription(),
                "type", b.getBoardTypeCode().getCode(),
                "status", b.getBoardStatusCode().getCode()
        )).toList());

        // 플랜 정보 (구독등급/만료일 등)
        if (plan != null) {
            result.put("plan", Map.of(
                    "levelCode", community.getSubscriptionLevelCode().getCode(),
                    "levelName", community.getSubscriptionLevelCode().getName(),
                    "startDate", plan.getStartDate(),
                    "endDate", plan.getEndDate(),
                    "paidPoint", plan.getPaidPoint()
            ));
        } else {
            result.put("plan", Map.of(
                    "levelCode", community.getSubscriptionLevelCode().getCode(),
                    "levelName", community.getSubscriptionLevelCode().getName()
            ));
        }

        return ResponseEntity.ok(result);
    }
}
