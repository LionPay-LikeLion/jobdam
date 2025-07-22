package com.jobdam.community.repository;

import com.jobdam.community.entity.CommunitySubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunitySubscriptionRepository extends JpaRepository<CommunitySubscription, Integer> {
    // 필요시 커뮤니티별 구독 기록 조회 등을 위한 메서드 추가 가능
    // CommunitySubscriptionRepository.java
    Optional<CommunitySubscription> findTop1ByCommunityIdOrderByStartDateDesc(Integer communityId);

}
