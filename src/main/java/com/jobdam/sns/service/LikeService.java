package com.jobdam.sns.service;

public interface LikeService {

    // 좋아요 추가 (중복 허용 안함)
    void addLike(Integer userId, Integer snsPostId);

    // 좋아요 취소
    void removeLike(Integer userId, Integer snsPostId);

    // 게시글 좋아요 개수
    int countLikes(Integer snsPostId);

    // 사용자가 해당 게시글에 좋아요 눌렀는지 여부
    boolean isLiked(Integer userId, Integer snsPostId);
}
