package com.apidemo.mspatterns.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(UUID id, String sku, int quantity, Money unitPrice) {
    public OrderItem {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public Money lineTotal() {
        return new Money(unitPrice.amount().multiply(BigDecimal.valueOf(quantity)), unitPrice.currency());
    }
}
