package com.apidemo.cqrs.projection;

import com.apidemo.cqrs.events.DomainEventRepository;
import com.apidemo.cqrs.events.EventStore;
import com.apidemo.cqrs.query.OrderReadModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderProjectionService {
    private final DomainEventRepository events;
    private final EventStore eventStore;
    private final OrderReadModelRepository readModels;
    private final OrderReadModelMapper mapper;

    public OrderProjectionService(DomainEventRepository events, EventStore eventStore, OrderReadModelRepository readModels, OrderReadModelMapper mapper) {
        this.events = events;
        this.eventStore = eventStore;
        this.readModels = readModels;
        this.mapper = mapper;
    }

    @Transactional
    public int projectPending() {
        var pending = events.findTop100ByProjectedFalseOrderByCreatedAtAsc();
        pending.forEach(event -> {
            var payload = eventStore.readPayload(event);
            readModels.save(mapper.toReadModel(payload, event));
            event.projected = true;
            event.projectedAt = Instant.now();
            events.save(event);
        });
        return pending.size();
    }
}
