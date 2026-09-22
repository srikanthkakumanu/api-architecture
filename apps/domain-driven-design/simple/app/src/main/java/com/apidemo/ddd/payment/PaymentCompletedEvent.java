package com.apidemo.ddd.payment;

import com.apidemo.ddd.order.OrderId;

public class PaymentCompletedEvent {
    private final OrderId orderId;

    public PaymentCompletedEvent(OrderId orderId) {
        this.orderId = orderId;
    }

    public OrderId getOrderId() { return orderId; }
}
