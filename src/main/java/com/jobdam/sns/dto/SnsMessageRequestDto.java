package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnsMessageRequestDto {
    private Long receiverId;
    private String content;
}
