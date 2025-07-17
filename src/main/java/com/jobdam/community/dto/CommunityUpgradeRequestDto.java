package com.jobdam.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityUpgradeRequestDto {
    private Integer communityId;
    private String planType; // "MONTHLY" or "YEARLY"
}
