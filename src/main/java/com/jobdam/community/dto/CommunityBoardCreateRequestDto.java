package com.jobdam.community.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityBoardCreateRequestDto {

    private String name;
    private String description;
    private Integer boardTypeCodeId;

}
