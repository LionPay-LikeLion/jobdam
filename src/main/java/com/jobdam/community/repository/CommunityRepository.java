package com.jobdam.community.repository;

import com.jobdam.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

    List<Community> findAllByOrderByCurrentMemberDesc();
}

