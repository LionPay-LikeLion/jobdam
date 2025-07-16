package com.jobdam.payment.entity;

public enum PaymentStatusCode {
    SUCCESS(1),
    FAILED(2),
    CANCELLED(3);

    private final int code;

    PaymentStatusCode(int code) { this.code = code; }
    public int getCode() { return code; }
}