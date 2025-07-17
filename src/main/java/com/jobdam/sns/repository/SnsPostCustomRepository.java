package com.jobdam.sns.repository;

import com.jobdam.sns.dto.SnsPostFilterDto;
import com.jobdam.sns.entity.SnsPost;

import java.util.List;

public interface SnsPostCustomRepository {

    List<SnsPost> searchFilteredPosts(SnsPostFilterDto filter);
}
