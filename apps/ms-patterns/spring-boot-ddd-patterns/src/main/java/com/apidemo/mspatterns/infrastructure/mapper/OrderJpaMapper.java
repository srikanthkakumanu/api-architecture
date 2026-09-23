package com.apidemo.mspatterns.infrastructure.mapper;

import com.apidemo.mspatterns.domain.Money;
import com.apidemo.mspatterns.domain.Order;
import com.apidemo.mspatterns.domain.OrderItem;
import com.apidemo.mspatterns.domain.OrderStatus;
import com.apidemo.mspatterns.infrastructure.persistence.OrderItemJpaEntity;
import com.apidemo.mspatterns.infrastructure.persistence.OrderJpaEntity;
import com.apidemo.mspatterns.infrastructure.persistence.OrderStatusJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderJpaMapper {
    @Mapping(target = "total", expression = "java(new Money(entity.totalAmount, entity.currency))")
    Order toDomain(OrderJpaEntity entity);

    @Mapping(target = "totalAmount", source = "total.amount")
    @Mapping(target = "currency", source = "total.currency")
    OrderJpaEntity toEntity(Order order);

    @Mapping(target = "unitPrice", expression = "java(new Money(entity.unitPrice, entity.order.currency))")
    OrderItem toDomain(OrderItemJpaEntity entity);

    @Mapping(target = "unitPrice", source = "unitPrice.amount")
    @Mapping(target = "order", ignore = true)
    OrderItemJpaEntity toEntity(OrderItem item);

    default OrderStatusJpa toJpa(OrderStatus status) {
        return OrderStatusJpa.valueOf(status.name());
    }

    default OrderStatus toDomain(OrderStatusJpa status) {
        return OrderStatus.valueOf(status.name());
    }

    default BigDecimal moneyAmount(Money money) {
        return money.amount();
    }
}
