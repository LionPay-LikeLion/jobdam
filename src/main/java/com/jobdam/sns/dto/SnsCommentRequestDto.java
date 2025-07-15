package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnsCommentRequestDto {


    private Integer snsPostId;
    private String content;
}
