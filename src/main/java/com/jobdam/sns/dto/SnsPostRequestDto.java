package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnsPostRequestDto {

    // 게시글 작성/수정 시 클라이언트가 전달하는 데이터

    private String title;         // 게시글 제목
    private String content;       // 게시글 본문
    private String imageUrl;      // 이미지 (선택)
    private String attachmentUrl; // 첨부 파일 (선택)
}
