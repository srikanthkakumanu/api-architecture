package com.apidemo.ddd.payment;

import com.apidemo.ddd.order.OrderId;

// Aggregate Root: Payment
public class Payment {
    private final PaymentId id;
    private final OrderId orderId;
    private PaymentStatus status;

    public Payment(PaymentId id, OrderId orderId) {
        this.id = id;
        this.orderId = orderId;
        this.status = PaymentStatus.PENDING;
    }

    public void complete() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment cannot be completed");
        }
        this.status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    public PaymentId getId() { return id; }
    public PaymentStatus getStatus() { return status; }
    public OrderId getOrderId() { return orderId; }
}
