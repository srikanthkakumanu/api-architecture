package com.apidemo.ddd.order;

public record OrderItem(ProductId productId, int quantity) {
    public OrderItem {
        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
