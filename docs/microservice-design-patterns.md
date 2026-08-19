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

[Back to top](#top)

## Communication Patterns

Common communication patterns include:

- API Gateway for a single client-facing entry point.
- Backend for Frontend for client-specific APIs.
- Synchronous REST calls for resource-oriented interactions.
- Synchronous gRPC calls for efficient internal RPC.
- Asynchronous messaging for event-driven workflows.
- Publish-subscribe for broadcasting domain events.

These patterns shape how services collaborate across the network.

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

[Back to top](#top)

## Learning Exercises

- Add an API Gateway note for routing to `rest-spring` and future services.
- Extend the `grpc` project with timeout and retry examples.
- Add an outbox table to `rest-spring` for TODO events.
- Document a Saga example for a multi-step workflow.
- Add correlation IDs to REST and gRPC calls.
- Compare database-per-service with modular-monolith table ownership.

[Back to top](#top)
