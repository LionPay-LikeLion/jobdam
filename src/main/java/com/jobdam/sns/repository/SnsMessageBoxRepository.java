package com.jobdam.sns.repository;

import com.jobdam.sns.dto.SnsMessageBoxResponseDto;
import com.jobdam.sns.entity.SnsMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsMessageBoxRepository extends JpaRepository<SnsMessage, Long> {

    @Query("""
    SELECT new com.jobdam.sns.dto.SnsMessageBoxResponseDto(
        CAST(CASE WHEN m.senderId = :userId THEN m.receiver.userId ELSE m.sender.userId END AS long),
        CASE WHEN m.senderId = :userId THEN m.receiver.nickname ELSE m.sender.nickname END,
        CASE WHEN m.senderId = :userId THEN m.receiver.profileImageUrl ELSE m.sender.profileImageUrl END,
        m.content,
        m.createdAt
    )
    FROM SnsMessage m
    WHERE m.createdAt IN (
        SELECT MAX(m2.createdAt)
        FROM SnsMessage m2
        WHERE m2.senderId = :userId OR m2.receiverId = :userId
        GROUP BY
            CASE
                WHEN m2.senderId = :userId THEN m2.receiverId
                ELSE m2.senderId
            END
    )
    AND (m.senderId = :userId OR m.receiverId = :userId)
    ORDER BY m.createdAt DESC
""")
    List<SnsMessageBoxResponseDto> findMessageBoxesByUserId(@Param("userId") Long userId);

}
