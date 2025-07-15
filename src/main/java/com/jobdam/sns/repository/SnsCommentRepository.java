package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsCommentRepository extends JpaRepository<SnsComment, Long> {

    List<SnsComment> findBySnsPostIdOrderByCreatedAtAsc(Integer snsPostId);
    List<SnsComment> findByUserId(Integer userId);
}