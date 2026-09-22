package com.example.ecommerce.ordering.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private static final Money UNIT_PRICE = new Money(new BigDecimal("10.00"), "USD");

    @Test
    void newOrderStartsAsCreatedWithNoItems() {
        Order order = new Order(OrderId.generate(), "customer-1", "221B Baker Street, London");

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(0, order.getItems().size());
    }

    @Test
    void addItemAccumulatesTheOrderTotal() {
        Order order = new Order(OrderId.generate(), "customer-1", "221B Baker Street, London");

        order.addItem("SKU-1", "Widget", UNIT_PRICE, 2);

        assertEquals(new Money(new BigDecimal("20.00"), "USD"), order.calculateTotal());
    }

    @Test
    void addItemThrowsOnceOrderIsPaid() {
        Order order = new Order(OrderId.generate(), "customer-1", "221B Baker Street, London");
        order.addItem("SKU-1", "Widget", UNIT_PRICE, 1);
        order.markAsPaid("TXN-1");

        assertThrows(IllegalStateException.class, () -> order.addItem("SKU-2", "Gadget", UNIT_PRICE, 1));
    }

    @Test
    void markAsPaidThrowsWhenOrderHasNoItems() {
        Order order = new Order(OrderId.generate(), "customer-1", "221B Baker Street, London");

        assertThrows(IllegalStateException.class, () -> order.markAsPaid("TXN-1"));
    }

    @Test
    void markAsPaidThrowsWhenAlreadyPaid() {
        Order order = new Order(OrderId.generate(), "customer-1", "221B Baker Street, London");
        order.addItem("SKU-1", "Widget", UNIT_PRICE, 1);
        order.markAsPaid("TXN-1");

        assertThrows(IllegalStateException.class, () -> order.markAsPaid("TXN-2"));
    }

    @Test
    void markAsPaidTransitionsToPaid() {
        Order order = new Order(OrderId.generate(), "customer-1", "221B Baker Street, London");
        order.addItem("SKU-1", "Widget", UNIT_PRICE, 1);

        order.markAsPaid("TXN-1");

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void reconstructRestoresAggregateStateWithoutReplayingBusinessMethods() {
        OrderId id = OrderId.generate();
        OrderItem item = new OrderItem("SKU-1", "Widget", UNIT_PRICE, 3);

        Order restored = Order.reconstruct(id, "customer-1", "221B Baker Street, London",
                java.util.List.of(item), OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, restored.getStatus());
        assertEquals(1, restored.getItems().size());
        assertEquals(new Money(new BigDecimal("30.00"), "USD"), restored.calculateTotal());
    }
}
