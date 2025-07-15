package com.jobdam.sns.service;

import com.jobdam.sns.dto.BookmarkResponseDto;
import com.jobdam.sns.entity.Bookmark;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.user.entity.User;
import com.jobdam.sns.repository.BookmarkRepository;
import com.jobdam.sns.repository.SnsPostRepository;
import com.jobdam.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final SnsPostRepository snsPostRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addBookmark(Integer userId, Integer snsPostId) {
        if (bookmarkRepository.existsByUserIdAndSnsPostId(userId, snsPostId)) {
            throw new RuntimeException("이미 북마크된 게시글입니다.");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setSnsPostId(snsPostId);
        bookmark.setCreatedAt(LocalDateTime.now());

        bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(Integer userId, Integer snsPostId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndSnsPostId(userId, snsPostId)
                .orElseThrow(() -> new RuntimeException("북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);
    }

    @Override
    public List<BookmarkResponseDto> getBookmarksByUser(Integer userId) {

        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        return bookmarks.stream().map(bookmark -> {
            SnsPost post = bookmark.getSnsPost();
            BookmarkResponseDto dto = new BookmarkResponseDto();
            dto.setBookmarkId(bookmark.getBookmarkId());
            dto.setSnsPostId(post.getSnsPostId());
            dto.setTitle(post.getTitle());
            dto.setThumbnailImageUrl(post.getImageUrl());
            dto.setBookmarkedAt(bookmark.getCreatedAt());
            dto.setUserId(userId);
            dto.setNickname(post.getUser().getNickname());
            return dto;
        }).collect(Collectors.toList());
    }
}