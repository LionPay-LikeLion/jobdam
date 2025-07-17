package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.sns.repository.BookmarkRepository;
import com.jobdam.sns.repository.LikeRepository;
import com.jobdam.sns.repository.SnsCommentRepository;
import com.jobdam.user.repository.UserRepository;
import com.jobdam.sns.dto.SnsPostDetailResponseDto;
import com.jobdam.code.entity.MemberTypeCode;
import com.jobdam.sns.mapper.SnsPostMapper;
import com.jobdam.sns.dto.SnsPostFilterDto;


import com.jobdam.sns.repository.SnsPostRepository;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnsPostServiceImpl implements SnsPostService {

    private final SnsPostRepository snsPostRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SnsCommentRepository  snsCommentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SnsPostResponseDto> getAllPosts(Integer currentUserId) {
        List<SnsPost> posts = snsPostRepository.findAllWithUserAndMemberTypeCode();

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
            dto.setCommentCount(snsCommentRepository.countBySnsPostId(post.getSnsPostId()));
            dto.setLiked(likeRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()));
            dto.setBookmarked(bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()));
            dto.setMemberTypeCode(
                    post.getUser().getMemberTypeCode() != null
                            ? post.getUser().getMemberTypeCode().getCode()
                            : null
            );
            return dto;

        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SnsPostResponseDto> getMyPosts(Integer currentUserId) {
        List<SnsPost> posts = snsPostRepository.findAllByUserIdWithUserAndMemberTypeCode(currentUserId);

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
            dto.setCommentCount(snsCommentRepository.countBySnsPostId(post.getSnsPostId()));
            dto.setLiked(likeRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()));
            dto.setBookmarked(bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()));
            dto.setMemberTypeCode(
                    post.getUser().getMemberTypeCode() != null
                            ? post.getUser().getMemberTypeCode().getCode()
                            : null
            );
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public SnsPostResponseDto getPostById(Integer postId, Integer currentUserId) {
        SnsPost post = snsPostRepository.findByIdWithUserAndMemberTypeCode(postId)
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
        dto.setCommentCount(snsCommentRepository.countBySnsPostId(post.getSnsPostId()));
        dto.setLiked(likeRepository.existsByUserIdAndSnsPostId(currentUserId, postId));
        dto.setBookmarked(bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, postId));
        return dto;
    }

    @Override
    public SnsPostDetailResponseDto getPostDetail(Integer postId, Integer userId) {
        SnsPost post = snsPostRepository.findByIdWithUserAndMemberTypeCode(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        int likeCount = likeRepository.countBySnsPostId(postId);
        int commentCount = snsCommentRepository.countBySnsPostId(postId);
        boolean liked = likeRepository.existsByUserIdAndSnsPostId(userId, postId);
        boolean bookmarked = bookmarkRepository.existsByUserIdAndSnsPostId(userId, postId);

        return SnsPostDetailResponseDto.builder()
                .snsPostId(post.getSnsPostId())
                .userId(post.getUserId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .attachmentUrl(post.getAttachmentUrl())
                .createdAt(post.getCreatedAt())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .liked(liked)
                .bookmarked(bookmarked)
                .build();
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
    @Override
    @Transactional(readOnly = true)
    public List<SnsPostResponseDto> getFilteredPosts(SnsPostFilterDto filter, Integer currentUserId) {
        List<SnsPost> posts = snsPostRepository.searchFilteredPosts(filter);

        return posts.stream().map(post -> {
            return SnsPostMapper.toDto(
                    post,
                    post.getUser(),
                    likeRepository.countBySnsPostId(post.getSnsPostId()),
                    snsCommentRepository.countBySnsPostId(post.getSnsPostId()),
                    likeRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()),
                    bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId())
            );
        }).collect(Collectors.toList());
    }

}