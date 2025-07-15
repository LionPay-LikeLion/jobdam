package com.jobdam.sns.entity;

import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "sns_post")
public class SnsPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer snsPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 255)
    private String attachmentUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
