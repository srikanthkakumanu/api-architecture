package com.apidemo.ddd.order;

import com.apidemo.ddd.payment.PaymentCompletedEvent;

public class PaymentCompletedHandler {
    private final OrderRepository orderRepository;

    public PaymentCompletedHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void handle(PaymentCompletedEvent event) {
        Order order = orderRepository.findById(event.getOrderId());
        order.markAsPaid();
        orderRepository.save(order);
    }
}
