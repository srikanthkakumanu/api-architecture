package com.apidemo.cqrs.command;

import java.util.UUID;

public record CommandResult(UUID orderId, OrderStatus status, String total, UUID eventId) {
}
