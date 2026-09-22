package com.apidemo.ddd.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void newOrderStartsAsPlacedWithNoItems() {
        Order order = new Order(OrderId.generate());

        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void addItemAppendsAnOrderItem() {
        Order order = new Order(OrderId.generate());

        order.addItem(new ProductId("SKU-1"), 3);

        assertEquals(1, order.getItems().size());
        assertEquals(3, order.getItems().get(0).quantity());
    }

    @Test
    void markAsPaidTransitionsFromPlacedToPaid() {
        Order order = new Order(OrderId.generate());

        order.markAsPaid();

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void markAsPaidThrowsWhenOrderIsNotPlaced() {
        Order order = new Order(OrderId.generate());
        order.markAsPaid();

        assertThrows(IllegalStateException.class, order::markAsPaid);
    }
}
