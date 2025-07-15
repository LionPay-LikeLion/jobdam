package com.jobdam.sns.controller;

import com.jobdam.sns.dto.BookmarkResponseDto;
import com.jobdam.sns.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sns/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @PostMapping
    public void addBookmark(@RequestParam Integer userId, @RequestParam Integer postId) {
        bookmarkService.addBookmark(userId, postId);
    }


    @DeleteMapping
    public void removeBookmark(@RequestParam Integer userId, @RequestParam Integer postId) {
        bookmarkService.removeBookmark(userId, postId);
    }


    @GetMapping
    public List<BookmarkResponseDto> getBookmarks(@RequestParam Integer userId) {
        return bookmarkService.getBookmarksByUser(userId);
    }
}
