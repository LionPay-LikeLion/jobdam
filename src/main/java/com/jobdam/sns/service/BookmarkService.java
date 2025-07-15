package com.jobdam.sns.service;

import com.jobdam.sns.dto.BookmarkResponseDto;

import java.util.List;

public interface BookmarkService {


    void addBookmark(Integer userId, Integer snsPostId);
    void removeBookmark(Integer userId, Integer snsPostId);
    List<BookmarkResponseDto> getBookmarksByUser(Integer userId);
}
