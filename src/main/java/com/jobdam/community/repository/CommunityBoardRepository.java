package com.jobdam.community.repository;

import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Integer> {

    List<CommunityBoard> findAllByCommunityIdOrderByCreatedAtDesc(Integer communityId);
    int countByCommunityId(Integer communityId);

    List<CommunityBoard> findTop3ByCommunityIdOrderByCreatedAtDesc(Integer communityId);
}

