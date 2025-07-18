package com.jobdam.code.repository;

import com.jobdam.code.entity.RoleCode;
import com.jobdam.code.entity.SubscriptionLevelCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionLevelCodeRepository extends JpaRepository<SubscriptionLevelCode, Integer> {

    Optional<SubscriptionLevelCode> findByCode(String code);
}

