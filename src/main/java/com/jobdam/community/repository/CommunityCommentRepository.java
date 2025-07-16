package com.jobdam.community.repository;

import com.jobdam.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {

    int countByCommunityPostId(Integer postId);

}
