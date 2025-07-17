package com.jobdam.community.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityCreateRequestDto {
    private String name;
    private String description;
    private Integer userId;
    private Integer enterPoint;
    //private Integer maxMember;
}

