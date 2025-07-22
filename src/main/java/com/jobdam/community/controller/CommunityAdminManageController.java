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
        // 1. 파라미터 및 인증 체크
        if (communityId == null) {
            return ResponseEntity.badRequest().body("communityId required");
        }
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 2. 커뮤니티 정보
        Community community = communityRepository.findById(communityId)
                .orElse(null);
        if (community == null) {
            return ResponseEntity.status(404).body("커뮤니티를 찾을 수 없습니다.");
        }

        // 3. 소유자 체크
        if (!Objects.equals(community.getUserId(), user.getUserId())) {
            return ResponseEntity.status(403).body("접근 권한이 없습니다.");
        }

        // 4. 멤버 리스트
        List<CommunityMember> memberList = communityMemberRepository.findByCommunityId(communityId);
        List<Map<String, Object>> members = new ArrayList<>();
        for (CommunityMember m : memberList) {
            Map<String, Object> member = new HashMap<>();
            member.put("userId", m.getUserId());
            member.put("nickname", m.getUser() != null ? m.getUser().getNickname() : "");
            member.put("profileImageUrl", m.getUser() != null ? m.getUser().getProfileImageUrl() : "");
            member.put("role", m.getCommunityMemberRoleCode() != null ? m.getCommunityMemberRoleCode().getCode() : null);
            member.put("joinedAt", m.getJoinedAt());
            members.add(member);
        }




        // 5. 게시판 리스트
        List<CommunityBoard> boardList = communityBoardRepository.findAllByCommunityIdOrderByCreatedAtDesc(communityId);
        List<Map<String, Object>> boards = new ArrayList<>();
        for (CommunityBoard b : boardList) {
            Map<String, Object> board = new HashMap<>();
            board.put("boardId", b.getCommunityBoardId());
            board.put("name", b.getName());
            board.put("description", b.getDescription());
            board.put("type", b.getBoardTypeCode() != null ? b.getBoardTypeCode().getCode() : null);
            board.put("status", b.getBoardStatusCode() != null ? b.getBoardStatusCode().getCode() : null);
            boards.add(board);
        }

        // 6. 플랜 정보(구독 등급, 만료일 등)
        CommunitySubscription plan = communitySubscriptionRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getCommunityId(), communityId))
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .findFirst()
                .orElse(null);

        Map<String, Object> planMap = new LinkedHashMap<>();
        planMap.put("levelCode", community.getSubscriptionLevelCode() != null ? community.getSubscriptionLevelCode().getCode() : null);
        planMap.put("levelName", community.getSubscriptionLevelCode() != null ? community.getSubscriptionLevelCode().getName() : null);
        if (plan != null) {
            planMap.put("startDate", plan.getStartDate());
            planMap.put("endDate", plan.getEndDate());
            planMap.put("paidPoint", plan.getPaidPoint());
        }





        // 7. 전체 결과 조립
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("communityId", community.getCommunityId());
        result.put("name", community.getName());
        result.put("description", community.getDescription());
        result.put("ownerNickname", community.getUser() != null ? community.getUser().getNickname() : "");
        result.put("currentMember", community.getCurrentMember());
        result.put("maxMember", community.getMaxMember());
        result.put("enterPoint", community.getEnterPoint());
        result.put("subscriptionLevelCode", community.getSubscriptionLevelCode() != null ? community.getSubscriptionLevelCode().getCode() : null);
        result.put("profileImageUrl", community.getProfileImageUrl());

        result.put("members", members);
        result.put("boards", boards);
        result.put("plan", planMap);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/manage/kick/{targetUserId}")
    public ResponseEntity<?> kickMember(
            @PathVariable Integer communityId,
            @PathVariable Integer targetUserId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Community community = communityRepository.findById(communityId).orElse(null);
        if (community == null) return ResponseEntity.status(404).body("커뮤니티를 찾을 수 없습니다.");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        if (!Objects.equals(community.getUserId(), user.getUserId()))
            return ResponseEntity.status(403).body("OWNER만 강퇴 가능");

        if (Objects.equals(user.getUserId(), targetUserId))
            return ResponseEntity.badRequest().body("운영자는 자신을 강퇴할 수 없습니다.");

        CommunityMember targetMember = communityMemberRepository.findByCommunityIdAndUserId(communityId, targetUserId)
                .orElse(null);
        if (targetMember == null) return ResponseEntity.status(404).body("해당 멤버가 없습니다.");

        // 실제 삭제(강퇴)
        communityMemberRepository.delete(targetMember);

        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // currentMember 수동 감소
        int cur = community.getCurrentMember() != null ? community.getCurrentMember() : 0;
        community.setCurrentMember(Math.max(0, cur - 1));
        communityRepository.save(community);
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        return ResponseEntity.ok("success");
    }


    @DeleteMapping("/manage/board/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable Integer communityId,
            @PathVariable Integer boardId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Community community = communityRepository.findById(communityId).orElse(null);
        if (community == null) return ResponseEntity.status(404).body("커뮤니티를 찾을 수 없습니다.");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        if (!Objects.equals(community.getUserId(), user.getUserId()))
            return ResponseEntity.status(403).body("OWNER만 삭제 가능");

        CommunityBoard board = communityBoardRepository.findById(boardId).orElse(null);
        if (board == null || !Objects.equals(board.getCommunityId(), communityId)) {
            return ResponseEntity.status(404).body("해당 게시판을 찾을 수 없습니다.");
        }

        communityBoardRepository.delete(board);

        return ResponseEntity.ok("게시판이 삭제되었습니다.");
    }


    @PutMapping("/manage/board/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable Integer communityId,
            @PathVariable Integer boardId,
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Community community = communityRepository.findById(communityId).orElse(null);
        if (community == null) return ResponseEntity.status(404).body("커뮤니티를 찾을 수 없습니다.");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        if (!Objects.equals(community.getUserId(), user.getUserId()))
            return ResponseEntity.status(403).body("OWNER만 수정 가능");

        CommunityBoard board = communityBoardRepository.findById(boardId).orElse(null);
        if (board == null || !Objects.equals(board.getCommunityId(), communityId)) {
            return ResponseEntity.status(404).body("해당 게시판을 찾을 수 없습니다.");
        }

        if (req.containsKey("name")) board.setName(req.get("name"));
        if (req.containsKey("description")) board.setDescription(req.get("description"));
        communityBoardRepository.save(board);

        return ResponseEntity.ok("게시판이 수정되었습니다.");
    }
}
