package com.jobdam.community.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunityCreateRequestDto {
    private String name;
    private String description;
    private Integer userId;
    private Integer enterPoint;
    private MultipartFile profileImage;
    //private Integer maxMember;
}

