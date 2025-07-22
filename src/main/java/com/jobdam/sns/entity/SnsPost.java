package com.jobdam.sns.entity;

import com.jobdam.code.entity.BoardStatusCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import com.jobdam.sns.entity.Like;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter @Setter
@Entity
@Table(name = "sns_post")
public class SnsPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sns_post_id")
    private Integer snsPostId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "attachment_url", length = 255)
    private String attachmentUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "board_status_code_id", nullable = false)
    private Integer boardStatusCodeId;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    // user 객체
    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_status_code_id", insertable = false, updatable = false)
    private BoardStatusCode boardStatusCode;
}
