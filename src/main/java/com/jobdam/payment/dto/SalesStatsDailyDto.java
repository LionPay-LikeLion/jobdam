package com.jobdam.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SalesStatsDailyDto {
    private LocalDate date;
    private int cardSales;      // 카드 매출 or 전체 매출
    private int cardRefunds;    // 카드 환불
    private int netSales;       // 순매출 or 전체 매출
    private int totalOrders;    // 결제 건수
    private double avgAmount;   // 평균 결제 금액
    private int totalSales;     // 전체 매출 (일별 전체매출 API용)
}
