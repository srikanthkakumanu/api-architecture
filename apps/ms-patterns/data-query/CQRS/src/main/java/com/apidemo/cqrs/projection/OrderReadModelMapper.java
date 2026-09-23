package com.apidemo.cqrs.projection;

import com.apidemo.cqrs.events.DomainEventEntity;
import com.apidemo.cqrs.events.EventStore;
import com.apidemo.cqrs.query.OrderReadModelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = Instant.class)
public interface OrderReadModelMapper {
    @Mapping(target = "orderId", source = "payload.orderId")
    @Mapping(target = "customerId", source = "payload.customerId")
    @Mapping(target = "status", source = "payload.status")
    @Mapping(target = "total", source = "payload.total")
    @Mapping(target = "itemCount", source = "payload.itemCount")
    @Mapping(target = "lineSummary", source = "payload.lineSummary")
    @Mapping(target = "version", source = "payload.version")
    @Mapping(target = "lastEventId", source = "event.id")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    OrderReadModelEntity toReadModel(EventStore.OrderSnapshot payload, DomainEventEntity event);
}
