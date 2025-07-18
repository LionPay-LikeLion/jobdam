// src/main/java/com/jobdam/payment/controller/PaymentController.java
package com.jobdam.payment.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.payment.dto.CancelPaymentRequestDto;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public PaymentResponseDto createPayment(@AuthenticationPrincipal CustomUserDetails user,
                                            @RequestBody PaymentRequestDto dto) {
        return paymentService.createPayment(dto, user.getUserId());
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
    public PaymentResponseDto cancelPayment(@AuthenticationPrincipal CustomUserDetails user,
                                            @RequestBody CancelPaymentRequestDto dto) {
        return paymentService.cancelPayment(dto, user.getUserId());
    }

    @GetMapping("/my")
    public List<PaymentResponseDto> getMyPayments(@AuthenticationPrincipal CustomUserDetails user) {
        return paymentService.getPaymentsByUserId(user.getUserId());
    }
}

