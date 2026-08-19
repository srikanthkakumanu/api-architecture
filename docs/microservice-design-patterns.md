<a id="top"></a>

# Microservice Design Patterns

This document catalogs common microservice design patterns for learning, comparison, and future implementation examples in this repository.

## Table of Contents

- [Purpose](#purpose)
- [Decomposition Patterns](#decomposition-patterns)
- [Communication Patterns](#communication-patterns)
- [Data Management Patterns](#data-management-patterns)
- [Reliability Patterns](#reliability-patterns)
- [Observability Patterns](#observability-patterns)
- [Deployment Patterns](#deployment-patterns)
- [Pattern Summary](#pattern-summary)
- [Additional Pattern Placeholders](#additional-pattern-placeholders)
- [Learning Exercises](#learning-exercises)

## Purpose

Microservice design patterns help solve recurring problems that appear when an application is split into independently deployable services. They are useful, but they also add complexity, so each pattern should be connected to a real architectural force such as independent scaling, team ownership, failure isolation, or data ownership.

[Back to top](#top)

## Decomposition Patterns

Common decomposition patterns include:

- Decompose by business capability.
- Decompose by subdomain.
- Strangler Fig for incremental migration from a monolith.
- Anti-corruption layer to protect a new model from a legacy model.
- Bounded context to define model and language boundaries.

These patterns help decide where service boundaries should exist.

### Strangler Fig

The Strangler Fig pattern incrementally replaces an existing monolith by routing selected capabilities to new services while the old system continues to run. New functionality grows around the old application until enough behavior has moved away and the old code can be retired.

Use it when a full rewrite is too risky. It works well with an API Gateway or reverse proxy because traffic can be routed feature by feature.

Watch for duplicated business logic, inconsistent data ownership, and long transition periods where both old and new systems must stay compatible.

[Back to top](#top)

## Communication Patterns

Common communication patterns include:

- API Gateway for a single client-facing entry point.
- Service Discovery so services can locate each other dynamically.
- Backend for Frontend for client-specific APIs.
- Synchronous REST calls for resource-oriented interactions.
- Synchronous gRPC calls for efficient internal RPC.
- Asynchronous messaging for event-driven workflows.
- Publish-subscribe for broadcasting domain events.

These patterns shape how services collaborate across the network.

### API Gateway

An API Gateway is a single entry point for client traffic. It routes requests to the right backend service and may handle cross-cutting concerns such as authentication, TLS termination, rate limiting, request logging, response shaping, and protocol translation.

Use it when clients should not know every internal service address. It is especially helpful when mobile, browser, and third-party clients need a stable public API while internal services evolve.

Avoid putting too much business logic in the gateway. If every workflow lives there, it becomes a new monolith at the edge.

### Service Discovery

Service Discovery lets services find each other without hard-coded hostnames and ports. A service registers its network location with a registry, and clients or infrastructure resolve the current location at runtime.

Common approaches:

- Client-side discovery: The client asks a registry where to call.
- Server-side discovery: A load balancer or platform routes the request.
- Platform discovery: Kubernetes Services, DNS, or a service mesh provide discovery.

Use it when services scale dynamically, move between nodes, or run in containers where addresses are not stable.

[Back to top](#top)

## Data Management Patterns

Common data patterns include:

- Database per service.
- Saga for multi-service business transactions.
- Transactional outbox for reliable event publishing.
- Event sourcing for storing state as a sequence of events.
- CQRS for separating command and query models.
- Materialized view for service-owned read models.

These patterns help preserve service autonomy while handling workflows that cross service boundaries.

### Database per Service

Database per Service means each service owns its data store and other services cannot directly read or write that database. Other services must use the owning service's API, consume its events, or build their own read models.

This pattern protects service autonomy and prevents hidden coupling through shared tables. It also makes independent deployment safer because schema changes are owned by one service.

The cost is distributed data complexity. Joins across services become API calls, replicated read models, or reporting pipelines. Multi-service workflows need patterns such as Saga, CQRS, and event-driven integration.

### Saga

A Saga manages a business transaction that spans multiple services without using one global database transaction. Each step commits locally in one service. If a later step fails, earlier steps are undone through compensating actions.

Two common styles:

- Choreography: Services publish and react to events without a central coordinator.
- Orchestration: A coordinator tells each service which step to perform next.

Use Saga for workflows such as order placement, payment, inventory reservation, shipment, or onboarding. Design compensating actions carefully because not every real-world action can be perfectly undone.

### CQRS

CQRS, or Command Query Responsibility Segregation, separates the model used to change state from the model used to read state.

Commands validate intent and update the source of truth. Queries read from models optimized for lookup, reporting, or UI needs. In microservices, CQRS often appears with service-owned read models populated by events from other services.

Use CQRS when read and write needs are very different, queries are expensive, or consumers need denormalized views. Avoid it for simple CRUD services where one model is enough.

### Event Sourcing

Event Sourcing stores state as a sequence of events instead of only storing the latest state. The current state is rebuilt by replaying events.

For example, a TODO item might be represented by events such as `TodoCreated`, `TodoDescriptionChanged`, and `TodoCompleted`.

Use Event Sourcing when auditability, temporal history, replay, or complex domain behavior is important. The trade-off is higher complexity around event schema evolution, replay, snapshots, and query models.

[Back to top](#top)

## Reliability Patterns

Common reliability patterns include:

- Timeout to prevent waiting forever.
- Retry for transient failures.
- Circuit breaker to stop repeated calls to unhealthy dependencies.
- Bulkhead to isolate failures.
- Rate limiting to protect services from overload.
- Idempotent consumer to safely process repeated messages.

These patterns are essential because microservices fail partially and independently.

### Circuit Breaker

The Circuit Breaker pattern prevents a service from repeatedly calling an unhealthy dependency. After failures cross a threshold, the circuit opens and calls fail fast or return a fallback. After a delay, the circuit moves to half-open and allows a small number of trial calls.

Use it for remote calls to services, databases, external APIs, and message brokers where repeated failure can exhaust threads, connection pools, or user patience.

Circuit breakers work best with timeouts, retries, bulkheads, and clear fallback behavior. They should protect the caller without hiding real outages from observability.

### Bulkhead

The Bulkhead pattern isolates resources so one failing dependency, workflow, or tenant cannot consume everything the service needs to keep running. The name comes from ship compartments: damage in one compartment should not sink the whole ship.

In software, bulkheads are often implemented with separate thread pools, connection pools, queues, rate limits, or worker groups. For example, calls to a slow payment service should not consume the same execution pool used by login, health checks, or core reads.

Use it when a service handles multiple dependency calls or workloads with different reliability needs. Bulkheads pair well with circuit breakers because the circuit breaker stops repeated failing calls, while the bulkhead limits how much damage those calls can cause before the circuit opens.

[Back to top](#top)

## Observability Patterns

Common observability patterns include:

- Health check endpoint.
- Centralized logging.
- Distributed tracing.
- Metrics collection.
- Correlation IDs across service calls.
- Audit logging for important business actions.

These patterns make distributed behavior understandable during development and production operations.

[Back to top](#top)

## Deployment Patterns

Common deployment patterns include:

- Service instance per host or container.
- Sidecar for supporting capabilities near a service.
- Blue-green deployment.
- Canary release.
- Rolling deployment.
- Externalized configuration.

These patterns help services change independently with less operational risk.

### Sidecar

The Sidecar pattern deploys a helper process beside a service. The sidecar provides supporting capabilities without embedding that code into the service itself.

Common sidecar responsibilities:

- Service mesh proxying.
- TLS and mutual TLS.
- Logging or metrics forwarding.
- Configuration refresh.
- Local adapters for external systems.

Use it when the same operational behavior is needed across many services. Be careful that sidecars increase runtime complexity and resource usage.

[Back to top](#top)

## Pattern Summary

| Pattern | Primary Problem | Typical Use |
| --- | --- | --- |
| API Gateway | Client entry point and request routing | Public API edge for many internal services |
| Circuit Breaker | Repeated remote dependency failures | Protect callers from unhealthy dependencies |
| Bulkhead | Failure spreading through shared resources | Isolate thread pools, queues, or connection pools |
| Saga | Multi-service business transaction | Long-running workflows with local commits |
| Service Discovery | Dynamic service locations | Containerized or elastic service environments |
| Database per Service | Hidden data coupling | Independent service data ownership |
| CQRS | Different read and write needs | Denormalized read models and complex queries |
| Event Sourcing | Need complete state history | Audit, replay, and event-first domain models |
| Sidecar | Shared operational capabilities | Service mesh, logging, security, config helpers |
| Strangler Fig | Incremental monolith migration | Gradual replacement of legacy functionality |

[Back to top](#top)

## Additional Pattern Placeholders

These placeholders are reserved for future notes.

### API Edge and Gateway Patterns

- Backend for Frontend
- API Composition
- Gateway Routing
- Gateway Aggregation
- Gateway Offloading
- Ambassador
- Adapter
- Aggregator

### Reliability and Traffic Patterns

- Retry
- Timeout
- Rate Limiting
- Throttling
- Load Balancing
- Backpressure

### Messaging and Event Patterns

- Transactional Outbox
- Inbox
- Idempotent Consumer
- Competing Consumers
- Claim Check
- Dead Letter Queue
- Retry Queue
- Priority Queue
- Event-Carried State Transfer
- Choreography
- Orchestration

### Data and Query Patterns

- Materialized View
- Sharding
- Cache-Aside
- Read Replica

### Deployment and Migration Patterns

- Canary Release
- Blue-Green Deployment
- Branch by Abstraction
- Externalized Configuration
- Config Server

### Platform and Operations Patterns

- Health Check API
- Service Mesh
- Sidecar
- Leader Election

### Security and Observability Patterns

- Token Relay
- Correlation ID
- Distributed Tracing
- Log Aggregation
- Centralized Audit Logging

### Boundary and Testing Patterns

- Anti-Corruption Layer
- Consumer-Driven Contract

[Back to top](#top)

## Learning Exercises

- Add an API Gateway note for routing to `rest-spring` and future services.
- Extend the `grpc` project with timeout and retry examples.
- Add an outbox table to `rest-spring` for TODO events.
- Document a Saga example for a multi-step workflow.
- Add correlation IDs to REST and gRPC calls.
- Compare database-per-service with modular-monolith table ownership.

[Back to top](#top)
