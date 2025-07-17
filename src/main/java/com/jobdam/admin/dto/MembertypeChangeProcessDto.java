package com.jobdam.admin.dto;

import lombok.Data;

@Data
public class MembertypeChangeProcessDto {
    private String statusCode; // 예: "APPROVED", "REJECTED"
}
