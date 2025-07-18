package com.jobdam.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPremiumRequest {
    // 업그레이드할 유저 ID
    private String planType;     // "MONTHLY" 또는 "YEARLY"

}
