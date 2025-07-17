package com.jobdam.code.repository;

import com.jobdam.code.entity.MemberTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTypeCodeRepository extends JpaRepository<MemberTypeCode, Integer> {

    Optional<MemberTypeCode> findByCode(String currentMemberTypeCode);

}

