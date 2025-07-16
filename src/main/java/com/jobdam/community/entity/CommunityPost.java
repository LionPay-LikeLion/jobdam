package com.jobdam.community.entity;

import com.jobdam.code.entity.PostTypeCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_post")
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_post_id")
    private Integer communityPostId;

    @Column(name = "community_board_id", nullable = false)
    private Integer communityBoardId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "post_type_code_id", nullable = false)
    private Integer postTypeCodeId;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "board_status_code_id", nullable = false)
    private Integer boardStatusCodeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_type_code_id", insertable = false, updatable = false)
    private PostTypeCode postTypeCode;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
