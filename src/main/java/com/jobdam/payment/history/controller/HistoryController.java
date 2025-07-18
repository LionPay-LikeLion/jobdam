// src/main/java/com/jobdam/payment/history/controller/HistoryController.java
package com.jobdam.payment.history.controller;

import com.jobdam.payment.history.dto.HistoryResponseDto;
import com.jobdam.payment.history.dto.PaymentHistoryResponseDto;
import com.jobdam.payment.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<HistoryResponseDto> getPaymentHistory(
            @RequestParam("userId") Integer userId) {
        HistoryResponseDto history = historyService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }

    // 결제(실 결제) 내역만
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentHistoryResponseDto>> getPaymentList(
            @RequestParam("userId") Integer userId) {
        List<PaymentHistoryResponseDto> list = historyService.getPaymentList(userId);
        return ResponseEntity.ok(list);
    }

    // 포인트 내역만 (적립/사용/충전 등)
    @GetMapping("/points")
    public ResponseEntity<List<PaymentHistoryResponseDto>> getPointList(
            @RequestParam("userId") Integer userId) {
        List<PaymentHistoryResponseDto> list = historyService.getPointList(userId);
        return ResponseEntity.ok(list);
    }
}
