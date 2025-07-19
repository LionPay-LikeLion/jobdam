// src/main/java/com/jobdam/payment/entity/ChargeOption.java
package com.jobdam.payment.entity;

public enum ChargeOption {
    BASIC_10000("BASIC_10000", 10000, 10000),
    BONUS_20000("BONUS_20000", 22000, 20000),
    BONUS_30000("BONUS_30000", 33000, 30000),
    BONUS_40000("BONUS_40000", 45000, 40000),
    PROMO_50000("PROMO_50000", 60000, 50000),
     OPEN_EVENT("EVENT_100", 300000, 100),
    // ================================
    ;

    private final String code;
    private final int point;
    private final int amount;

    ChargeOption(String code, int point, int amount) {
        this.code = code;
        this.point = point;
        this.amount = amount;
    }

    public static ChargeOption fromCode(String code) {
        for (ChargeOption c : values()) {
            if (c.code.equals(code)) return c;
        }
        throw new IllegalArgumentException("존재하지 않는 충전옵션: " + code);
    }

    public int getPoint() { return point; }
    public int getAmount() { return amount; }
    public String getCode() { return code; }
}
