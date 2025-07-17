package com.jobdam.user.repository;

import com.jobdam.subscription.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    // 필요 시 사용자 ID로 최근 구독 정보 가져오는 메서드 추가 가능
    UserSubscription findTopByUserIdOrderByStartDateDesc(Integer userId);
}
