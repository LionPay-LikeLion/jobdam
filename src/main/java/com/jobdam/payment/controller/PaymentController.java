package com.jobdam.payment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService svc;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDto> create(@RequestBody PaymentRequestDto dto) {
        return ResponseEntity.ok(svc.createPayment(dto));
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponseDto> confirm(
            @RequestParam String imp_uid,
            @RequestParam String merchant_uid,
            @RequestBody JsonNode pgResult
    ) {
        return ResponseEntity.ok(svc.confirmPayment(imp_uid, merchant_uid, pgResult));
    }

    @PostMapping("/fail")
    public ResponseEntity<PaymentResponseDto> fail(@RequestParam String merchant_uid) {
        return ResponseEntity.ok(svc.failPayment(merchant_uid));
    }

    @PostMapping("/cancel")
    public ResponseEntity<PaymentResponseDto> cancel(@RequestParam String merchant_uid) {
        return ResponseEntity.ok(svc.cancelPayment(merchant_uid));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> list(@PathVariable Integer userId) {
        return ResponseEntity.ok(svc.getPaymentsByUserId(userId));
    }
}
