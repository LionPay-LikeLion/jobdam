package com.jobdam.admin.service;

import com.jobdam.admin.entity.Report;
import com.jobdam.admin.repository.ReportRepository;
import com.jobdam.admin.dto.ReportRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public void createReport(ReportRequestDto dto, Integer userId) {
        boolean isDuplicate = reportRepository.existsByUserIdAndTargetId(userId, dto.getTargetId());
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 해당 대상에 대해 신고하셨습니다.");
        }

        Report report = new Report();
        report.setReportTypeCodeId(dto.getReportTypeCodeId());
        report.setTargetId(dto.getTargetId());
        report.setUserId(userId);
        report.setReason(dto.getReason());

        reportRepository.save(report);
    }
}
