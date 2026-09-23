package com.apidemo.cqrs.interfaces.rest;

import com.apidemo.cqrs.query.OrderQueryService;
import com.apidemo.cqrs.query.OrderReadModelEntity;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/queries/orders")
public class OrderQueryController {
    private final OrderQueryService service;
    private final QueryApiMapper mapper;

    public OrderQueryController(OrderQueryService service, QueryApiMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public RestDtos.OrderQueryResponse get(@PathVariable UUID id) {
        return mapper.toResponse(service.get(id));
    }

    @GetMapping
    public List<RestDtos.OrderQueryResponse> search(@RequestParam(required = false) String customerId, @RequestParam(required = false) String status) {
        return service.search(customerId, status).stream().map(mapper::toResponse).toList();
    }

    @Mapper(componentModel = "spring")
    public interface QueryApiMapper {
        RestDtos.OrderQueryResponse toResponse(OrderReadModelEntity entity);
    }
}
