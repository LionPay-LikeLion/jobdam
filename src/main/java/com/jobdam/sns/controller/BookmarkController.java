package com.jobdam.sns.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.sns.dto.BookmarkResponseDto;
import com.jobdam.sns.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sns/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @PostMapping
    public void addBookmark(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Integer postId) {
        bookmarkService.addBookmark(user.getUserId(), postId);
    }


    @DeleteMapping
    public void removeBookmark(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Integer postId) {
        bookmarkService.removeBookmark(user.getUserId(), postId);
    }


    @GetMapping
    public List<BookmarkResponseDto> getBookmarks(@AuthenticationPrincipal CustomUserDetails user) {
        return bookmarkService.getBookmarksByUser(user.getUserId());
    }
}
