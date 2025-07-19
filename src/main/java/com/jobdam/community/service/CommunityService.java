package com.jobdam.community.service;

import com.jobdam.code.entity.SubscriptionLevelCode;
import com.jobdam.community.dto.*;
import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityBoard;
import com.jobdam.community.entity.CommunityMember;
import com.jobdam.community.repository.CommunityBoardRepository;
import com.jobdam.community.repository.CommunityMemberRepository;
import com.jobdam.community.repository.CommunityRepository;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.jobdam.community.entity.CommunitySubscription;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.repository.PaymentRepository;
import com.jobdam.community.repository.CommunitySubscriptionRepository;


@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final PaymentRepository paymentRepository;
    private final CommunitySubscriptionRepository communitySubscriptionRepository;
    private final CommunityBoardRepository communityBoardRepository;


    private static final int PAYMENT_TYPE_COMMUNITY_JOIN = 2;  // 커뮤니티 가입
    private static final int PAYMENT_SUCCESS_ID = 1;

    @Transactional
    public Integer createCommunity(CommunityCreateRequestDto dto, Integer userId) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        if (!user.getSubscriptionLevelCodeId().equals(2)) {
            throw new RuntimeException("구독 레벨이 2여야 커뮤니티를 생성할 수 있습니다.");
        }

        String imageUrl = null;
        if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + "community" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + dto.getProfileImage().getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            try {
                dto.getProfileImage().transferTo(dest);
                imageUrl = "/uploads/community/" + userId + "/" + fileName;
            } catch (IOException | IllegalStateException e) {
                // 예외 로그 출력 (실제 운영환경에서는 log.error로 남기기)
                e.printStackTrace();
                // 사용자에게 알릴 수 있도록 런타임 예외 던지기
                throw new RuntimeException("프로필 이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }


        Community community = new Community();
        community.setName(dto.getName());
        community.setDescription(dto.getDescription());
        community.setSubscriptionLevelCodeId(1);
        community.setUserId(userId);
        community.setEnterPoint(dto.getEnterPoint());
        community.setCurrentMember(1);
        community.setMaxMember(30);
        community.setProfileImageUrl(imageUrl);

        communityRepository.save(community);

        CommunityMember member = CommunityMember.builder()
                .communityId(community.getCommunityId())
                .userId(userId)
                .joinedAt(LocalDateTime.now())
                .paidPoint(0)
                .communityMemberRoleCodeId(1) // 운영자
                .build();

        communityMemberRepository.save(member);

        return community.getCommunityId();
    }


    @Transactional
    public void upgradeCommunityToPremium(Integer userId, CommunityUpgradeRequestDto dto) {
        Community community = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new RuntimeException("커뮤니티를 찾을 수 없습니다."));

        if (!community.getUserId().equals(userId)) {
            throw new RuntimeException("커뮤니티 생성자만 업그레이드할 수 있습니다.");
        }
        if (community.getSubscriptionLevelCodeId() == 2) {
            throw new RuntimeException("이미 프리미엄 커뮤니티입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        int price = dto.getPlanType().equalsIgnoreCase("YEARLY") ? 100000 : 10000;

        if (user.getPoint() < price) {
            throw new RuntimeException("포인트가 부족합니다.");
        }

        // 1. 포인트 차감
        user.setPoint(user.getPoint() - price);
        userRepository.save(user);

        // 2. 커뮤니티 구독 정보 저장 (프리미엄)
        CommunitySubscription subscription = new CommunitySubscription();
        subscription.setCommunityId(community.getCommunityId());
        subscription.setSubscriptionLevelCodeId(2); // PREMIUM
        subscription.setPaidPoint(price);
        subscription.setStartDate(LocalDateTime.now());

        // 종료일 계산
        if (dto.getPlanType().equalsIgnoreCase("YEARLY")) {
            subscription.setEndDate(LocalDateTime.now().plusYears(1));
        } else {
            subscription.setEndDate(LocalDateTime.now().plusMonths(1));
        }

        subscription.setSubscriptionStatusCodeId(1); // ACTIVE
        communitySubscriptionRepository.save(subscription);

        // 3. 커뮤니티 등급 업데이트
        community.setSubscriptionLevelCodeId(2); // PREMIUM
        community.setMaxMember(100); // 프리미엄 한도 반영
        communityRepository.save(community);

        // 4. 결제 기록 (포인트 차감 로그)
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setPoint(-price);
        payment.setPaymentTypeCodeId(4); // COMMUNITY_GRADE_UPGRADE
        payment.setPaymentStatusCodeId(1); // SUCCESS
        payment.setMethod("POINT");
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }


    @Transactional
    public void joinCommunity(Integer userId, Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("커뮤니티를 찾을 수 없습니다."));

        if (community.getCurrentMember() >= community.getMaxMember()) {
            throw new RuntimeException("가입 인원이 가득 찼습니다.");
        }

        if (communityMemberRepository.existsByUserIdAndCommunityId(userId, communityId)) {
            throw new RuntimeException("이미 가입한 커뮤니티입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        int enterPoint = community.getEnterPoint() != null ? community.getEnterPoint() : 0;

        // 포인트 차감 체크
        if (user.getPoint() < enterPoint) {
            throw new RuntimeException("포인트가 부족합니다.");
        }

        // 포인트 차감
        user.setPoint(user.getPoint() - enterPoint);
        userRepository.save(user);

        Payment payment = Payment.builder()
                .userId(userId)
                .point(-enterPoint)
                .amount(0)
                .paymentTypeCodeId(PAYMENT_TYPE_COMMUNITY_JOIN) // 예: 6번 = COMMUNITY_JOIN
                .paymentStatusCodeId(PAYMENT_SUCCESS_ID)       // 예: 1번 = SUCCESS
                .method("POINT")
                .impUid("system-" + System.currentTimeMillis())
                .merchantUid("community-join-" + userId + "-" + System.currentTimeMillis())
                .build();
        paymentRepository.save(payment);

        CommunityMember member = CommunityMember.builder()
                .communityId(communityId)
                .userId(userId)
                .joinedAt(LocalDateTime.now())
                .paidPoint(0)
                .communityMemberRoleCodeId(2) // 일반 멤버
                .build();

        communityMemberRepository.save(member);

        community.setCurrentMember(community.getCurrentMember() + 1);
        communityRepository.save(community);
    }

    @Transactional
    public List<CommunityListResponseDto> getAllCommunities() {

        List<Community> communities = communityRepository.findAllByOrderBySubscriptionLevelCodeIdDescCurrentMemberDesc();

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
                        .profileImageUrl(c.getProfileImageUrl())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommunityListResponseDto> getCommunitiesByUserId(Integer userId) {
        List<Community> communities = communityRepository.findByUserId(userId);

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
                        .profileImageUrl(c.getProfileImageUrl())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommunityDetailResponseDto getCommunityDetail(Integer communityId) {
        // 1. 커뮤니티 기본 정보 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("커뮤니티를 찾을 수 없습니다."));

        String ownerNickname = community.getUser().getNickname();


        String subscriptionLevelCode = community.getSubscriptionLevelCode().getCode();


        Integer currentBoard = communityBoardRepository.countByCommunityId(communityId);


        List<CommunityBoard> popularBoards = communityBoardRepository
                .findTop3ByCommunityIdOrderByCreatedAtDesc(communityId);
        List<CommunityBoardListResponseDto> popularBoardsDto = popularBoards.stream()
                .map(board -> CommunityBoardListResponseDto.builder()
                        .communityBoardId(board.getCommunityBoardId())
                        .name(board.getName())
                        .description(board.getDescription())
                        .boardTypeCode(board.getBoardTypeCode().getCode())
                        .boardStatusCode(board.getBoardStatusCode().getCode())
                        .build()
                )
                .toList();

        return CommunityDetailResponseDto.builder()
                .communityId(community.getCommunityId().longValue())
                .name(community.getName())
                .description(community.getDescription())
                .profileImageUrl(community.getProfileImageUrl())
                .subscriptionLevelCode(subscriptionLevelCode)
                .ownerNickname(ownerNickname)
                .currentMember(community.getCurrentMember())
                .currentBoard(currentBoard)
                .enterPoint(community.getEnterPoint())
                .popularBoards(popularBoardsDto)
                .build();

    }


}

