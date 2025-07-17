package com.jobdam.payment.history.controller;

import com.jobdam.payment.history.dto.HistoryResponseDto;
import com.jobdam.payment.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
