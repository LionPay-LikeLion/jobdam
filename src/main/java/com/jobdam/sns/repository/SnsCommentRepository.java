package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsCommentRepository extends JpaRepository<SnsComment, Integer> {

    List<SnsComment> findBySnsPostIdOrderByCreatedAtAsc(Integer snsPostId);
    List<SnsComment> findByUserId(Integer userId);
    Integer countBySnsPostId(Integer snsPostId);

    List<SnsComment> findBySnsPostId(Integer snsPostId);
}