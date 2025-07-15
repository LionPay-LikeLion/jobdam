package com.jobdam.sns.repository;

import com.jobdam.sns.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 유저가 북마크한 게시글 전체 조회
    List<Bookmark> findByUserId(Integer userId);

    // 북마크 여부 확인
    Optional<Bookmark> findByUserIdAndSnsPostId(Integer userId, Integer snsPostId);

    // 게시글 삭제 시 관련 북마크 제거
    void deleteBySnsPostId(Integer snsPostId);
}
