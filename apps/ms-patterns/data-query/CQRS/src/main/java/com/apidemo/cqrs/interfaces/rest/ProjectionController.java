package com.apidemo.cqrs.interfaces.rest;

import com.apidemo.cqrs.projection.OrderProjectionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/projections/orders")
public class ProjectionController {
    private final OrderProjectionService projections;

    public ProjectionController(OrderProjectionService projections) {
        this.projections = projections;
    }

    @PostMapping("/run")
    public RestDtos.ProjectionResponse run() {
        return new RestDtos.ProjectionResponse(projections.projectPending());
    }
}
