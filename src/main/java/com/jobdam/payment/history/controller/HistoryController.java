// src/main/java/com/jobdam/payment/history/controller/HistoryController.java
package com.jobdam.payment.history.controller;

import com.jobdam.payment.history.dto.HistoryResponseDto;
import com.jobdam.payment.history.dto.PaymentHistoryResponseDto;
import com.jobdam.payment.history.service.HistoryService;
import com.jobdam.common.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<HistoryResponseDto> getPaymentHistory(
            @AuthenticationPrincipal CustomUserDetails User) {
        HistoryResponseDto history = historyService.getUserHistory(User.getUserId());
        return ResponseEntity.ok(history);
    }

    // 결제(실 결제) 내역만
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentHistoryResponseDto>> getPaymentList(
            @AuthenticationPrincipal CustomUserDetails User) {
        List<PaymentHistoryResponseDto> list = historyService.getPaymentList(User.getUserId());
        return ResponseEntity.ok(list);
    }

    // 포인트 내역만 (적립/사용/충전 등)
    @GetMapping("/points")
    public ResponseEntity<List<PaymentHistoryResponseDto>> getPointList(
            @AuthenticationPrincipal CustomUserDetails User) {
        List<PaymentHistoryResponseDto> list = historyService.getPointList(User.getUserId());
        return ResponseEntity.ok(list);
    }
}
