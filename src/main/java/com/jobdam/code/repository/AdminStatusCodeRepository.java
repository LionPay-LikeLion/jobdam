package com.jobdam.code.repository;

import com.jobdam.code.entity.AdminStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminStatusCodeRepository extends JpaRepository<AdminStatusCode, Integer> {

    Optional<AdminStatusCode> findByCode(String statusCode);
}

