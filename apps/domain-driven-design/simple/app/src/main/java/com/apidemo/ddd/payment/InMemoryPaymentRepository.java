package com.apidemo.ddd.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPaymentRepository implements PaymentRepository {
    private final Map<PaymentId, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Payment findById(PaymentId id) {
        Payment payment = store.get(id);
        if (payment == null) {
            throw new IllegalArgumentException("No payment found for id: " + id);
        }
        return payment;
    }

    @Override
    public void save(Payment payment) {
        store.put(payment.getId(), payment);
    }
}
