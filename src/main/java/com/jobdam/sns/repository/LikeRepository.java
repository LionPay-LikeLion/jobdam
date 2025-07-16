package com.jobdam.sns.repository;

import com.jobdam.sns.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByUserIdAndSnsPostId(Integer userId, Integer snsPostId);
    Integer countBySnsPostId(Integer snsPostId);
    void deleteBySnsPostId(Integer snsPostId);

    boolean existsByUserIdAndSnsPostId(Integer userId, Integer snsPostId);
}
