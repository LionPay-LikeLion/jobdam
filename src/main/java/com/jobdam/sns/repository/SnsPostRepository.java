package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsPostRepository extends JpaRepository<SnsPost, Long> {

    // 특정 유저의 모든 게시글 조회
    List<SnsPost> findByUserId(Integer userId);

    // 제목 키워드로 게시글 검색
    List<SnsPost> findByTitleContaining(String keyword);

    // 최신순으로 모든 게시글 조회
    List<SnsPost> findAllByOrderByCreatedAtDesc();
}
