// src/main/java/com/jobdam/payment/service/SalesStatsService.java
package com.jobdam.payment.service;

import com.jobdam.payment.dto.SalesStatsDailyDto;
import com.jobdam.payment.dto.SalesStatsMonthlyDto;
import com.jobdam.payment.repository.SalesStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SalesStatsService {

    private final SalesStatsRepository repo;

    // 일별 카드 매출 통계
    public List<SalesStatsDailyDto> getDailyStats() {
        Map<LocalDate, int[]> salesMap = new HashMap<>();  // [sum, count, avg]
        Map<LocalDate, int[]> refundMap = new HashMap<>(); // [sum, count, avg]

        // 카드 매출
        for (Object[] row : repo.findDailyCardSales()) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            int sum = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            double avg = ((Number) row[3]).doubleValue();
            salesMap.put(date, new int[] {sum, count, (int) avg});
        }

        // 카드 환불
        for (Object[] row : repo.findDailyCardRefunds()) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            int sum = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            double avg = ((Number) row[3]).doubleValue();
            refundMap.put(date, new int[] {sum, count, (int) avg});
        }

        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(salesMap.keySet());
        allDates.addAll(refundMap.keySet());

        List<LocalDate> sortedDates = new ArrayList<>(allDates);
        Collections.sort(sortedDates);

        List<SalesStatsDailyDto> result = new ArrayList<>();
        for (LocalDate date : sortedDates) {
            int cardSales = salesMap.getOrDefault(date, new int[]{0,0,0})[0];
            int cardRefunds = refundMap.getOrDefault(date, new int[]{0,0,0})[0];
            int netSales = cardSales - cardRefunds;
            int totalOrders = salesMap.getOrDefault(date, new int[]{0,0,0})[1];
            double avgAmount = salesMap.getOrDefault(date, new int[]{0,0,0})[2];
            result.add(new SalesStatsDailyDto(date, cardSales, cardRefunds, netSales, totalOrders, avgAmount));
        }
        return result;
    }

    // 월별 카드 매출 통계
    public List<SalesStatsMonthlyDto> getMonthlyStats() {
        Map<String, int[]> salesMap = new HashMap<>();
        Map<String, int[]> refundMap = new HashMap<>();

        // 카드 매출
        for (Object[] row : repo.findMonthlyCardSales()) {
            String month = (String) row[0];
            int sum = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            double avg = ((Number) row[3]).doubleValue();
            salesMap.put(month, new int[] {sum, count, (int) avg});
        }

        // 카드 환불
        for (Object[] row : repo.findMonthlyCardRefunds()) {
            String month = (String) row[0];
            int sum = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            double avg = ((Number) row[3]).doubleValue();
            refundMap.put(month, new int[] {sum, count, (int) avg});
        }

        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(salesMap.keySet());
        allMonths.addAll(refundMap.keySet());

        List<String> sortedMonths = new ArrayList<>(allMonths);
        Collections.sort(sortedMonths);

        List<SalesStatsMonthlyDto> result = new ArrayList<>();
        for (String month : sortedMonths) {
            int cardSales = salesMap.getOrDefault(month, new int[]{0,0,0})[0];
            int cardRefunds = refundMap.getOrDefault(month, new int[]{0,0,0})[0];
            int netSales = cardSales - cardRefunds;
            int totalOrders = salesMap.getOrDefault(month, new int[]{0,0,0})[1];
            double avgAmount = salesMap.getOrDefault(month, new int[]{0,0,0})[2];
            result.add(new SalesStatsMonthlyDto(month, cardSales, cardRefunds, netSales, totalOrders, avgAmount));
        }
        return result;
    }

    // 일별 전체 결제(카드, 포인트 등 결제수단 구분X, 성공만)
    public List<SalesStatsDailyDto> getDailyTotalStats() {
        List<SalesStatsDailyDto> result = new ArrayList<>();
        for (Object[] row : repo.findDailyTotalSales()) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            int sum = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            double avg = ((Number) row[3]).doubleValue();
            result.add(new SalesStatsDailyDto(date, sum, 0, sum, count, avg));
        }
        return result;
    }

    // 월별 전체 결제(카드, 포인트 등 결제수단 구분X, 성공만)
    public List<SalesStatsMonthlyDto> getMonthlyTotalStats() {
        List<SalesStatsMonthlyDto> result = new ArrayList<>();
        for (Object[] row : repo.findMonthlyTotalSales()) {
            String month = (String) row[0];
            int sum = ((Number) row[1]).intValue();
            int count = ((Number) row[2]).intValue();
            double avg = ((Number) row[3]).doubleValue();
            result.add(new SalesStatsMonthlyDto(month, sum, 0, sum, count, avg));
        }
        return result;
    }
}
