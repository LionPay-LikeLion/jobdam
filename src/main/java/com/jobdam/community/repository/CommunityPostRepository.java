package com.jobdam.community.repository;


import com.jobdam.community.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.jobdam.community.dto.MyCommunityPostResponseDto;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Integer> {


    List<CommunityPost> findByCommunityBoardIdOrderByCreatedAtDesc(Integer boardId);


    List<CommunityPost> findByCommunityBoardIdAndPostTypeCodeCodeOrderByCreatedAtDesc(Integer boardId, String postTypeCode);


    @Query("SELECT p FROM CommunityPost p WHERE p.communityBoardId = :boardId AND " +
            "(p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createdAt DESC")
    List<CommunityPost> findByCommunityBoardIdAndKeyword(@Param("boardId") Integer boardId, @Param("keyword") String keyword);


    @Query("SELECT p FROM CommunityPost p WHERE p.communityBoardId = :boardId AND p.postTypeCodeId = :postTypeCode " +
            "AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createdAt DESC")
    List<CommunityPost> findByCommunityBoardIdAndPostTypeCodeCodeAndKeyword(

            @Param("boardId") Integer boardId,
            @Param("postTypeCode") String postTypeCode,
            @Param("keyword") String keyword

    );


}

