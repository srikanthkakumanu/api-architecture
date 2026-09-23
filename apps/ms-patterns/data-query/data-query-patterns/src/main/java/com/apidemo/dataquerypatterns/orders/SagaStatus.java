package com.apidemo.dataquerypatterns.orders;

public enum SagaStatus {
    STARTED,
    INVENTORY_RESERVED,
    PAYMENT_AUTHORIZED,
    COMPLETED,
    COMPENSATED
}
