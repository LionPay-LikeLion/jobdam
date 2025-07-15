package com.jobdam.community.entity;

import com.jobdam.code.domain.SubscriptionLevelCode;
import com.jobdam.code.domain.SubscriptionStatusCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "community_subscription")
public class CommunitySubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communitySubscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private SubscriptionLevelCode level;

    @Column(nullable = false)
    private Integer paidPoint;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private SubscriptionStatusCode status;
}

