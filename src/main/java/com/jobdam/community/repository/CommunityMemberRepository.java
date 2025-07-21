package com.jobdam.community.repository;

import com.jobdam.community.entity.CommunityComment;
import com.jobdam.community.entity.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Integer> {

    List<CommunityMember> findByCommunityId(Integer communityId);

    boolean existsByCommunityIdAndUserId(Integer communityId, Integer userId);
    boolean existsByUserIdAndCommunityId(Integer userId, Integer communityId);

    Integer countByCommunityId(Integer communityId);
}
