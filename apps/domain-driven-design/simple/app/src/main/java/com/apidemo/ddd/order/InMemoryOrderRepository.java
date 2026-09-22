package com.apidemo.ddd.order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<OrderId, Order> store = new ConcurrentHashMap<>();

    @Override
    public Order findById(OrderId id) {
        Order order = store.get(id);
        if (order == null) {
            throw new IllegalArgumentException("No order found for id: " + id);
        }
        return order;
    }

    @Override
    public void save(Order order) {
        store.put(order.getId(), order);
    }
}
