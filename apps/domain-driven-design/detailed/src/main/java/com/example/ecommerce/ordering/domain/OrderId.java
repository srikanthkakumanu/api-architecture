package com.example.ecommerce.ordering.domain;

import java.util.UUID;

/**
 * Strongly-typed Identity Value Object.
 */
public record OrderId(UUID value) {
    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("OrderId value cannot be null");
        }
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId fromString(String raw) {
        return new OrderId(UUID.fromString(raw));
    }
}
