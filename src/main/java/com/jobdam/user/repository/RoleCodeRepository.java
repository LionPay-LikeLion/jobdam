package com.jobdam.user.repository;

import com.jobdam.user.entity.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleCodeRepository extends JpaRepository<RoleCode, Integer> {
}
