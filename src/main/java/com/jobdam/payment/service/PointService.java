package com.jobdam.payment.service;

import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;

    // 결제 시 포인트 적립
    @Transactional
    public void addPoint(Integer userId, int point) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        int current = (user.getPoint() == null ? 0 : user.getPoint());
        user.setPoint(current + point);
        userRepository.save(user);
    }

    // 환불 시 포인트 차감
    @Transactional
    public void subtractPoint(Integer userId, int point) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        int current = (user.getPoint() == null ? 0 : user.getPoint());
        if (current < point) throw new IllegalArgumentException("환불할 포인트 부족");
        user.setPoint(current - point);
        userRepository.save(user);
    }

    public int getUserPoint(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        return user.getPoint() == null ? 0 : user.getPoint();
    }

}
