package com.jobdam.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "sender_user_id", nullable = false)
    private Integer senderUserId;

    @Column(name = "receiver_user_id", nullable = false)
    private Integer receiverUserId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id", insertable = false, updatable = false)
    private User senderUser;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_user_id", insertable = false, updatable = false)
    private User receiverUser;
}
