<a id="top"></a>

# Event Driven Architecture

This document explains event-driven architecture, event-driven APIs, and common design patterns for modular monolith and microservice systems.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Event Driven APIs](#event-driven-apis)
- [Event Types](#event-types)
- [Design Patterns](#design-patterns)
- [Event-Driven Microservice Patterns](#event-driven-microservice-patterns)
- [Delivery and Consistency](#delivery-and-consistency)
- [Comparison of API Styles](#comparison-of-api-styles)
- [Architecture Learning Notes](#architecture-learning-notes)
- [Learning Exercises](#learning-exercises)

## Overview

Event-driven architecture is a style where systems communicate by producing and consuming events. An event represents something that already happened, such as `TodoCreated`, `PaymentAuthorized`, `OrderShipped`, or `CustomerEmailChanged`.

Instead of one component directly commanding every other component, producers publish events and consumers react to them. This can reduce coupling, improve extensibility, and support asynchronous workflows.

[Back to top](#top)

## Core Concepts

Key concepts:

- Event: A fact that something happened.
- Producer: The component or service that creates an event.
- Consumer: The component or service that reacts to an event.
- Broker: Infrastructure that receives, stores, and routes events.
- Topic or stream: A named event channel.
- Subscription: A consumer's interest in a topic or stream.
- Event schema: The structure and meaning of an event payload.

Events should be named in the past tense because they describe completed facts, not instructions.

[Back to top](#top)

## Event Driven APIs

Event-driven APIs expose or deliver changes as events. They are useful when clients need timely updates instead of repeatedly asking for current state.

Common event-driven API styles:

- Polling: A client repeatedly asks whether anything changed.
- WebHooks: A provider sends HTTP callbacks to a registered consumer URL.
- WebSockets: Client and server communicate over a long-lived two-way connection.
- HTTP streaming: A server sends updates over a long-lived HTTP response.
- Messaging APIs: Consumers receive events from a queue, topic, broker, or stream.

[Back to top](#top)

## Event Types

Common event types:

- Domain event: A business fact from the domain, such as `TodoCompleted`.
- Integration event: A cross-boundary event intended for other modules or services.
- Notification event: A lightweight signal that something changed.
- State-carried event: An event containing enough data for consumers to update local read models.
- Event-carried command: A message that looks like an event but is really asking another component to do work. Use carefully, because it can hide coupling.

[Back to top](#top)

## Design Patterns

Common event-driven design patterns include:

- Publish-subscribe: Producers publish events to a topic and many consumers can subscribe.
- Message queue: Producers send messages to a queue and one consumer handles each message.
- Event sourcing: Store state changes as an append-only sequence of events.
- CQRS: Separate command models from query/read models.
- Transactional outbox: Save state changes and outgoing events in the same database transaction.
- Inbox pattern: Store received message IDs so consumers can process messages idempotently.
- Saga: Coordinate a multi-step workflow across services using events and compensating actions.
- Choreography: Services react to events without a central orchestrator.
- Orchestration: A coordinator tells participants what step to perform next.
- Dead letter queue: Move repeatedly failing messages aside for investigation.

[Back to top](#top)

## Event-Driven Microservice Patterns

Some microservice patterns are especially important in event-driven systems.

### Saga

A Saga coordinates a business workflow that crosses service boundaries. Each service performs a local transaction and publishes an event. The next service reacts to that event and continues the workflow.

In choreography, services react to each other's events. In orchestration, a workflow coordinator decides which service should act next. Choreography can reduce central control, but it can become hard to understand. Orchestration is easier to follow, but the orchestrator becomes an important dependency.

### Database per Service

Database per Service supports event-driven architecture because each service owns its own state. When another service needs to know about a change, it consumes events instead of querying the owner's tables directly.

This creates eventual consistency. Consumers may see changes slightly later than the producer, so workflows must tolerate delays, retries, and duplicate messages.

### CQRS

CQRS is often paired with events. Commands update the owning service's write model, and events update separate read models optimized for queries.

For example, a `TodoCompleted` event could update a dashboard read model without making the dashboard service directly read the TODO service database.

### Event Sourcing

Event Sourcing stores the history of state changes as events. Instead of saving only the latest row, the system appends facts such as `TodoCreated`, `TodoRenamed`, and `TodoCompleted`.

This makes audit and replay natural, but it requires careful event versioning and read model design. Event Sourcing is powerful, but it should be chosen for a real need, not just because a system uses events.

### Circuit Breaker

Circuit Breaker is not an event pattern by itself, but it matters when event-driven services still make synchronous calls. If a consumer calls another service while processing an event, a circuit breaker can prevent repeated dependency failures from blocking message processing.

### Bulkhead

Bulkhead is useful for event consumers because message processing can be isolated by topic, consumer group, dependency, or workload type. A slow or failing event handler should not exhaust the same thread pool, queue capacity, or database connections needed by unrelated handlers.

For example, a failed notification workflow should not prevent payment events from being consumed. Bulkheads help preserve partial availability when one stream, dependency, or consumer path is unhealthy.

### Service Discovery, API Gateway, Sidecar, and Strangler Fig

These patterns support event-driven systems indirectly:

- Service Discovery helps event producers, consumers, and supporting services locate each other in dynamic environments.
- API Gateway can expose event-related HTTP APIs such as WebHook registration, event history, or streaming endpoints.
- Sidecar can provide service mesh routing, mutual TLS, metrics, and tracing around event producers and consumers.
- Strangler Fig can migrate a monolith toward event-driven services by routing selected workflows to new components and publishing integration events during the transition.

[Back to top](#top)

## Delivery and Consistency

Event-driven systems require explicit thinking about delivery guarantees and consistency.

Important concerns:

- At-most-once delivery: A message may be lost, but is not repeated.
- At-least-once delivery: A message is not lost, but may be repeated.
- Exactly-once effect: The business result happens once, usually through idempotency rather than magical transport guarantees.
- Ordering: Some consumers need events in sequence.
- Replay: Consumers may rebuild state by reading old events.
- Schema evolution: Event contracts must evolve without breaking existing consumers.
- Eventual consistency: Consumers may observe changes after a delay.

[Back to top](#top)

## Comparison of API Styles

| Style | Direction | Best Fit | Main Risk |
| --- | --- | --- | --- |
| Polling | Client to server | Simple status checks | Waste and stale data |
| WebHooks | Server to server | External event callbacks | Retries and trust |
| WebSockets | Two-way | Live interaction | Connection scaling |
| HTTP streaming | Server to client | One-way live feeds | Buffering and reconnects |
| Messaging | Service to service | Asynchronous workflows | Operational complexity |

[Back to top](#top)

## Architecture Learning Notes

In a modular monolith, events can be in-process domain events. They help one module react to another module without direct calls everywhere.

In microservices, events usually cross process boundaries through a broker or stream. This adds concerns such as delivery guarantees, idempotency, ordering, schema evolution, observability, and replay.

The same domain event idea can be practiced first inside a modular monolith and later promoted to integration events when a module becomes a separate service.

[Back to top](#top)

## Learning Exercises

- Add a `TodoCompleted` domain event inside `rest-spring`.
- Document whether the event is internal, integration-facing, or both.
- Add an outbox table for reliable event publishing.
- Add an idempotent consumer example.
- Compare a WebHook delivery flow with a broker-based event flow.
- Document retry and dead-letter handling for failed event processing.

[Back to top](#top)
