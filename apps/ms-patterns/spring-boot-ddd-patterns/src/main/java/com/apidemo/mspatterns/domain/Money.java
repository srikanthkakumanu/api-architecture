package com.apidemo.mspatterns.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be zero or positive");
        }
        if (currency == null || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a three-letter ISO code");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency())) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(amount.add(other.amount()), currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
