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

    private final PaymentService paymentService;

    // [1] 결제 요청 생성
    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto responseDto = paymentService.createPayment(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // [2] 결제 완료 확인 - 추후 웹훅 or 프론트 콜백에 따라 방식 변경 가능
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(
            @RequestParam("imp_uid") String impUid,
            @RequestParam("merchant_uid") String merchantUid,
            @RequestBody(required = false) JsonNode iamportResult
    ) {
        paymentService.confirmPayment(impUid, merchantUid, iamportResult);
        return ResponseEntity.ok().build();
    }

    // [3] 유저의 결제 내역(포인트 사용/충전 포함) 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> getUserPayments(@PathVariable Integer userId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }
}
