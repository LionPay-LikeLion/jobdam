package com.jobdam.sns.entity;

import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "bookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Integer bookmarkId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "sns_post_id", nullable = false)
    private Integer snsPostId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // user 객체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
