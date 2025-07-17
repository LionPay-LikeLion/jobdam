package com.jobdam.admin.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private Integer reportTypeCodeId;
    private Long targetId;
    private Integer userId;
    private String reason;
}
