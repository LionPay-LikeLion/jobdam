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
import com.jobdam.user.entity.User;
import com.jobdam.sns.repository.BookmarkRepository;


import com.jobdam.sns.repository.SnsPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobdam.common.exception.LimitExceededException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
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
            dto.setSubscriptionLevelCode(post.getUser().getSubscriptionLevelCode().getCode());
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


    /*@Override
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
    }*/
    @Override
    @Transactional
    public Integer createPost(SnsPostRequestDto requestDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (user.getSubscriptionLevelCodeId() == 1) {
            int postCount = snsPostRepository.countByUserIdInCurrentMonth(userId);
            if (postCount >= 10) {
                throw new LimitExceededException("일반 회원은 한 달에 최대 10개의 게시글만 작성할 수 있습니다.");
            }
        }


        String imageUrl = null;
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + requestDto.getImage().getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            try {
                requestDto.getImage().transferTo(dest);
                imageUrl = "/uploads/" + userId + "/" + fileName;
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException("이미지 파일 업로드 중 오류가 발생했습니다.", e);
            }
        }

        String attachmentUrl = null;
        if (requestDto.getAttachment() != null && !requestDto.getAttachment().isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + requestDto.getAttachment().getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            try {
                requestDto.getAttachment().transferTo(dest);
                attachmentUrl = "/uploads/" + userId + "/" + fileName;
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException("첨부파일 업로드 중 오류가 발생했습니다.", e);
            }
        }


        SnsPost post = new SnsPost();
        post.setUserId(userId);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setImageUrl(imageUrl);        // 저장된 경로
        post.setAttachmentUrl(attachmentUrl); // 저장된 경로

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

        // ---- 이미지 파일 새로 업로드 시 ----
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + requestDto.getImage().getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            try {
                requestDto.getImage().transferTo(dest);
                post.setImageUrl("/uploads/" + userId + "/" + fileName);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException("이미지 파일 업로드 중 오류가 발생했습니다.", e);
            }
        }

        if (requestDto.getAttachment() != null && !requestDto.getAttachment().isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + requestDto.getAttachment().getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            try {
                requestDto.getAttachment().transferTo(dest);
                post.setAttachmentUrl("/uploads/" + userId + "/" + fileName);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException("첨부파일 업로드 중 오류가 발생했습니다.", e);
            }
        }
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

    @Override
    public List<SnsPostResponseDto> searchPostsByKeyword(String keyword, Integer userId) {
        List<SnsPost> posts = snsPostRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        return posts.stream()
                .map(post -> SnsPostMapper.toDto(
                        post,
                        post.getUser(),
                        likeRepository.countBySnsPostId(post.getSnsPostId()),
                        snsCommentRepository.countBySnsPostId(post.getSnsPostId()),
                        likeRepository.existsByUserIdAndSnsPostId(userId, post.getSnsPostId()),
                        bookmarkRepository.existsByUserIdAndSnsPostId(userId, post.getSnsPostId())
                ))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<SnsPostResponseDto> getPremiumPriorityPosts(Integer currentUserId) {
        List<SnsPost> posts = snsPostRepository.findAllOrderByPremiumFirst();

        return posts.stream().map(post ->
                SnsPostMapper.toDto(
                        post,
                        post.getUser(),
                        likeRepository.countBySnsPostId(post.getSnsPostId()),
                        snsCommentRepository.countBySnsPostId(post.getSnsPostId()),
                        likeRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId()),
                        bookmarkRepository.existsByUserIdAndSnsPostId(currentUserId, post.getSnsPostId())
                )
        ).collect(Collectors.toList());
    }



}