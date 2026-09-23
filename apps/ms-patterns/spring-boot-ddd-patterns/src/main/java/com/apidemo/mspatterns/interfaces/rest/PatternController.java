package com.apidemo.mspatterns.interfaces.rest;

import com.apidemo.mspatterns.application.OutboxPublisher;
import com.apidemo.mspatterns.application.Ports;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class PatternController {
    private final OutboxPublisher publisher;
    private final Ports.AuditLogRepository audit;

    public PatternController(OutboxPublisher publisher, Ports.AuditLogRepository audit) {
        this.publisher = publisher;
        this.audit = audit;
    }

    @PostMapping("/internal/outbox/publish")
    public RestDtos.PublishResponse publish() {
        return new RestDtos.PublishResponse(publisher.publishPending());
    }

    @GetMapping("/audit-logs")
    public List<Ports.AuditLog> auditLogs(@RequestParam UUID orderId) {
        return audit.findByOrderId(orderId);
    }
}
