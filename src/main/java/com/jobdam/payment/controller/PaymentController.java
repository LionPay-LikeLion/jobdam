// src/main/java/com/jobdam/payment/controller/PaymentController.java
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

    // PortOne “결제 준비(ready)” 호출 후 내부 DB 저장
    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDto> create(@RequestBody PaymentRequestDto dto) {
        return ResponseEntity.ok(svc.createPayment(dto));
    }

    // PortOne “결제 승인(approve)” 콜백 처리
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponseDto> confirm(
            @RequestParam String imp_uid,
            @RequestParam String merchant_uid,
            @RequestBody JsonNode pgResult
    ) {
        return ResponseEntity.ok(svc.confirmPayment(imp_uid, merchant_uid, pgResult));
    }

    // PortOne “결제 실패(fail)” 처리
    @PostMapping("/fail")
    public ResponseEntity<PaymentResponseDto> fail(@RequestParam String merchant_uid) {
        return ResponseEntity.ok(svc.failPayment(merchant_uid));
    }

    // PortOne “결제 취소(cancel)” 처리
    @PostMapping("/cancel")
    public ResponseEntity<PaymentResponseDto> cancel(@RequestParam String merchant_uid) {
        return ResponseEntity.ok(svc.cancelPayment(merchant_uid));
    }

    // 유저별 결제 내역 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> list(@PathVariable Integer userId) {
        return ResponseEntity.ok(svc.getPaymentsByUserId(userId));
    }

}
