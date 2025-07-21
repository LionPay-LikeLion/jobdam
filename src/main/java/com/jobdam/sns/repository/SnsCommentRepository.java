package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobdam.sns.dto.MySnsCommentResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SnsCommentRepository extends JpaRepository<SnsComment, Integer> {

    List<SnsComment> findBySnsPostIdOrderByCreatedAtAsc(Integer snsPostId);
    List<SnsComment> findByUserId(Integer userId);
    Integer countBySnsPostId(Integer snsPostId);

    List<SnsComment> findBySnsPostId(Integer snsPostId);

    @Query("""
    SELECT new com.jobdam.sns.dto.MySnsCommentResponseDto(
        c.snsCommentId,
        c.snsPost.snsPostId,
        c.content,
        c.createdAt,
        c.updatedAt
    )
    FROM SnsComment c
    WHERE c.user.userId = :userId
    ORDER BY c.createdAt DESC
""")
    List<MySnsCommentResponseDto> findMySnsComments(@Param("userId") Integer userId);

}