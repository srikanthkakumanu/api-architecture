package com.apidemo.mspatterns.interfaces.rest;

import com.apidemo.mspatterns.domain.Order;
import com.apidemo.mspatterns.domain.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class RestDtos {
    private RestDtos() {}

    public record CreateOrderRequest(@NotBlank String customerId, @NotEmpty List<@Valid ItemRequest> items, String actor) {}

    public record ItemRequest(@NotBlank String sku, @Positive int quantity, @NotNull BigDecimal unitPrice, @NotBlank String currency) {}

    public record ChangeStatusRequest(@NotNull OrderStatus status, String actor) {}

    public record OrderResponse(UUID id, String customerId, OrderStatus status, String total, int itemCount) {
        public static OrderResponse from(Order order) {
            return new OrderResponse(order.id(), order.customerId(), order.status(), order.total().toString(), order.itemCount());
        }
    }

    public record PublishResponse(int published) {}
}
