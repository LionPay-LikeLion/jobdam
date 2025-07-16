package com.jobdam.community.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostCreateRequestDto {

    private String title;
    private String content;
    private String postTypeCode;

}
