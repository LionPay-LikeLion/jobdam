package com.jobdam.sns.service;

import com.jobdam.sns.dto.BookmarkResponseDto;

import java.util.List;

public interface BookmarkService {

    // 북마크 등록
    void addBookmark(Integer userId, Integer snsPostId);

    // 북마크 삭제
    void removeBookmark(Integer userId, Integer snsPostId);

    // 사용자의 북마크 전체 조회
    List<BookmarkResponseDto> getBookmarksByUser(Integer userId);
}
