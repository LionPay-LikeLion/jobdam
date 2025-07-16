package com.jobdam.community.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityBoardListResponseDto {

    private Integer communityBoardId;
    private String name;
    private String description;
    private String boardTypeCode;
    private String boardStatusCode;

}
