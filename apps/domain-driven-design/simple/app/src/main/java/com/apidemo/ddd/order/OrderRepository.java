package com.apidemo.ddd.order;

public interface OrderRepository {
    Order findById(OrderId id);
    void save(Order order);
}
