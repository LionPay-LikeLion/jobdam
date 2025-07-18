package com.jobdam.community.service;

import com.jobdam.community.dto.CommunityMemberListResponseDto;
import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityMember;
import com.jobdam.community.repository.CommunityMemberRepository;
import com.jobdam.community.repository.CommunityRepository;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityMemberService {


    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final CommunityMemberRepository communityMemberRepository;

    @Transactional
    public void joinCommunity(Integer userId, Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("커뮤니티 없음"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        if (communityMemberRepository.existsByCommunityIdAndUserId(communityId, userId)) {
            throw new RuntimeException("이미 가입된 회원");
        }

        int enterPoint = community.getEnterPoint();

        int userPoint = user.getPoint();

        if (userPoint < enterPoint) {
            throw new RuntimeException("포인트 부족");
        }


        user.setPoint(userPoint - enterPoint);
        userRepository.save(user);


        CommunityMember member = CommunityMember.builder()
                .communityId(communityId)
                .userId(userId)
                .joinedAt(LocalDateTime.now())
                .paidPoint(enterPoint)
                .communityMemberRoleCodeId(2)
                .build();

        communityMemberRepository.save(member);

        community.setCurrentMember(community.getCurrentMember() + 1);
        communityRepository.save(community);


    }

    @Transactional(readOnly = true)
    public List<CommunityMemberListResponseDto> getCommunityMembers(Integer communityId) {

        List<CommunityMember> members = communityMemberRepository.findByCommunityId(communityId);

        return members.stream()
                .map(member -> {
                    User user = member.getUser();

                    return CommunityMemberListResponseDto.builder()
                            .userId(user.getUserId())
                            .nickname(user.getNickname())
                            .joinedAt(member.getJoinedAt())
                            .profileImageUrl(user.getProfileImageUrl())
                            .communityMemberRoleCode(
                                    member.getCommunityMemberRoleCode() != null
                                            ? member.getCommunityMemberRoleCode().getCode()
                                            : null
                            )
                            .memberTypeCode(
                                    user.getMemberTypeCode() != null
                                            ? user.getMemberTypeCode().getCode()
                                            : null
                            )
                            .build();
                })
                .toList();
    }
}
