package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SnsMessageRepository extends JpaRepository<SnsMessage, Long> {

List<SnsMessage> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);

    @Query("""
        SELECT m FROM SnsMessage m
        JOIN FETCH m.sender
        WHERE (m.senderId = :senderId1 AND m.receiverId = :receiverId1)
           OR (m.senderId = :senderId2 AND m.receiverId = :receiverId2)
        ORDER BY m.createdAt
    """)
    List<SnsMessage> findMessagesWithSenderFetched(
            @Param("senderId1") Integer senderId1,
            @Param("receiverId1") Integer receiverId1,
            @Param("senderId2") Integer senderId2,
            @Param("receiverId2") Integer receiverId2
    );
}
