package com.jobdam.sns.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 북마크 생성/삭제 요청 시 사용하는 DTO
 * 필요한 최소 정보: snsPostId (북마크할 게시글 ID)
 */
@Getter
@Setter
public class BookmarkRequestDto {

    private Integer snsPostId;

}
