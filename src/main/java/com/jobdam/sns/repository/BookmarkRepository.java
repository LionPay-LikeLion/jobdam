package com.jobdam.sns.repository;

import com.jobdam.sns.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    List<Bookmark> findByUserId(Integer userId);
    Optional<Bookmark> findByUserIdAndSnsPostId(Integer userId, Integer snsPostId);
    void deleteBySnsPostId(Integer snsPostId);

    boolean existsByUserIdAndSnsPostId(Integer userId, Integer snsPostId);
    int countByUserId(Integer userId);

}
