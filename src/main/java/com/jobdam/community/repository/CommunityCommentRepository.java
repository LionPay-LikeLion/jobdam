package com.jobdam.community.repository;

import com.jobdam.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {

    int countByCommunityPostId(Integer postId);

    List<CommunityComment> findByUserIdOrderByCreatedAtDesc(Integer userId);

    List<CommunityComment> findByCommunityPostIdOrderByCreatedAtAsc(Integer postId);

}
