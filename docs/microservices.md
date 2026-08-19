<a id="top"></a>

# Microservice Architecture

This document introduces microservice architecture as a learning path for this repository.

## Table of Contents

- [Definition](#definition)
- [When Microservices Help](#when-microservices-help)
- [Service Boundaries](#service-boundaries)
- [Data Ownership](#data-ownership)
- [Communication Styles](#communication-styles)
- [Twelve-Factor App Methodology](#twelve-factor-app-methodology)
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

## Twelve-Factor App Methodology

The Twelve-Factor App methodology is a set of practices for building applications that are portable, repeatable, and easier to operate in modern deployment environments. It is not the same thing as microservice architecture, but it fits microservices very well because each service should be independently built, configured, deployed, scaled, and observed.

The twelve factors are:

| Factor | Meaning for a Microservice |
| --- | --- |
| Codebase | One service should have one tracked codebase that can be deployed to multiple environments. |
| Dependencies | Declare dependencies explicitly instead of relying on tools or libraries installed on a machine. |
| Config | Store environment-specific configuration outside the code, usually in environment variables or platform configuration. |
| Backing services | Treat databases, queues, caches, and external APIs as attached resources that can change by configuration. |
| Build, release, run | Separate building an artifact, combining it with configuration, and running it. |
| Processes | Run the service as one or more stateless processes. |
| Port binding | Expose the service through a port instead of depending on an external application server. |
| Concurrency | Scale by adding more processes or instances. |
| Disposability | Start quickly and shut down gracefully. |
| Dev/prod parity | Keep development, staging, and production as similar as practical. |
| Logs | Write logs as event streams and let the platform collect and route them. |
| Admin processes | Run one-off tasks, such as migrations, using the same code and configuration model. |

Its relationship to cloud-native architecture is direct: cloud-native systems assume automated deployment, elastic scaling, externalized configuration, managed backing services, logs and metrics collected by the platform, and fast replacement of unhealthy instances. Twelve-Factor gives practical application-level habits that support those cloud-native expectations.

Its relationship to microservices is also practical. A microservice that follows Twelve-Factor principles is easier to deploy independently, scale horizontally, move between environments, recover after failure, and operate through container platforms such as Kubernetes or cloud application platforms.

In this repository, `rest-spring` can demonstrate several factors:

- Dependencies: Gradle declares application dependencies.
- Config: `application.yml` can evolve toward environment-provided configuration.
- Backing services: PostgreSQL is an attached resource.
- Port binding: Spring Boot exposes the app through an embedded web server.
- Logs: Spring Boot writes logs that can be collected by the runtime platform.
- Admin processes: Flyway migrations represent database administration tasks tied to the application lifecycle.

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
