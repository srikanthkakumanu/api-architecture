package com.example.ecommerce.ordering.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void rejectsNegativeAmounts() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(new BigDecimal("-1.00"), "USD"));
    }

    @Test
    void addSumsAmountsOfTheSameCurrency() {
        Money five = new Money(new BigDecimal("5.00"), "USD");
        Money three = new Money(new BigDecimal("3.00"), "USD");

        assertEquals(new Money(new BigDecimal("8.00"), "USD"), five.add(three));
    }

    @Test
    void addRejectsMismatchedCurrencies() {
        Money usd = new Money(BigDecimal.TEN, "USD");
        Money eur = new Money(BigDecimal.TEN, "EUR");

        assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
    }

    @Test
    void multiplyScalesTheAmount() {
        Money unitPrice = new Money(new BigDecimal("2.50"), "USD");

        assertEquals(new Money(new BigDecimal("7.50"), "USD"), unitPrice.multiply(3));
    }
}
