package com.example.ecommerce.ordering.infrastructure;

import com.example.ecommerce.ordering.domain.Money;
import com.example.ecommerce.ordering.domain.OrderItem;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * JPA-mapped shadow of the pure {@link OrderItem} value object. The domain
 * model stays framework-free; only this infrastructure-layer type knows about JPA.
 */
@Embeddable
class OrderItemEmbeddable {

    private String productId;
    private String productName;
    private BigDecimal unitPriceAmount;
    private String unitPriceCurrency;
    private int quantity;

    protected OrderItemEmbeddable() {
        // required by JPA
    }

    OrderItemEmbeddable(String productId, String productName, BigDecimal unitPriceAmount,
                         String unitPriceCurrency, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPriceAmount = unitPriceAmount;
        this.unitPriceCurrency = unitPriceCurrency;
        this.quantity = quantity;
    }

    static OrderItemEmbeddable fromDomain(OrderItem item) {
        return new OrderItemEmbeddable(
                item.productId(),
                item.productName(),
                item.unitPrice().amount(),
                item.unitPrice().currency(),
                item.quantity()
        );
    }

    OrderItem toDomain() {
        return new OrderItem(productId, productName, new Money(unitPriceAmount, unitPriceCurrency), quantity);
    }
}
