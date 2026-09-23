package com.apidemo.mspatterns.application;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderCommand(String customerId, List<Item> items, String actor) {
    public record Item(String sku, int quantity, BigDecimal unitPrice, String currency) {}
}
