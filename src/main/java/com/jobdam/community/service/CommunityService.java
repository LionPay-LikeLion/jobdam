package com.jobdam.community.service;

import com.jobdam.code.entity.SubscriptionLevelCode;
import com.jobdam.community.dto.CommunityCreateRequestDto;
import com.jobdam.community.dto.CommunityListResponseDto;
import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityMember;
import com.jobdam.community.repository.CommunityMemberRepository;
import com.jobdam.community.repository.CommunityRepository;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;
    private final CommunityMemberRepository communityMemberRepository;


    @Transactional
    public Integer createCommunity(CommunityCreateRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        if (!user.getSubscriptionLevelCodeId().equals(2)) {
            throw new RuntimeException("구독 레벨이 2여야 커뮤니티를 생성할 수 있습니다.");
        }

        Community community = new Community();

        community.setName(dto.getName());
        community.setDescription(dto.getDescription());
        community.setSubscriptionLevelCodeId(1);
        community.setUserId(dto.getUserId());
        community.setEnterPoint(dto.getEnterPoint());
        community.setMaxMember(dto.getMaxMember());
        community.setCurrentMember(1);


        communityRepository.save(community);

        CommunityMember member = CommunityMember.builder()
                .communityId(community.getCommunityId())
                .userId(dto.getUserId())
                .joinedAt(LocalDateTime.now())
                .paidPoint(0) // 만든 사람은 입장 포인트 없음
                .communityMemberRoleCodeId(1) // 1번이 관리자라면
                .build();

        communityMemberRepository.save(member);

        return community.getCommunityId();
    }

    @Transactional
    public List<CommunityListResponseDto> getAllCommunities() {

        List<Community> communities = communityRepository.findAllByOrderByCurrentMemberDesc();

        return communities.stream()
                .map(c -> CommunityListResponseDto.builder()
                        .communityId(c.getCommunityId())
                        .name(c.getName())
                        .description(c.getDescription())
                        .subscriptionLevelCode(
                                subscriptionLevelCodeRepository.findById(c.getSubscriptionLevelCodeId())
                                        .map(SubscriptionLevelCode::getCode)
                                        .orElse(null)
                        )
                        .ownerNickname(c.getUser().getNickname())
                        .maxMember(c.getMaxMember())
                        .currentMember(c.getCurrentMember())
                        .enterPoint(c.getEnterPoint())
                        .build()
                )
                .collect(Collectors.toList());
    }




}

