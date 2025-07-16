package com.jobdam.code.repository;

import com.jobdam.code.entity.BoardTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardTypeCodeRepository extends JpaRepository<BoardTypeCode, Integer> {

    Optional<BoardTypeCode> findByCode(String code); // ★이거 추가!
}

