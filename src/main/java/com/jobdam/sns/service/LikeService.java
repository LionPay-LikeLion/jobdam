package com.jobdam.sns.service;

public interface LikeService {

    void addLike(Integer userId, Integer snsPostId);
    void removeLike(Integer userId, Integer snsPostId);
    int countLikes(Integer snsPostId);
    boolean isLiked(Integer userId, Integer snsPostId);
}
