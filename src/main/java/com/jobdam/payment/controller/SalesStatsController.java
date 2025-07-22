// src/main/java/com/jobdam/payment/controller/SalesStatsController.java
package com.jobdam.payment.controller;

import com.jobdam.payment.dto.SalesStatsDailyDto;
import com.jobdam.payment.dto.SalesStatsMonthlyDto;
import com.jobdam.payment.service.SalesStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesStatsController {

    private final SalesStatsService statsService;

    // 일별 카드매출/환불/순매출/건수/평균
    @GetMapping("/daily")
    public List<SalesStatsDailyDto> getDailyCardStats() {
        return statsService.getDailyStats();
    }

    // 월별 카드매출/환불/순매출/건수/평균
    @GetMapping("/monthly")
    public List<SalesStatsMonthlyDto> getMonthlyCardStats() {
        return statsService.getMonthlyStats();
    }

    // 일별 전체 결제금액(결제수단 구분X)
    @GetMapping("/daily/total")
    public List<SalesStatsDailyDto> getDailyTotalStats() {
        return statsService.getDailyTotalStats();
    }

    // 월별 전체 결제금액(결제수단 구분X)
    @GetMapping("/monthly/total")
    public List<SalesStatsMonthlyDto> getMonthlyTotalStats() {
        return statsService.getMonthlyTotalStats();
    }
}
