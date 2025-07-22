// src/main/java/com/jobdam/payment/repository/SalesStatsRepository.java
package com.jobdam.payment.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.jobdam.payment.entity.Payment;

import java.util.List;

public interface SalesStatsRepository extends CrudRepository<Payment, Long> {

    // 일별 카드 매출
    @Query("SELECT DATE(p.createdAt), SUM(p.amount), COUNT(p), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE p.method = 'CARD' AND p.paymentStatusCodeId = 1 " +
            "GROUP BY DATE(p.createdAt)")
    List<Object[]> findDailyCardSales();

    // 일별 카드 환불
    @Query("SELECT DATE(p.createdAt), SUM(p.amount), COUNT(p), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE p.method = 'CARD' AND p.paymentStatusCodeId = 3 " +
            "GROUP BY DATE(p.createdAt)")
    List<Object[]> findDailyCardRefunds();

    // 월별 카드 매출
    @Query("SELECT FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m'), SUM(p.amount), COUNT(p), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE p.method = 'CARD' AND p.paymentStatusCodeId = 1 " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m')")
    List<Object[]> findMonthlyCardSales();

    // 월별 카드 환불
    @Query("SELECT FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m'), SUM(p.amount), COUNT(p), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE p.method = 'CARD' AND p.paymentStatusCodeId = 3 " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m')")
    List<Object[]> findMonthlyCardRefunds();

    // 일별 총 결제금액(모든 결제수단, 성공만)
    @Query("SELECT DATE(p.createdAt), SUM(p.amount), COUNT(p), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE p.paymentStatusCodeId = 1 " +
            "GROUP BY DATE(p.createdAt)")
    List<Object[]> findDailyTotalSales();

    // 월별 총 결제금액(모든 결제수단, 성공만)
    @Query("SELECT FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m'), SUM(p.amount), COUNT(p), AVG(p.amount) " +
            "FROM Payment p " +
            "WHERE p.paymentStatusCodeId = 1 " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m')")
    List<Object[]> findMonthlyTotalSales();
}
