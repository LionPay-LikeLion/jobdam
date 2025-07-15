package com.jobdam.community.entity;

import com.jobdam.code.domain.BoardStatusCode;
import com.jobdam.code.domain.BoardTypeCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(name = "community_board")
public class CommunityBoard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityBoardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private BoardTypeCode type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private BoardStatusCode status;

    @Column
    private java.sql.Timestamp createdAt;

    @Column
    private java.sql.Timestamp updatedAt;
}

