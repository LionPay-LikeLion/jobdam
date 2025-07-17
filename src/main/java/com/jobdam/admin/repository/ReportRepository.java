package com.jobdam.admin.repository;

import com.jobdam.admin.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    boolean existsByUserIdAndTargetId(Integer userId, Long targetId);

}
