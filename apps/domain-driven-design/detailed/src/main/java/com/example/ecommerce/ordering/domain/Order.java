package com.example.ecommerce.ordering.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root: Order.
 * Encapsulates order state and protects invariants.
 */
public class Order {
    private final OrderId id;
    private final String customerId;
    private final String shippingAddress;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(OrderId id, String customerId, String shippingAddress) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("CustomerId cannot be blank");
        }
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new IllegalArgumentException("ShippingAddress cannot be blank");
        }
        this.id = id;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
    }

    // Business Method: Enforces invariant that items cannot be modified after confirmation
    public void addItem(String productId, String productName, Money unitPrice, int quantity) {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Cannot add items to an order in status: " + this.status);
        }
        this.items.add(new OrderItem(productId, productName, unitPrice, quantity));
    }

    // Business Method: Enforces invariant that an empty or already paid order cannot be marked paid
    public void markAsPaid(String paymentTransactionId) {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot pay for an order with zero items");
        }
        if (this.status == OrderStatus.PAID) {
            throw new IllegalStateException("Order is already paid");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot pay for a cancelled order");
        }
        if (paymentTransactionId == null || paymentTransactionId.isBlank()) {
            throw new IllegalArgumentException("Valid payment transaction ID required");
        }
        this.status = OrderStatus.PAID;
    }

    public Money calculateTotal() {
        return items.stream()
                .map(OrderItem::subtotal)
                .reduce(Money.ZERO_USD, Money::add);
    }

    // Encapsulation: Unmodifiable getters, no public setters
    public OrderId getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getShippingAddress() { return shippingAddress; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public OrderStatus getStatus() { return status; }

    /**
     * Persistence rehydration factory. Restores an aggregate exactly as previously
     * persisted (including its status and items) without replaying the business
     * methods above. Intended for use by infrastructure-layer repository adapters only.
     */
    public static Order reconstruct(OrderId id, String customerId, String shippingAddress,
                                     List<OrderItem> items, OrderStatus status) {
        Order order = new Order(id, customerId, shippingAddress);
        order.items.addAll(items);
        order.status = status;
        return order;
    }
}
