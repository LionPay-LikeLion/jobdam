package com.jobdam.community.service;

import com.jobdam.code.entity.BoardTypeCode;
import com.jobdam.code.entity.PostTypeCode;
import com.jobdam.code.repository.BoardTypeCodeRepository;
import com.jobdam.code.repository.PostTypeCodeRepository;
import com.jobdam.community.dto.*;
import com.jobdam.community.entity.Community;
import com.jobdam.community.entity.CommunityBoard;
import com.jobdam.community.entity.CommunityPost;
import com.jobdam.community.repository.CommunityBoardRepository;
import com.jobdam.community.repository.CommunityCommentRepository;
import com.jobdam.community.repository.CommunityPostRepository;
import com.jobdam.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityBoardRepository communityBoardRepository;
    private final PostTypeCodeRepository postTypeCodeRepository;

    @Transactional
    public List<CommunityPostListResponseDto> getPostsByBoard(Integer boardId, String postTypeCode, String keyword) {

        List<CommunityPost> posts;


        boolean hasType = postTypeCode != null && !postTypeCode.isBlank();
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (!hasType && !hasKeyword) {
            posts = communityPostRepository.findByCommunityBoardIdOrderByCreatedAtDesc(boardId);

        } else if (hasType && !hasKeyword) {
            posts = communityPostRepository.findByCommunityBoardIdAndPostTypeCodeCodeOrderByCreatedAtDesc(boardId, postTypeCode);

        } else if (!hasType) {
            posts = communityPostRepository.findByCommunityBoardIdAndKeyword(boardId, keyword);

        } else {
            posts = communityPostRepository.findByCommunityBoardIdAndPostTypeCodeCodeAndKeyword(boardId, postTypeCode, keyword);
        }

        return posts.stream()
                .map(post -> {
                    int commentCount = communityCommentRepository.countByCommunityPostId(post.getCommunityPostId());

                    return CommunityPostListResponseDto.builder()
                            .postId(post.getCommunityPostId())
                            .boardId(boardId)
                            .title(post.getTitle())
                            .content(post.getContent())
                            .userNickname(post.getUser().getNickname())
                            .createdAt(post.getCreatedAt())
                            .commentCount(commentCount)
                            .viewCount(post.getViewCount())
                            .postTypeCode(post.getPostTypeCode().getCode())
                            .build();
                })
                .toList();
    }

    @Transactional
    public Integer createPost(CommunityPostCreateRequestDto dto, Integer communityBoardId, Integer userId) {

        CommunityBoard communityBoard = communityBoardRepository.findById(communityBoardId)
                .orElseThrow(() -> new RuntimeException("커뮤니티를 찾을 수 없습니다."));

        PostTypeCode postTypeCode = postTypeCodeRepository.findByCode(dto.getPostTypeCode())
                .orElseThrow(() -> new RuntimeException("게시판 타입을 찾을 수 없습니다."));



        CommunityPost post = CommunityPost.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .communityBoardId(communityBoardId)
                .postTypeCodeId(postTypeCode.getPostTypeCodeId())
                .boardStatusCodeId(1)
                .viewCount(0)
                .userId(userId)
                .build();

        communityPostRepository.save(post);

        return post.getCommunityPostId();
    }

    @Transactional
    public void updatePost(CommunityPostUpdateRequestDto dto, Integer communityPostId, Integer userId) {

        CommunityPost post = communityPostRepository.findById(communityPostId)
                .orElseThrow(() -> new RuntimeException("포스트를 찾을 수 없습니다."));

        PostTypeCode postTypeCode = postTypeCodeRepository.findByCode(dto.getPostTypeCode())
                .orElseThrow(() -> new RuntimeException("게시글 타입을 찾을 수 없습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("작성자만 수정/삭제할 수 있습니다.");

        }


        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPostTypeCodeId(postTypeCode.getPostTypeCodeId());
        post.setUpdatedAt(LocalDateTime.now());



    }

    @Transactional
    public void softDelete(Integer postId, Integer userId) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("포스트를 찾을 수 없습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }


        int DELETED_STATUS_CODE_ID = 2;
        post.setBoardStatusCodeId(DELETED_STATUS_CODE_ID);

        post.setUpdatedAt(LocalDateTime.now());

    }

    public CommunityPostDetailResponseDto getPostDetail(Integer postId) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        User author = post.getUser();

        // 댓글 개수 카운트 (댓글 Repository 사용)
        int commentCount = communityCommentRepository.countByCommunityPostId(postId);

        return CommunityPostDetailResponseDto.builder()
                .postId(post.getCommunityPostId())
                .boardId(post.getCommunityBoardId())
                .title(post.getTitle())
                .content(post.getContent())
                .userNickname(author.getNickname())
                .userProfileImageUrl(author.getProfileImageUrl())
                .createdAt(post.getCreatedAt())
                .commentCount(commentCount) // 이렇게 주입!
                .viewCount(post.getViewCount())
                .postTypeCode(post.getPostTypeCode().getCode())
                .build();
    }



}


