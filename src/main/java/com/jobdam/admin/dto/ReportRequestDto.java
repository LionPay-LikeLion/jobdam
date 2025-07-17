package com.jobdam.admin.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private Integer reportTypeCodeId; // 1: 게시글, 2: 댓글, 3: 유저
    private Long targetId;            // 신고 대상 (postId, commentId, userId 등)
    private Integer userId;           // 신고자
    private String reason;            // 신고 사유
}
