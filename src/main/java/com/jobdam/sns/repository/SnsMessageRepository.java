package com.jobdam.sns.repository;

import com.jobdam.sns.entity.SnsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsMessageRepository extends JpaRepository<SnsMessage, Long> {

    List<SnsMessage> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);

    List<SnsMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAt(
            Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
    );
}
