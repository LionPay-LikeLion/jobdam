package com.jobdam.payment.history.controller;

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
    public ResponseEntity<List<PaymentHistoryResponseDto>> getPaymentHistory(
            @RequestParam("userId") Integer userId) {
        List<PaymentHistoryResponseDto> history = historyService.getUserPaymentHistory(userId);
        return ResponseEntity.ok(history);
    }
}
