package com.jobdam.admin.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembertypeChangeRequestDto {

    private String requestedMemberTypeCode;
    private String title;
    private String reason;
    private String content;
    private String referenceLink;

    private MultipartFile attachment;

}
