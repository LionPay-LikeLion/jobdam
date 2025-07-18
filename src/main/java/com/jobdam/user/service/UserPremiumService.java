package com.jobdam.user.service;

import com.jobdam.subscription.domain.UserSubscription;
import com.jobdam.user.repository.UserSubscriptionRepository;
import com.jobdam.user.dto.UserPremiumRequest;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserPremiumService {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PaymentRepository paymentRepository;

    private static final int PREMIUM_LEVEL_ID = 2;               // 프리미엄 구독 코드 ID
    private static final int ACTIVE_STATUS_ID = 1;               // 구독 상태: 활성
    private static final int USER_GRADE_UPGRADE_ID = 3;          // 결제 타입: 유저 등급 업그레이드
    private static final int PAYMENT_SUCCESS_ID = 1;             // 결제 상태: 성공

    @Transactional
    public void upgradeToPremium(UserPremiumRequest request, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        int price = request.getPlanType().equalsIgnoreCase("YEARLY") ? 99000 : 9900;
        if (user.getPoint() < price) {
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");
        }

        // 포인트 차감 및 유저 구독 등급 변경
        user.setPoint(user.getPoint() - price);
        user.setSubscriptionLevelCodeId(PREMIUM_LEVEL_ID);
        userRepository.save(user);

        // 구독 내역 저장
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = request.getPlanType().equalsIgnoreCase("YEARLY") ? now.plusYears(1) : now.plusMonths(1);

        UserSubscription subscription = new UserSubscription();
        subscription.setUserId(userId);
        subscription.setPaidPoint(price);
        subscription.setStartDate(now);
        subscription.setEndDate(end);
        subscription.setSubscriptionLevelCodeId(PREMIUM_LEVEL_ID);
        subscription.setSubscriptionStatusCodeId(ACTIVE_STATUS_ID);
        userSubscriptionRepository.save(subscription);

        // 결제 내역 저장
        Payment payment = Payment.builder()
                .userId(userId)
                .point(-price)
                .amount(0)
                .paymentTypeCodeId(USER_GRADE_UPGRADE_ID)
                .paymentStatusCodeId(PAYMENT_SUCCESS_ID)
                .method("POINT")
                .impUid("system-" + System.currentTimeMillis())
                .merchantUid("upgrade-" + userId + "-" + System.currentTimeMillis())
                .build();
        paymentRepository.save(payment);
    }
}
