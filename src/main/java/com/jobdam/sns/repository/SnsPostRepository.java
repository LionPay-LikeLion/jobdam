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

    @Query("SELECT p FROM SnsPost p JOIN FETCH p.user u JOIN FETCH u.memberTypeCode ORDER BY p.createdAt DESC")
    List<SnsPost> findAllWithUserAndMemberTypeCode();

    @Query("SELECT p FROM SnsPost p JOIN FETCH p.user u JOIN FETCH u.memberTypeCode WHERE p.snsPostId = :postId")
    Optional<SnsPost> findByIdWithUserAndMemberTypeCode(@Param("postId") Integer postId);

    // 내 피드만 보기
    @Query("SELECT p FROM SnsPost p JOIN FETCH p.user u JOIN FETCH u.memberTypeCode WHERE u.userId = :userId ORDER BY p.createdAt DESC")
    List<SnsPost> findAllByUserIdWithUserAndMemberTypeCode(@Param("userId") Integer userId);

}
