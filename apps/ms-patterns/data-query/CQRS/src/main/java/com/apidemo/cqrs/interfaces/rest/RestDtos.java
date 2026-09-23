package com.apidemo.cqrs.interfaces.rest;

import com.apidemo.cqrs.command.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class RestDtos {
    private RestDtos() {
    }

    public record CreateOrderRequest(@NotBlank String customerId, @NotEmpty List<@Valid OrderLineRequest> items) {
    }

    public record OrderLineRequest(@NotBlank String sku, @Positive int quantity, @NotNull BigDecimal unitPrice, @NotBlank String currency) {
    }

    public record ChangeStatusRequest(@NotNull OrderStatus status) {
    }

    public record CommandResponse(UUID orderId, OrderStatus status, String total, UUID eventId) {
    }

    public record OrderQueryResponse(UUID orderId, String customerId, String status, String total, int itemCount, String lineSummary, long version, UUID lastEventId) {
    }

    public record ProjectionResponse(int projected) {
    }
}
