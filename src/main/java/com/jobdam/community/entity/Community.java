package com.jobdam.community.entity;


import com.jobdam.code.domain.SubscriptionLevelCode;
import com.jobdam.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "community")
public class Community {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_level_id", nullable = false)
    private SubscriptionLevelCode subscriptionLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column
    private Integer enterPoint;

    @Column
    private Integer maxMember;

    @Column
    private Integer currentMember;
}
