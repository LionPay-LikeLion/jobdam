package com.jobdam.community.repository;

import com.jobdam.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

    List<Community> findAllByOrderByCurrentMemberDesc();
    @Query("SELECT c FROM Community c ORDER BY c.subscriptionLevelCodeId DESC, c.currentMember DESC")
    List<Community> findAllOrderedByPremiumAndMember();

    List<Community> findByUserId(Integer userId);
    // 프리미엄 → 기본 순 정렬 + 현재 멤버 많은 순
    List<Community> findAllByOrderBySubscriptionLevelCodeIdDescCurrentMemberDesc();

}

