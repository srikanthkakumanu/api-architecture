package com.apidemo.dataquerypatterns.web;

import com.apidemo.dataquerypatterns.orders.OrderRepository;
import com.apidemo.dataquerypatterns.saga.OrderSagaService;
import jakarta.validation.Valid;
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
public class OrderSagaController {
    private final OrderSagaService saga;
    private final OrderRepository orders;

    public OrderSagaController(OrderSagaService saga, OrderRepository orders) {
        this.saga = saga;
        this.orders = orders;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestDtos.OrderResponse create(@Valid @RequestBody RestDtos.CreateOrderRequest request) {
        return RestDtos.OrderResponse.from(saga.placeOrder(new OrderSagaService.CreateOrderCommand(request.customerId(), request.totalAmount(), request.currency())));
    }

    @PatchMapping("/{id}/advance")
    public RestDtos.OrderResponse advance(@PathVariable UUID id) {
        return RestDtos.OrderResponse.from(saga.advance(id));
    }

    @GetMapping("/{id}")
    public RestDtos.OrderResponse get(@PathVariable UUID id) {
        return RestDtos.OrderResponse.from(orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found")));
    }
}
