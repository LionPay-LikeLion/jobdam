package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SnsPostRequestDto {

    private String title;
    private String content;

    private MultipartFile image;
    private MultipartFile attachment;
}

