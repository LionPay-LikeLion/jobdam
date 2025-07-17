package com.jobdam.admin.controller;

import com.jobdam.admin.dto.ReportRequestDto;
import com.jobdam.admin.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<String> createReport(@RequestBody ReportRequestDto dto) {
        reportService.createReport(dto);
        return ResponseEntity.ok("신고가 접수되었습니다.");
    }
}
