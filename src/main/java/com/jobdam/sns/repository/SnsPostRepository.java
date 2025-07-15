package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsPostRepository extends JpaRepository<SnsPost, Long> {

    List<SnsPost> findByUserId(Integer userId);
    List<SnsPost> findByTitleContaining(String keyword);
    List<SnsPost> findAllByOrderByCreatedAtDesc();
}
