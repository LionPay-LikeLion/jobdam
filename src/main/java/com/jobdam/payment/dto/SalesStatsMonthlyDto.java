// src/main/java/com/jobdam/payment/dto/SalesStatsMonthlyDto.java
package com.jobdam.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesStatsMonthlyDto {
    private String month;           // "YYYY-MM"
    private int cardSales;
    private int cardRefunds;
    private int netSales;
    private int totalOrders;        // 결제 건수
    private double avgAmount;       // 평균 결제 금액
}
