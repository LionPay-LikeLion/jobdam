package com.jobdam.sns.repository;

import com.jobdam.sns.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 유저가 특정 게시글에 좋아요 눌렀는지 여부 확인
    Optional<Like> findByUserIdAndSnsPostId(Integer userId, Integer snsPostId);

    // 특정 게시글의 좋아요 개수
    Long countBySnsPostId(Integer snsPostId);

    // 특정 게시글의 모든 좋아요 삭제 (게시글 삭제 시)
    void deleteBySnsPostId(Integer snsPostId);
}
