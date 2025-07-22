// src/main/java/com/jobdam/payment/dto/SalesStatsDailyDto.java
package com.jobdam.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SalesStatsDailyDto {
    private LocalDate date;
    private int cardSales;
    private int cardRefunds;
    private int netSales;
    private int totalOrders;        // 결제 건수
    private double avgAmount;       // 평균 결제 금액
}
