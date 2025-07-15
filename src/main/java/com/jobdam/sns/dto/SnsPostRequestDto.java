package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnsPostRequestDto {


    private String title;
    private String content;
    private String imageUrl;
    private String attachmentUrl;
}
