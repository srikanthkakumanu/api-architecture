package com.apidemo.dataquerypatterns.web;

import com.apidemo.dataquerypatterns.outbox.OutboxMessageRepository;
import com.apidemo.dataquerypatterns.outbox.OutboxPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/outbox")
public class OutboxController {
    private final OutboxPublisher publisher;

    public OutboxController(OutboxPublisher publisher, OutboxMessageRepository outbox) {
        this.publisher = publisher;
    }

    @PostMapping("/publish")
    public RestDtos.PublishResponse publish() {
        return new RestDtos.PublishResponse(publisher.publishPending());
    }

    @PostMapping("/replay/{messageId}")
    public RestDtos.ReplayResponse replay(@PathVariable UUID messageId) {
        return new RestDtos.ReplayResponse(publisher.replay(messageId));
    }
}
