<a id="top"></a>

# Microservice Architecture

This document introduces microservice architecture as a learning path for this repository.

## Table of Contents

- [Definition](#definition)
- [When Microservices Help](#when-microservices-help)
- [Service Boundaries](#service-boundaries)
- [Data Ownership](#data-ownership)
- [Communication Styles](#communication-styles)
- [Operational Concerns](#operational-concerns)
- [Testing Strategy](#testing-strategy)
- [Learning Exercises](#learning-exercises)

## Definition

Microservice architecture splits a system into independently deployable services. Each service owns a business capability, exposes explicit contracts, and usually owns its own data.

The key shift from a modular monolith is that boundaries become network boundaries.

[Back to top](#top)

## When Microservices Help

Microservices can help when:

- Different parts of the system need independent deployment.
- Teams need clear service ownership.
- Some capabilities need independent scaling.
- Failure isolation is important.
- Technology choices need to vary by service.

They also add cost: networking, retries, observability, distributed data, deployment automation, and harder debugging.

[Back to top](#top)

## Service Boundaries

A service boundary should usually align with a business capability. Avoid splitting services only by technical layers such as controller, service, and repository.

Healthy service boundaries have:

- Clear ownership.
- A small public API.
- Private internal implementation.
- Independent data ownership.
- Explicit versioning and compatibility rules.

[Back to top](#top)

## Data Ownership

Each microservice should own its data. Other services should not directly read or write its database.

Common patterns:

- Service API for command and query access.
- Published events for state changes.
- Read models for reporting.
- Saga or process manager for multi-service workflows.
- Idempotent consumers for event processing.

[Back to top](#top)

## Communication Styles

Microservices communicate through explicit contracts:

- REST for resource-oriented APIs.
- gRPC for efficient, strongly typed service-to-service RPC.
- Messaging for asynchronous event-driven workflows.
- WebHooks for external callbacks.

The `grpc` project is useful for learning RPC contracts. The `rest-spring` project is useful for learning HTTP resource APIs.

[Back to top](#top)

## Operational Concerns

Microservices need operational maturity:

- Service discovery and configuration.
- Centralized logging.
- Metrics and tracing.
- Health checks.
- Deployment automation.
- Retry, timeout, and circuit breaker policies.
- Backward-compatible API changes.
- Security between services.

[Back to top](#top)

## Testing Strategy

Useful test levels:

- Unit tests inside each service.
- Integration tests with real adapters.
- Consumer-driven contract tests.
- End-to-end tests for critical journeys.
- Resilience tests for timeout, retry, and partial failure behavior.

[Back to top](#top)

## Learning Exercises

- Treat `rest-spring` as one service that owns TODO resources.
- Treat `grpc` as an internal service-to-service communication example.
- Add OpenAPI documentation for REST endpoints.
- Add protobuf contract evolution examples.
- Experiment with a second service that consumes TODO events.
- Document failure handling for REST and gRPC calls.

[Back to top](#top)
