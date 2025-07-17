// src/main/java/com/jobdam/payment/controller/PaymentController.java
package com.jobdam.payment.controller;

import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public PaymentResponseDto createPayment(@RequestBody PaymentRequestDto dto) {
        return paymentService.createPayment(dto);
    }

    @PostMapping("/confirm")
    public PaymentResponseDto confirmPayment(
            @RequestParam String merchantUid,
            @RequestParam String impUid,
            @RequestParam Integer amount
    ) {
        return paymentService.confirmPayment(merchantUid, impUid, amount);
    }

    @PostMapping("/cancel")
    public PaymentResponseDto cancelPayment(@RequestParam String merchantUid) {
        return paymentService.cancelPayment(merchantUid);
    }

    @GetMapping("/user/{userId}")
    public List<PaymentResponseDto> getPaymentsByUser(@PathVariable Integer userId) {
        return paymentService.getPaymentsByUserId(userId);
    }
}
