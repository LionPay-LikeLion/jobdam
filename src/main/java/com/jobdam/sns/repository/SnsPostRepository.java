package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


    public interface SnsPostRepository extends JpaRepository<SnsPost, Integer>, SnsPostCustomRepository {

    List<SnsPost> findByUserId(Integer userId);
    List<SnsPost> findByTitleContaining(String keyword);
    List<SnsPost> findAllByOrderByCreatedAtDesc();
    List<SnsPost> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    @Query("SELECT p FROM SnsPost p JOIN FETCH p.user u JOIN FETCH u.memberTypeCode ORDER BY p.createdAt DESC")
    List<SnsPost> findAllWithUserAndMemberTypeCode();

    @Query("SELECT p FROM SnsPost p JOIN FETCH p.user u JOIN FETCH u.memberTypeCode WHERE p.snsPostId = :postId")
    Optional<SnsPost> findByIdWithUserAndMemberTypeCode(@Param("postId") Integer postId);

    // 내 피드만 보기
    @Query("SELECT p FROM SnsPost p JOIN FETCH p.user u JOIN FETCH u.memberTypeCode WHERE u.userId = :userId ORDER BY p.createdAt DESC")
    List<SnsPost> findAllByUserIdWithUserAndMemberTypeCode(@Param("userId") Integer userId);

    @Query("""
        SELECT p FROM SnsPost p
        JOIN FETCH p.user u
        ORDER BY 
            CASE WHEN u.subscriptionLevelCodeId = 2 THEN 0 ELSE 1 END,
            p.createdAt DESC
        """)
        List<SnsPost> findAllOrderByPremiumFirst();

        @Query("""
            SELECT COUNT(p) FROM SnsPost p
            WHERE p.userId = :userId
            AND YEAR(p.createdAt) = YEAR(CURRENT_DATE)
            AND MONTH(p.createdAt) = MONTH(CURRENT_DATE)
            """)
        int countByUserIdInCurrentMonth(@Param("userId") Integer userId);


    }
