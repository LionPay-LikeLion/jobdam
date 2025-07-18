package com.jobdam.code.repository;

import com.jobdam.code.entity.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleCodeRepository extends JpaRepository<RoleCode, Integer> {

    Optional<RoleCode> findByCode(String code);
}

