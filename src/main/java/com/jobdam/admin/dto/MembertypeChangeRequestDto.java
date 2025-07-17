package com.jobdam.admin.dto;

import lombok.*;

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
    private String attachmentUrl;

}