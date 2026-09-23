package com.apidemo.mspatterns.interfaces.rest;

import com.apidemo.mspatterns.application.CreateOrderCommand;
import com.apidemo.mspatterns.application.OrderApplicationService;
import com.apidemo.mspatterns.infrastructure.observability.CorrelationId;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderApplicationService service;

    public OrderController(OrderApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestDtos.OrderResponse create(@Valid @RequestBody RestDtos.CreateOrderRequest request) {
        var command = new CreateOrderCommand(
                request.customerId(),
                request.items().stream()
                        .map(item -> new CreateOrderCommand.Item(item.sku(), item.quantity(), item.unitPrice(), item.currency()))
                        .toList(),
                actor(request.actor())
        );
        return RestDtos.OrderResponse.from(service.create(command, correlationId()));
    }

    @PatchMapping("/{id}/status")
    public RestDtos.OrderResponse changeStatus(@PathVariable UUID id, @Valid @RequestBody RestDtos.ChangeStatusRequest request) {
        return RestDtos.OrderResponse.from(service.changeStatus(id, request.status(), actor(request.actor()), correlationId()));
    }

    @GetMapping("/{id}")
    public RestDtos.OrderResponse get(@PathVariable UUID id) {
        return RestDtos.OrderResponse.from(service.get(id));
    }

    private static String correlationId() {
        return MDC.get(CorrelationId.MDC_KEY);
    }

    private static String actor(String actor) {
        return actor == null || actor.isBlank() ? "anonymous" : actor;
    }
}
