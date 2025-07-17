package com.jobdam.sns.repository;

import com.jobdam.sns.dto.SnsPostFilterDto;
import com.jobdam.sns.entity.SnsPost;

import java.util.List;

public interface SnsPostCustomRepository {

    /**
     * 작성자 유형 및 정렬 기준에 따라 SNS 게시글 필터링 조회
     * @param filter 필터 조건 (작성자 유형, 정렬 기준)
     * @return 필터링된 SNS 게시글 리스트
     */
    List<SnsPost> searchFilteredPosts(SnsPostFilterDto filter);
}
