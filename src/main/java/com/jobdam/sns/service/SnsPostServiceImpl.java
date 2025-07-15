package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.sns.entity.User;
import com.jobdam.sns.repository.BookmarkRepository;
import com.jobdam.user.repository.UserRepository;

import com.jobdam.sns.repository.SnsPostRepository;
import com.jobdam.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnsPostServiceImpl implements SnsPostService {

    private final SnsPostRepository snsPostRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    @Override
    public List<SnsPostResponseDto> getAllPosts(Integer currentUserId) {
        List<SnsPost> posts = snsPostRepository.findAllByOrderByCreatedAtDesc();

        return posts.stream().map(post -> {
            SnsPostResponseDto dto = new SnsPostResponseDto();
            dto.setSnsPostId(post.getSnsPostId());
            dto.setUserId(post.getUserId());
            dto.setNickname(post.getUser().getNickname());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            dto.setAttachmentUrl(post.getAttachmentUrl());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setLikeCount(likeRepository.countBySnsPostId(post.getSnsPostId()));
            dto.setCommentCount(post.getCommentList() != null ? post.getCommentList().size() : 0);
            dto.setIsLiked(likeRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()));
            dto.setIsBookmarked(bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public SnsPostResponseDto getPostById(Integer postId, Integer currentUserId) {
        SnsPost post = snsPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        SnsPostResponseDto dto = new SnsPostResponseDto();
        dto.setSnsPostId(post.getSnsPostId());
        dto.setUserId(post.getUserId());
        dto.setNickname(post.getUser().getNickname());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setAttachmentUrl(post.getAttachmentUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(likeRepository.countBySnsPostId(postId));
        dto.setCommentCount(post.getCommentList() != null ? post.getCommentList().size() : 0);
        dto.setIsLiked(likeRepository.existsByUserIdAndSnsPostId(currentUserId, postId));
        dto.setIsBookmarked(bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, postId));
        return dto;
    }

    @Override
    @Transactional
    public Integer createPost(SnsPostRequestDto requestDto, Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        SnsPost post = new SnsPost();
        post.setUserId(userId);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setImageUrl(requestDto.getImageUrl());
        post.setAttachmentUrl(requestDto.getAttachmentUrl());

        snsPostRepository.save(post);
        return post.getSnsPostId();
    }

    @Override
    @Transactional
    public void updatePost(Integer postId, SnsPostRequestDto requestDto, Integer userId) {
        SnsPost post = snsPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("본인만 수정할 수 있습니다.");
        }

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setImageUrl(requestDto.getImageUrl());
        post.setAttachmentUrl(requestDto.getAttachmentUrl());

        // JPA dirty checking으로 자동 업데이트
    }

    @Override
    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        SnsPost post = snsPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("본인만 삭제할 수 있습니다.");
        }

        snsPostRepository.delete(post);
    }
}
