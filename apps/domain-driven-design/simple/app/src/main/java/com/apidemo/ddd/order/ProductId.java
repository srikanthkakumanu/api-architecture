package com.apidemo.ddd.order;

public record ProductId(String value) {
    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId value cannot be blank");
        }
    }
}
