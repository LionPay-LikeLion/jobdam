package com.jobdam.admin.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReportResponseDto {
    private Integer reportId;
    private Integer reportTypeCodeId;
    private String reportTypeName; // (코드 join시)
    private Long targetId;
    private Integer userId;
    private String reporterEmail;
    private String reporterNickname;
    private String reportedNickname;    // 피신고자 닉네임 (target_id → 게시글/댓글 → user → 닉네임)
    private String reason;
    private LocalDateTime createdAt;
    private Integer status; // 처리 상태 추가
    private LocalDateTime processedAt; // 처리 일시 추가
}
