package com.example.ecommerce.ordering.infrastructure;

import com.example.ecommerce.ordering.domain.Order;
import com.example.ecommerce.ordering.domain.OrderId;
import com.example.ecommerce.ordering.domain.OrderItem;
import com.example.ecommerce.ordering.domain.OrderStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence model for the {@link Order} aggregate. The aggregate itself
 * carries zero framework imports; this entity exists purely so the
 * infrastructure layer can map it to and from relational storage.
 */
@Entity
@Table(name = "orders")
class OrderJpaEntity {

    @Id
    private String id;
    private String customerId;
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItemEmbeddable> items = new ArrayList<>();

    protected OrderJpaEntity() {
        // required by JPA
    }

    static OrderJpaEntity fromDomain(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.id = order.getId().value().toString();
        entity.customerId = order.getCustomerId();
        entity.shippingAddress = order.getShippingAddress();
        entity.status = order.getStatus();
        entity.items = new ArrayList<>(order.getItems().stream().map(OrderItemEmbeddable::fromDomain).toList());
        return entity;
    }

    Order toDomain() {
        List<OrderItem> orderItems = items.stream().map(OrderItemEmbeddable::toDomain).toList();
        return Order.reconstruct(OrderId.fromString(id), customerId, shippingAddress, orderItems, status);
    }
}
