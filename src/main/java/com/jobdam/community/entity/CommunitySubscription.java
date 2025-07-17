package com.jobdam.community.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_subscription")
public class CommunitySubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_subscription_id")
    private Integer communitySubscriptionId;

    @Column(name = "community_id", nullable = false)
    private Integer communityId;

    @Column(name = "subscription_level_code_id", nullable = false)
    private Integer subscriptionLevelCodeId;

    @Column(name = "paid_point", nullable = false)
    private Integer paidPoint;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "subscription_status_code_id", nullable = false)
    private Integer subscriptionStatusCodeId;
}
