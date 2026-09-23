package com.apidemo.cqrs.interfaces.rest;

import com.apidemo.cqrs.command.CommandResult;
import com.apidemo.cqrs.command.OrderCommandService;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/commands/orders")
public class OrderCommandController {
    private final OrderCommandService service;
    private final CommandApiMapper mapper;

    public OrderCommandController(OrderCommandService service, CommandApiMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestDtos.CommandResponse create(@Valid @RequestBody RestDtos.CreateOrderRequest request) {
        return mapper.toResponse(service.createOrder(mapper.toCommand(request)));
    }

    @PatchMapping("/{id}/status")
    public RestDtos.CommandResponse changeStatus(@PathVariable UUID id, @Valid @RequestBody RestDtos.ChangeStatusRequest request) {
        return mapper.toResponse(service.changeStatus(id, request.status()));
    }

    @Mapper(componentModel = "spring")
    public interface CommandApiMapper {
        OrderCommandService.CreateOrderCommand toCommand(RestDtos.CreateOrderRequest request);

        RestDtos.CommandResponse toResponse(CommandResult result);
    }
}
