package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;
import com.jobdam.sns.entity.SnsComment;
import com.jobdam.sns.entity.User;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.sns.repository.SnsCommentRepository;
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
public class SnsCommentServiceImpl implements SnsCommentService {

    private final SnsCommentRepository snsCommentRepository;
    private final UserRepository userRepository;
    private final SnsPostRepository snsPostRepository;

    @Override
    public List<SnsCommentResponseDto> getCommentsByPostId(Integer snsPostId) {
        List<SnsComment> comments = snsCommentRepository.findBySnsPostId(snsPostId);

        return comments.stream().map(comment -> {
            SnsCommentResponseDto dto = new SnsCommentResponseDto();
            dto.setSnsCommentId(comment.getSnsCommentId());
            dto.setSnsPostId(comment.getSnsPostId());
            dto.setUserId(comment.getUserId());
            dto.setNickname(comment.getUser().getNickname());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());
            dto.setUpdatedAt(comment.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Integer createComment(SnsCommentRequestDto requestDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SnsPost post = snsPostRepository.findById(requestDto.getSnsPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        SnsComment comment = new SnsComment();
        comment.setSnsPostId(post.getSnsPostId());
        comment.setUserId(userId);
        comment.setContent(requestDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        snsCommentRepository.save(comment);
        return comment.getSnsCommentId();
    }

    @Override
    @Transactional
    public void updateComment(Integer commentId, SnsCommentRequestDto requestDto, Integer userId) {
        SnsComment comment = snsCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("You are not the owner of this comment.");
        }

        comment.setContent(requestDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        snsCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId, Integer userId) {
        SnsComment comment = snsCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("You are not the owner of this comment.");
        }

        snsCommentRepository.delete(comment);
    }
}
