package com.apidemo.ddd.order;

import java.util.ArrayList;
import java.util.List;

// Aggregate Root: Order
public class Order {
    private final OrderId id;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(OrderId id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PLACED;
    }

    public void addItem(ProductId productId, int quantity) {
        items.add(new OrderItem(productId, quantity));
    }

    public void markAsPaid() {
        if (status != OrderStatus.PLACED) {
            throw new IllegalStateException("Order cannot be paid in current state");
        }
        this.status = OrderStatus.PAID;
    }

    public OrderId getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return List.copyOf(items); }
}
