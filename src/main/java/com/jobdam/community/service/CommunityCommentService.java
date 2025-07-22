package com.jobdam.community.service;

import com.jobdam.community.dto.CommunityCommentCreateRequestDto;
import com.jobdam.community.dto.CommunityCommentListResponseDto;
import com.jobdam.community.entity.CommunityBoard;
import com.jobdam.community.entity.CommunityComment;
import com.jobdam.community.repository.CommunityCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobdam.code.entity.BoardStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityCommentService {


    private final CommunityCommentRepository communityCommentRepository;

    public Integer createComment(CommunityCommentCreateRequestDto dto, Integer postId, Integer userId) {

        CommunityComment comment = CommunityComment.builder()
                .content(dto.getContent())
                .communityPostId(postId)
                .boardStatusCodeId(1)
                .userId(userId)
                .build();

        communityCommentRepository.save(comment);

        return comment.getCommunityCommentId();
    }

    // 1. 게시글별 전체 댓글 조회
    @Transactional(readOnly = true)
    public List<CommunityCommentListResponseDto> getCommentsByPostId(Integer postId) {
        List<CommunityComment> comments = communityCommentRepository.findByCommunityPostIdOrderByCreatedAtAsc(postId);


        return comments.stream()
                .map(c -> CommunityCommentListResponseDto.builder()
                        .commentId(c.getCommunityCommentId())
                        .postId(postId)
                        .content(c.getContent())
                        .userNickname(c.getUser().getNickname())
                        .createdAt(c.getCreatedAt())
                        .boardStatusCode(c.getBoardStatusCode().getCode()
                        )
                        .build())
                .toList();
    }

    // 2. 내가 쓴 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<CommunityCommentListResponseDto> getCommentsByUserId(Integer userId) {
        List<CommunityComment> comments = communityCommentRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return comments.stream()
                .map(c -> CommunityCommentListResponseDto.builder()
                        .commentId(c.getCommunityCommentId())
                        .postId(c.getCommunityPostId())
                        .content(c.getContent())
                        .userNickname(c.getUser().getNickname())
                        .createdAt(c.getCreatedAt())
                        .boardStatusCode(c.getBoardStatusCode().getCode()
                        )
                        .build())
                .toList();
    }

    @Transactional
    public void updateComment(CommunityCommentCreateRequestDto dto, Integer commentId, Integer userId) {
        CommunityComment comment = communityCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }
        comment.setContent(dto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void softDeleteComment(Integer commentId, Integer userId) {
        CommunityComment comment = communityCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }
        int DELETED_STATUS_CODE_ID = 2;
        comment.setBoardStatusCodeId(DELETED_STATUS_CODE_ID);
        comment.setUpdatedAt(LocalDateTime.now());
    }





}
