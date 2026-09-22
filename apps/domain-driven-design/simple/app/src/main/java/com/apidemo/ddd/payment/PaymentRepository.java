package com.apidemo.ddd.payment;

public interface PaymentRepository {
    Payment findById(PaymentId id);
    void save(Payment payment);
}
