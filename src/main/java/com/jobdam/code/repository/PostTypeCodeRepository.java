package com.jobdam.code.repository;

import com.jobdam.code.entity.BoardTypeCode;
import com.jobdam.code.entity.PostTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTypeCodeRepository extends JpaRepository<PostTypeCode, Integer> {

    Optional<PostTypeCode> findByCode(String code);

}

