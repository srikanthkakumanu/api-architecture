package com.example.ecommerce.ordering.domain;

/**
 * Value Object representing an ordered item within the sales context.
 */
public record OrderItem(String productId, String productName, Money unitPrice, int quantity) {
    public OrderItem {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("ProductId cannot be blank");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}
