package com.jobdam.community.entity;

import com.jobdam.code.entity.BoardStatusCode;
import com.jobdam.code.entity.BoardTypeCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_comment")
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    private Integer communityCommentId;

    @Column(name = "community_post_id", nullable = false)
    private Integer communityPostId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "board_status_code_id", nullable = false)
    private Integer boardStatusCodeId;

    // user 객체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_status_code_id", insertable = false, updatable = false)
    private BoardStatusCode boardStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id", insertable = false, updatable = false)
    private CommunityPost communityPost;
}

