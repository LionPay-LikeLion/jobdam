package com.jobdam.payment.service;

import com.jobdam.payment.dto.SalesStatsDailyDto;
import com.jobdam.payment.dto.SalesStatsMonthlyDto;
import com.jobdam.payment.repository.SalesStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesStatsService {
    private final SalesStatsRepository repo;

    // 일별 카드 매출/환불/순매출/건수/평균
    public List<SalesStatsDailyDto> getDailyStats() {
        List<Object[]> cardSalesRows = repo.findDailyCardSales();
        List<Object[]> cardRefundRows = repo.findDailyCardRefunds();

        // 매출·환불 map: date -> value
        java.util.Map<LocalDate, Integer> salesMap = new java.util.HashMap<>();
        java.util.Map<LocalDate, Integer> refundMap = new java.util.HashMap<>();
        java.util.Map<LocalDate, Integer> orderMap = new java.util.HashMap<>();
        java.util.Map<LocalDate, Double> avgMap = new java.util.HashMap<>();

        for (Object[] row : cardSalesRows) {
            LocalDate date = row[0] instanceof LocalDate ? (LocalDate) row[0] : LocalDate.parse(row[0].toString());
            // null-safe 변환 (null이면 0/0.0)
            int amount = row[1] == null ? 0 : ((Number) row[1]).intValue();
            int cnt = row[2] == null ? 0 : ((Number) row[2]).intValue();
            double avg = row[3] == null ? 0.0 : ((Number) row[3]).doubleValue();
            salesMap.put(date, amount);
            orderMap.put(date, cnt);
            avgMap.put(date, avg);
        }
        for (Object[] row : cardRefundRows) {
            LocalDate date = row[0] instanceof LocalDate ? (LocalDate) row[0] : LocalDate.parse(row[0].toString());
            int refund = row[1] == null ? 0 : ((Number) row[1]).intValue();
            refundMap.put(date, refund);
        }

        List<SalesStatsDailyDto> result = new ArrayList<>();
        for (LocalDate date : salesMap.keySet()) {
            int cardSales = salesMap.getOrDefault(date, 0);
            int cardRefunds = refundMap.getOrDefault(date, 0);
            int netSales = cardSales - cardRefunds;
            int totalOrders = orderMap.getOrDefault(date, 0);
            double avgAmount = avgMap.getOrDefault(date, 0.0);
            // cardSales와 netSales는 카드매출 기준, 전체매출 X
            result.add(new SalesStatsDailyDto(
                    date, cardSales, cardRefunds, netSales, totalOrders, avgAmount, 0 // totalSales = 0
            ));
        }
        return result;
    }

    // 월별 카드 매출/환불/순매출/건수/평균
    public List<SalesStatsMonthlyDto> getMonthlyStats() {
        List<Object[]> cardSalesRows = repo.findMonthlyCardSales();
        List<Object[]> cardRefundRows = repo.findMonthlyCardRefunds();

        java.util.Map<String, Integer> salesMap = new java.util.HashMap<>();
        java.util.Map<String, Integer> refundMap = new java.util.HashMap<>();
        java.util.Map<String, Integer> orderMap = new java.util.HashMap<>();
        java.util.Map<String, Double> avgMap = new java.util.HashMap<>();

        for (Object[] row : cardSalesRows) {
            String month = row[0].toString();
            int amount = row[1] == null ? 0 : ((Number) row[1]).intValue();
            int cnt = row[2] == null ? 0 : ((Number) row[2]).intValue();
            double avg = row[3] == null ? 0.0 : ((Number) row[3]).doubleValue();
            salesMap.put(month, amount);
            orderMap.put(month, cnt);
            avgMap.put(month, avg);
        }
        for (Object[] row : cardRefundRows) {
            String month = row[0].toString();
            int refund = row[1] == null ? 0 : ((Number) row[1]).intValue();
            refundMap.put(month, refund);
        }

        List<SalesStatsMonthlyDto> result = new ArrayList<>();
        for (String month : salesMap.keySet()) {
            int cardSales = salesMap.getOrDefault(month, 0);
            int cardRefunds = refundMap.getOrDefault(month, 0);
            int netSales = cardSales - cardRefunds;
            int totalOrders = orderMap.getOrDefault(month, 0);
            double avgAmount = avgMap.getOrDefault(month, 0.0);
            // cardSales와 netSales는 카드매출 기준, 전체매출 X
            result.add(new SalesStatsMonthlyDto(
                    month, cardSales, cardRefunds, netSales, totalOrders, avgAmount, 0 // totalSales = 0
            ));
        }
        return result;
    }

    // 일별 전체 결제금액(카드/포인트/현금 등 결제수단 구분 없이 성공만)
    public List<SalesStatsDailyDto> getDailyTotalStats() {
        List<Object[]> rows = repo.findDailyTotalSales();
        List<SalesStatsDailyDto> result = new ArrayList<>();
        for (Object[] r : rows) {
            LocalDate date = r[0] instanceof LocalDate ? (LocalDate) r[0] : LocalDate.parse(r[0].toString());
            int sum = r[1] == null ? 0 : ((Number) r[1]).intValue();
            int count = r[2] == null ? 0 : ((Number) r[2]).intValue();
            double avg = r[3] == null ? 0.0 : ((Number) r[3]).doubleValue();

            result.add(new SalesStatsDailyDto(
                    date,
                    sum,   // cardSales == 전체 매출
                    0,     // cardRefunds
                    sum,   // netSales == 전체 매출
                    count,
                    avg,
                    sum    // totalSales
            ));
        }
        return result;
    }

    // 월별 전체 결제금액
    public List<SalesStatsMonthlyDto> getMonthlyTotalStats() {
        List<Object[]> rows = repo.findMonthlyTotalSales();
        List<SalesStatsMonthlyDto> result = new ArrayList<>();
        for (Object[] r : rows) {
            String month = r[0].toString();
            int sum = r[1] == null ? 0 : ((Number) r[1]).intValue();
            int count = r[2] == null ? 0 : ((Number) r[2]).intValue();
            double avg = r[3] == null ? 0.0 : ((Number) r[3]).doubleValue();

            result.add(new SalesStatsMonthlyDto(
                    month,
                    sum,   // cardSales == 전체 매출
                    0,     // cardRefunds
                    sum,   // netSales == 전체 매출
                    count,
                    avg,
                    sum    // totalSales
            ));
        }
        return result;
    }
}
