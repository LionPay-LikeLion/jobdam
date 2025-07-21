package com.jobdam.admin.controller;

import com.jobdam.admin.dto.ReportResponseDto;
import com.jobdam.admin.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/report")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;

    // 신고 목록 (관리자)
    @GetMapping
    public Page<ReportResponseDto> getReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return reportService.getReports(PageRequest.of(page, size));
    }

    // 신고 상세 (관리자)
    @GetMapping("/{id}")
    public ReportResponseDto getReportById(@PathVariable Integer id) {
        return reportService.getReportById(id);
    }

    // 신고 삭제 (관리자)
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Integer id) {
        reportService.deleteReport(id);
    }

    // === 신고대상 회원 활동정지 및 신고 처리완료 ===
    @PatchMapping("/{id}/deactivate")
    public void processReportAndDeactivate(@PathVariable Integer id) {
        reportService.processReportAndDeactivate(id);
    }

    // === 신고 허용 (status만 1로 바꿈, 정지 X) ===
    @PatchMapping("/{id}/approve")
    public void approveReport(@PathVariable Integer id) {
        reportService.approveReport(id);
    }
}
