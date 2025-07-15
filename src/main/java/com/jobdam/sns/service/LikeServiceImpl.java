package com.jobdam.sns.service;

import com.jobdam.sns.entity.Like;
import com.jobdam.sns.repository.LikeRepository;
import com.jobdam.user.repository.UserRepository;
import com.jobdam.sns.repository.SnsPostRepository;
import com.jobdam.sns.entity.User;
import com.jobdam.sns.entity.SnsPost;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final SnsPostRepository snsPostRepository;

    @Override
    @Transactional
    public void addLike(Integer userId, Integer snsPostId) {
        if (likeRepository.existsByUserIdAndSnsPostId(userId, snsPostId)) {
            throw new RuntimeException("이미 좋아요를 누른 게시글입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        SnsPost post = snsPostRepository.findById(snsPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Like like = new Like();
        like.setUserId(userId);
        like.setSnsPostId(snsPostId);

        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void removeLike(Integer userId, Integer snsPostId) {
        Like like = likeRepository.findByUserIdAndSnsPostId(userId, snsPostId)
                .orElseThrow(() -> new RuntimeException("좋아요 정보가 없습니다."));

        likeRepository.delete(like);
    }

    @Override
    public int countLikes(Integer snsPostId) {
        return likeRepository.countBySnsPostId(snsPostId);
    }

    @Override
    public boolean isLiked(Integer userId, Integer snsPostId) {
        return likeRepository.existsByUserIdAndSnsPostId(userId, snsPostId);
    }
}
