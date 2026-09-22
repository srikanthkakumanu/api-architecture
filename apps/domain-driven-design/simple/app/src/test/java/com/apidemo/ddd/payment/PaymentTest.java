package com.apidemo.ddd.payment;

import com.apidemo.ddd.order.OrderId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {

    @Test
    void newPaymentStartsAsPending() {
        Payment payment = new Payment(PaymentId.generate(), OrderId.generate());

        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void completeTransitionsFromPendingToCompleted() {
        Payment payment = new Payment(PaymentId.generate(), OrderId.generate());

        payment.complete();

        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    void completeThrowsWhenPaymentIsNotPending() {
        Payment payment = new Payment(PaymentId.generate(), OrderId.generate());
        payment.complete();

        assertThrows(IllegalStateException.class, payment::complete);
    }

    @Test
    void failTransitionsToFailed() {
        Payment payment = new Payment(PaymentId.generate(), OrderId.generate());

        payment.fail();

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }
}
