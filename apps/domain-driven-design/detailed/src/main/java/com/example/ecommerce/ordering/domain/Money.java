package com.example.ecommerce.ordering.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object: Money.
 * Immutable, self-validating, and provides domain-safe operations.
 */
public record Money(BigDecimal amount, String currency) {
    public static final Money ZERO_USD = new Money(BigDecimal.ZERO, "USD");

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative: " + amount);
        }
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add distinct currencies: " + this.currency + " and " + other.currency);
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Factor cannot be negative: " + factor);
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }
}
