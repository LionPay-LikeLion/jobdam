package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsCommentRepository extends JpaRepository<SnsComment, Long> {

    // 특정 게시글에 달린 댓글 목록 조회
    List<SnsComment> findBySnsPostIdOrderByCreatedAtAsc(Integer snsPostId);

    // 특정 유저가 작성한 댓글 조회
    List<SnsComment> findByUserId(Integer userId);
}