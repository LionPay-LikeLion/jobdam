package com.jobdam.admin.repository;

import com.jobdam.admin.entity.Report;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReportRepository extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report> {
    boolean existsByUserIdAndTargetId(Integer userId, Long targetId);

    @Override
    @EntityGraph(attributePaths = {"user", "reportTypeCode"})
    Page<Report> findAll(Pageable pageable);

    // status별 페이징 검색
    @EntityGraph(attributePaths = {"user", "reportTypeCode"})
    Page<Report> findByStatus(Integer status, Pageable pageable);
}
