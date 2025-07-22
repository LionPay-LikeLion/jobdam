package com.jobdam.community.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentListResponseDto {

    private Integer commentId;
    private Integer postId;
    private String content;
    private String userNickname;
    private LocalDateTime createdAt;
    private String boardStatusCode;


}
