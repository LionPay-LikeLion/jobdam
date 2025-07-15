package com.jobdam.payment.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.jobdam.payment.dto.PaymentRequestDto;
import com.jobdam.payment.dto.PaymentResponseDto;
import com.jobdam.payment.entity.Payment;
import com.jobdam.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
        //임의로 merchant_uid 생성(또는 프론트에서 받아온 값 사용 가능, 추후 프론트 수정하며 수정 예정)
        String merchantUid = requestDto.getMerchantUid();
        if(merchantUid == null||merchantUid.isBlank()) {
            merchantUid = UUID.randomUUID().toString();
        }


        Payment payment = Payment.builder()
                .merchantUid(merchantUid)
                .amount(requestDto.getAmount())
                .method(requestDto.getMethod())
                .status("READY") // 초기 상태
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        return PaymentResponseDto.builder()
                .merchantUid(saved.getMerchantUid())
                .amount(saved.getAmount())
                .method(saved.getMethod())
                .status(saved.getStatus())
                .build();
    }



    //imp_uid 받아서 실제 결과 저장(webhook 또는 콜백에서 사용)
    @Transactional
    public void confirmPayment(String impUid, String merchantUid, JsonNode iamportResult)
    {
        Payment payment =  paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(()-> new RuntimeException("결제 내역 없음"));

        payment.setImpUid(impUid);
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

    }

    public List<PaymentResponseDto> getPaymentsByUserId(Integer userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(p -> PaymentResponseDto.builder()
                        .merchantUid(p.getMerchantUid())
                        .impUid(p.getImpUid())
                        .amount(p.getAmount())
                        .method(p.getMethod())
                        .status(p.getStatus())
                        .paidAt(p.getPaidAt())
                        .failReason(p.getFailReason())
                        .build())
                .toList();
    }

}
