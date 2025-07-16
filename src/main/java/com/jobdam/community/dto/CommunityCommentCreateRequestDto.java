package com.jobdam.community.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentCreateRequestDto {

    private String content;

}
