package com.apidemo.ddd.payment;

import java.util.UUID;

public record PaymentId(String value) {
    public PaymentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PaymentId value cannot be blank");
        }
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID().toString());
    }
}
