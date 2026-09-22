<a id="top"></a>
# Microservice Design Patterns

This catalog organizes common microservice design patterns by the architectural problem they solve. Use it as a learning reference, a comparison guide, and a backlog of implementation examples for this repository.

---

## Table of Contents

- [Purpose and Selection Guidance](#purpose-and-selection-guidance)
  - [Quick Reading Guide](#quick-reading-guide)
- [Boundary and Decomposition Patterns](#boundary-and-decomposition-patterns)
  - [Decompose by Business Capability](#decompose-by-business-capability)
  - [Decompose by Subdomain](#decompose-by-subdomain)
  - [Bounded Context](#bounded-context)
  - [Anti-Corruption Layer](#anti-corruption-layer)
  - [Strangler Fig](#strangler-fig)
- [API Edge and Client-Facing Patterns](#api-edge-and-client-facing-patterns)
  - [API Gateway](#api-gateway)
  - [Backend for Frontend](#backend-for-frontend)
  - [API Composition](#api-composition)
- [Service Communication Patterns](#service-communication-patterns)
  - [Service Discovery](#service-discovery)
  - [Synchronous Request-Response](#synchronous-request-response)
  - [Asynchronous Messaging](#asynchronous-messaging)
  - [Publish-Subscribe](#publish-subscribe)
- [Data Ownership, Consistency, and Query Patterns](#data-ownership-consistency-and-query-patterns)
  - [Database per Service](#database-per-service)
  - [Saga](#saga)
  - [Transactional Outbox](#transactional-outbox)
  - [Inbox and Idempotent Consumer](#inbox-and-idempotent-consumer)
  - [CQRS](#cqrs)
  - [Event Sourcing](#event-sourcing)
  - [Materialized View](#materialized-view)
  - [Cache-Aside](#cache-aside)
- [Reliability and Traffic Management Patterns](#reliability-and-traffic-management-patterns)
  - [Timeout](#timeout)
  - [Retry](#retry)
  - [Circuit Breaker](#circuit-breaker)
  - [Bulkhead](#bulkhead)
  - [Rate Limiting and Throttling](#rate-limiting-and-throttling)
  - [Backpressure](#backpressure)
  - [Dead Letter Queue](#dead-letter-queue)
- [Observability and Operations Patterns](#observability-and-operations-patterns)
  - [Health Check API](#health-check-api)
  - [Correlation ID](#correlation-id)
  - [Distributed Tracing](#distributed-tracing)
  - [Centralized Logging](#centralized-logging)
  - [Metrics and Alerting](#metrics-and-alerting)
  - [Audit Logging](#audit-logging)
- [Deployment, Runtime, and Migration Patterns](#deployment-runtime-and-migration-patterns)
  - [Sidecar](#sidecar)
  - [Service Mesh](#service-mesh)
  - [Externalized Configuration](#externalized-configuration)
  - [Blue-Green Deployment](#blue-green-deployment)
  - [Canary Release](#canary-release)
  - [Rolling Deployment](#rolling-deployment)
  - [Branch by Abstraction](#branch-by-abstraction)
- [Security Patterns](#security-patterns)
  - [Token Relay](#token-relay)
  - [Gateway Offloading](#gateway-offloading)
- [Testing Patterns](#testing-patterns)
  - [Consumer-Driven Contract](#consumer-driven-contract)
- [Pattern Summary](#pattern-summary)
- [Learning Exercises](#learning-exercises)

[Back To Top](#top)

---

## Purpose and Selection Guidance

Microservice patterns solve recurring problems that appear when applications are split into independently deployable services. They are useful when they answer a real architectural force such as independent scaling, team ownership, failure isolation, data ownership, release safety, or operational visibility.

Do not apply a pattern only because it is popular. Each pattern trades one kind of complexity for another. A good pattern choice should explain:

- Which coupling or failure mode it reduces.
- Which service, team, or data boundary it protects.
- Which new operational responsibilities it introduces.
- How the pattern will be observed, tested, and rolled back.

[Back To Top](#top)

---

### Quick Reading Guide

Use the catalog by starting with the problem you need to solve, then follow the linked pattern family:

| If You Need To... | Start With | Related Patterns |
| --- | --- | --- |
| Define service ownership and boundaries | [Boundary and Decomposition Patterns](#boundary-and-decomposition-patterns) | Bounded Context, Anti-Corruption Layer, Strangler Fig |
| Provide a stable API to different clients | [API Edge and Client-Facing Patterns](#api-edge-and-client-facing-patterns) | API Gateway, Backend for Frontend, API Composition |
| Choose synchronous or asynchronous interaction | [Service Communication Patterns](#service-communication-patterns) | Request-Response, Messaging, Publish-Subscribe |
| Coordinate data across service boundaries | [Data Ownership, Consistency, and Query Patterns](#data-ownership-consistency-and-query-patterns) | [Saga](saga.md), Transactional Outbox, [CQRS](cqrs.md) |
| Prevent dependency failures from spreading | [Reliability and Traffic Management Patterns](#reliability-and-traffic-management-patterns) | Timeout, Retry, Circuit Breaker, [Bulkhead](bulkhead.md) |
| Diagnose distributed behavior | [Observability and Operations Patterns](#observability-and-operations-patterns) | Correlation ID, Tracing, Logging, Metrics |
| Release or migrate services safely | [Deployment, Runtime, and Migration Patterns](#deployment-runtime-and-migration-patterns) | Canary Release, Blue-Green Deployment, Strangler Fig |
| Protect APIs and service contracts | [Security Patterns](#security-patterns) and [Testing Patterns](#testing-patterns) | Token Relay, Gateway Offloading, Consumer-Driven Contract |

For deeper examples, continue with the dedicated guides for the [Saga pattern](saga.md), [CQRS pattern](cqrs.md), and [Bulkhead pattern](bulkhead.md).

[Back To Top](#top)

---

## Boundary and Decomposition Patterns

Boundary patterns help decide where services should exist. They are usually the first design decision because poor boundaries make every later communication, data, and reliability pattern harder.

[Back To Top](#top)

---

### Decompose by Business Capability

Split services around stable business activities, such as catalog, ordering, payment, inventory, shipping, billing, or identity.

**Use when:** Team and product ownership already map to business capabilities, and each team can own behavior end to end.

**Watch for:** Capabilities that are too broad. A service named after an entire department can become a distributed monolith if it owns unrelated workflows.

[Back To Top](#top)

---

### Decompose by Subdomain

Split services around domain-driven design subdomains: core, supporting, and generic. Core subdomains contain differentiating business logic; supporting and generic subdomains can often use simpler designs or managed products.

**Use when:** The domain has meaningful complexity and needs precise modeling.

**Watch for:** Premature splitting before the domain language is understood. Start with a modular boundary when the model is still changing quickly.

[Back To Top](#top)

---

### Bounded Context

A Bounded Context defines where a domain model, language, and set of rules are valid. The same word may mean different things in different contexts; for example, customer in billing may differ from customer in support.

**Use when:** Teams need autonomy over their model and schema. A bounded context can become one service, several services, or a module inside a larger service.

**Watch for:** Shared domain objects crossing boundaries. Sharing internal models creates tight coupling even when services are deployed separately.

[Back To Top](#top)

---

### Anti-Corruption Layer

An Anti-Corruption Layer translates between two models so one service is not forced to adopt another system's concepts directly. It is common when integrating with legacy systems, third-party APIs, or another bounded context.

**Use when:** The upstream model is unstable, awkward, or semantically different from the service's own model.

**Watch for:** The layer becoming a dumping ground for business logic. Keep translation explicit and test it heavily because it protects the domain boundary.

[Back To Top](#top)

---

### Strangler Fig

The Strangler Fig pattern incrementally replaces an existing monolith by routing selected capabilities to new services while the old system continues to run. New functionality grows around the old application until enough behavior has moved away and the old code can be retired.

**Use when:** A full rewrite is too risky. It works well with an API Gateway or reverse proxy because traffic can be routed feature by feature.

**Watch for:** Duplicated business logic, inconsistent data ownership, and long transition periods where old and new systems must remain compatible.

[Back To Top](#top)

---

## API Edge and Client-Facing Patterns

API edge patterns shape how external clients interact with a service landscape. They hide internal topology and let public APIs evolve at a different pace than internal service APIs.

[Back To Top](#top)

---

### API Gateway

An API Gateway is a single entry point for client traffic. It routes requests to backend services and may handle authentication, TLS termination, rate limiting, request logging, response shaping, and protocol translation.

**Use when:** Clients need a stable public API and should not know every internal service address.

**Watch for:** Business logic accumulating in the gateway. If every workflow lives there, the gateway becomes a new monolith at the edge.

[Back To Top](#top)

---

### Backend for Frontend

Backend for Frontend creates a dedicated edge API for a specific client experience, such as web, mobile, partner API, or admin UI. Each BFF can shape responses and workflows around one client without forcing every client through the same contract.

**Use when:** Clients have meaningfully different data, latency, authentication, or versioning needs.

**Watch for:** Duplicated logic across BFFs. Shared domain decisions belong in domain services, not client-specific adapters.

[Back To Top](#top)

---

### API Composition

API Composition builds a response by calling multiple services and combining their data. It is often used for read-heavy screens that need data owned by different services.

**Use when:** A client needs a simple read API but no single service owns the whole view.

**Watch for:** Chatty calls and partial failure. Composition often needs timeouts, fallbacks, caching, and clear ownership of the response contract.

[Back To Top](#top)

---

## Service Communication Patterns

Communication patterns define how services find each other and collaborate across process and network boundaries.

[Back To Top](#top)

---

### Service Discovery

Service Discovery lets services find each other without hard-coded hostnames and ports. A service registers its network location with a registry, and clients or infrastructure resolve the current location at runtime.

Common approaches include client-side discovery, server-side discovery through a load balancer, and platform discovery through Kubernetes Services, DNS, or a service mesh.

**Use when:** Services scale dynamically, move between nodes, or run in containers where addresses are not stable.

**Watch for:** Stale registrations and slow failure detection. Discovery health checks, deregistration, and client-side caching need compatible timing.

[Back To Top](#top)

---

### Synchronous Request-Response

Synchronous request-response uses protocols such as REST, GraphQL, or gRPC when a caller needs an immediate answer before continuing.

**Use when:** Handling queries, validation, or commands that need a quick outcome and have clear latency and availability expectations.

**Watch for:** Long call chains. A request that crosses many services multiplies latency and can make availability worse than any single service.

[Back To Top](#top)

---

### Asynchronous Messaging

Asynchronous messaging sends work through a broker or queue so producers and consumers do not need to be available at the same time.

**Use when:** Handling background work, long-running workflows, integration events, or load leveling.

**Watch for:** Duplicate delivery, ordering assumptions, poison messages, and eventual consistency. Consumers should usually be idempotent.

[Back To Top](#top)

---

### Publish-Subscribe

Publish-subscribe broadcasts events to multiple subscribers without the publisher knowing who consumes them. It is useful for domain events such as order placed, payment captured, or shipment dispatched.

**Use when:** Many services need to react independently to the same business fact.

**Watch for:** Event schema drift and hidden coupling. Events are contracts and need versioning discipline.

[Back To Top](#top)

---

## Data Ownership, Consistency, and Query Patterns

Data patterns protect service autonomy while handling workflows and views that cross service boundaries.

[Back To Top](#top)

---

### Database per Service

Each service owns its data store, and other services cannot directly read or write that database. They must use the owning service's API, consume its events, or build their own read models.

**Use when:** Services need independent schemas, releases, and scaling decisions.

**Watch for:** Distributed query complexity. Joins across services become API calls, replicated read models, or reporting pipelines.

[Back To Top](#top)

---

### Saga

A Saga manages a business transaction that spans multiple services without using one global database transaction. Each step commits locally. If a later step fails, earlier steps are addressed through compensating actions.

Two common styles are choreography, where services publish and react to events, and orchestration, where a coordinator tells each service which step to perform next.

**Use when:** A workflow such as order placement, payment, inventory reservation, shipment, or onboarding crosses service-owned data.

**Watch for:** Compensating actions that cannot perfectly undo real-world effects.

**Detailed guide:** [Saga Pattern](saga.md)

[Back To Top](#top)

---

### Transactional Outbox

Transactional Outbox stores an event in the same local transaction as the business state change. A separate relay later publishes the event to a broker.

**Use when:** A service must update its database and publish an event reliably without a distributed transaction.

**Watch for:** Duplicate publication. Consumers should combine this pattern with Inbox or Idempotent Consumer.

[Back To Top](#top)

---

### Inbox and Idempotent Consumer

Inbox stores received message identifiers so a consumer can safely ignore duplicates. Idempotent Consumer designs handling so processing the same message more than once produces the same final result.

**Use when:** Consuming messages from brokers that provide at-least-once delivery.

**Watch for:** Idempotency gaps in external calls such as payments, emails, and inventory reservations. Those operations may need their own idempotency keys.

[Back To Top](#top)

---

### CQRS

CQRS, or Command Query Responsibility Segregation, separates the model used to change state from the model used to read state. Commands validate intent and update the source of truth; queries read from models optimized for lookup, reporting, or UI needs.

**Use when:** Read and write needs are very different, queries are expensive, or consumers need denormalized views.

**Watch for:** Unnecessary complexity in simple CRUD services where one model is enough.

**Detailed guide:** [CQRS Pattern](cqrs.md)

[Back To Top](#top)

---

### Event Sourcing

Event Sourcing stores state as a sequence of events instead of only storing the latest state. The current state is rebuilt by replaying events. For example, a TODO item might use `TodoCreated`, `TodoDescriptionChanged`, and `TodoCompleted` events.

**Use when:** Auditability, temporal history, replay, or complex domain behavior is important.

**Watch for:** Event schema evolution, replay performance, snapshots, and query model complexity.

[Back To Top](#top)

---

### Materialized View

A Materialized View stores a precomputed read model for a query or screen. In microservices, events from the owning services commonly update it.

**Use when:** Reads need data from multiple services but synchronous API composition would be too slow or fragile.

**Watch for:** Staleness. Consumers should know whether the view is eventually consistent and how freshness is measured.

[Back To Top](#top)

---

### Cache-Aside

Cache-Aside lets application code check a cache before reading from the source of truth. On a miss, the service reads from the database and stores the result in the cache.

**Use when:** Reads are expensive or frequently repeated.

**Watch for:** Stale data, cache stampedes, and accidental use of the cache as the source of truth.

[Back To Top](#top)

---

## Reliability and Traffic Management Patterns

Reliability patterns limit the blast radius of partial failure. They should be designed with clear deadlines, observable failure modes, and business-aware fallback behavior.

[Back To Top](#top)

---

### Timeout

Timeout limits how long a caller waits for a dependency before failing or falling back.

**Use when:** Making any remote call, including service, database, broker, and external API calls.

**Watch for:** Timeouts longer than the caller's own deadline. A timeout should support the user journey, not just the dependency.

[Back To Top](#top)

---

### Retry

Retry repeats a failed operation when the failure is likely transient. Retries usually need a small limit, exponential backoff, and jitter.

**Use when:** Handling temporary network failures, rate-limit responses with retry guidance, or optimistic concurrency conflicts.

**Watch for:** Retry storms. Do not retry non-idempotent operations without idempotency keys or safe deduplication.

[Back To Top](#top)

---

### Circuit Breaker

Circuit Breaker prevents a service from repeatedly calling an unhealthy dependency. After failures cross a threshold, the circuit opens and calls fail fast or return a fallback. After a delay, it becomes half-open and allows a small number of trial calls.

**Use when:** Repeated dependency failures could exhaust threads, connection pools, or user patience.

**Watch for:** Breakers that hide outages. Combine them with timeouts, bounded retries, useful metrics, and clear fallback behavior.

[Back To Top](#top)

---

### Bulkhead

Bulkhead isolates resources so one failing dependency, workflow, or tenant cannot consume everything the service needs to keep running. Common implementations use separate thread pools, connection pools, queues, rate limits, or worker groups.

**Use when:** A service handles workloads or dependency calls with different reliability needs.

**Watch for:** Poorly sized partitions that waste resources or move the bottleneck elsewhere.

**Detailed guide:** [Bulkhead Pattern](bulkhead.md)

[Back To Top](#top)

---

### Rate Limiting and Throttling

Rate limiting caps how many requests a caller can make in a time window. Throttling slows or rejects requests when a service is under pressure.

**Use when:** Protecting public APIs, expensive operations, tenant fairness, or downstream dependencies.

**Watch for:** Unclear client behavior. Good limits return useful response codes and retry guidance.

[Back To Top](#top)

---

### Backpressure

Backpressure signals producers to slow down when consumers cannot keep up. It can appear as queue limits, streaming demand signals, rejected requests, or adaptive throttling.

**Use when:** Unbounded work queues could turn temporary overload into a prolonged outage.

**Watch for:** Silent buffering. Queues should have limits, metrics, and clear overflow behavior.

[Back To Top](#top)

---

### Dead Letter Queue

A Dead Letter Queue stores messages that cannot be processed successfully after retries. It gives operators a place to inspect, fix, replay, or discard failed messages.

**Use when:** Poison messages should not block an asynchronous stream.

**Watch for:** Ignored dead letters. A DLQ needs ownership, alerts, retention rules, and replay procedures.

[Back To Top](#top)

---

## Observability and Operations Patterns

Observability patterns make distributed behavior understandable during development, testing, and production operations.

[Back To Top](#top)

---

### Health Check API

Health Check API exposes service health for load balancers, orchestrators, and operators. Common checks include liveness, readiness, and dependency health.

**Use when:** Unhealthy instances must be removed from traffic or recovered automatically.

**Watch for:** Expensive checks. A health endpoint should not create the failure it is trying to detect.

[Back To Top](#top)

---

### Correlation ID

Correlation ID attaches a shared identifier to logs, traces, events, and downstream calls that belong to the same request or workflow.

**Use when:** A user action or business workflow crosses service boundaries.

**Watch for:** Missing propagation across asynchronous messages and scheduled jobs.

[Back To Top](#top)

---

### Distributed Tracing

Distributed Tracing records a request path across services with spans, timings, and metadata.

**Use when:** Debugging latency, partial failures, or unexpected dependency chains.

**Watch for:** High-cardinality attributes and sensitive data in spans.

[Back To Top](#top)

---

### Centralized Logging

Centralized Logging collects logs from all services into a searchable platform. Useful fields include service name, environment, correlation ID, severity, and relevant business identifiers.

**Use when:** Investigating incidents and comparing behavior across instances or services.

**Watch for:** Unstructured logs that cannot be queried effectively, and sensitive data that should not be recorded.

[Back To Top](#top)

---

### Metrics and Alerting

Metrics capture numeric signals such as request rate, error rate, latency, saturation, queue depth, and business counters. Alerting turns important signals into operator action.

**Use when:** Tracking system health, user impact, and capacity trends.

**Watch for:** Noisy alerts. Alert on actionable symptoms and user impact, not every internal fluctuation.

[Back To Top](#top)

---

### Audit Logging

Audit Logging records important business or security actions, including who acted, what changed, when it happened, and where the request came from.

**Use when:** Supporting compliance, dispute resolution, or sensitive workflows such as access changes and payments.

**Watch for:** Privacy and retention requirements. Audit logs need stronger integrity and access controls than ordinary application logs.

[Back To Top](#top)

---

## Deployment, Runtime, and Migration Patterns

Deployment and runtime patterns help services change independently with less operational risk.

[Back To Top](#top)

---

### Sidecar

Sidecar deploys a helper process beside a service. It provides supporting capabilities without embedding that code into the service itself, such as proxying, TLS, telemetry forwarding, configuration refresh, or local adapters.

**Use when:** The same operational behavior is needed across services implemented with different technology stacks.

**Watch for:** Increased runtime complexity, resource usage, and version compatibility between the service and its sidecar.

[Back To Top](#top)

---

### Service Mesh

Service Mesh provides infrastructure-level traffic management, security, and observability for service-to-service communication, often through sidecar or node-level proxies.

**Use when:** Many services need consistent mTLS, traffic splitting, tracing, or policy enforcement.

**Watch for:** Platform complexity. A mesh should solve a platform-wide problem, not compensate for unclear service ownership.

[Back To Top](#top)

---

### Externalized Configuration

Externalized Configuration keeps environment-specific settings outside the application artifact.

**Use when:** The same build must run across environments with different endpoints, credentials, feature flags, or limits.

**Watch for:** Configuration drift. Configuration should be versioned, validated, secured, and observable.

[Back To Top](#top)

---

### Blue-Green Deployment

Blue-Green Deployment runs two production-like environments. One receives live traffic while the other receives the new version. Traffic switches when the new environment is verified.

**Use when:** Fast rollback and environment-level validation are important.

**Watch for:** Database migrations and stateful dependencies that cannot switch as easily as stateless services.

[Back To Top](#top)

---

### Canary Release

Canary Release sends a small percentage of traffic to a new version before gradually increasing exposure.

**Use when:** Reducing release risk and detecting production-only issues early.

**Watch for:** Weak metrics. A canary needs clear success criteria, representative traffic, and automated rollback where possible.

[Back To Top](#top)

---

### Rolling Deployment

Rolling Deployment replaces instances gradually while keeping the service available.

**Use when:** Performing routine updates where adjacent versions can coexist during rollout.

**Watch for:** Incompatible API or schema changes. Rolling deployments require backward and forward compatibility.

[Back To Top](#top)

---

### Branch by Abstraction

Branch by Abstraction introduces an abstraction layer so old and new implementations can coexist while code is migrated incrementally.

**Use when:** Replacing a large component or completing a long refactor without a long-lived source branch.

**Watch for:** Temporary abstractions becoming permanent. Remove the layer when the migration no longer needs it.

[Back To Top](#top)

---

## Security Patterns

Security patterns protect identity, authorization, data, and trust boundaries across distributed services.

[Back To Top](#top)

---

### Token Relay

Token Relay passes a caller's identity token or a derived service token through downstream calls so authorization can be enforced consistently.

**Use when:** Downstream services need user or client context to make access decisions.

**Watch for:** Token leakage and over-trusting upstream services. Validate tokens and pass only the claims each service needs.

[Back To Top](#top)

---

### Gateway Offloading

Gateway Offloading moves edge responsibilities such as TLS termination, authentication checks, request size limits, and rate limiting to the gateway.

**Use when:** Common security controls need consistent enforcement at the edge.

**Watch for:** False confidence. Edge checks do not remove the need for service-level authorization on sensitive operations.

[Back To Top](#top)

---

## Testing Patterns

Testing patterns keep independently deployed services compatible as they evolve.

[Back To Top](#top)

---

### Consumer-Driven Contract

Consumer-Driven Contract testing captures expectations from service consumers and verifies that providers continue to satisfy them.

**Use when:** Independent teams deploy services that communicate through APIs or events.

**Watch for:** Contracts that duplicate provider implementation details. Good contracts focus on externally observable behavior that consumers actually depend on.

[Back To Top](#top)

---

## Pattern Summary

| Area | Pattern | Primary Problem | Typical Use |
| --- | --- | --- | --- |
| Boundary | Decompose by Business Capability | Services need business-aligned ownership | Split catalog, ordering, payment, inventory, and shipping capabilities |
| Boundary | Bounded Context | Shared models create semantic coupling | Give each domain context its own model and language |
| Boundary | Anti-Corruption Layer | External models leak into the domain | Translate between incompatible models |
| Migration | Strangler Fig | A full rewrite is too risky | Gradually replace monolith capabilities |
| API Edge | API Gateway | Clients need one stable entry point | Route, secure, limit, and shape client traffic |
| API Edge | Backend for Frontend | Different clients need different APIs | Build web, mobile, or partner-specific edge APIs |
| Communication | Service Discovery | Service addresses change dynamically | Resolve service locations in containerized platforms |
| Communication | Asynchronous Messaging | Services should not wait on each other | Queue background work and integration events |
| Data | Database per Service | Shared tables create hidden coupling | Give each service ownership over its data |
| Data | [Saga](saga.md) | A workflow spans multiple services | Coordinate local transactions with compensation |
| Data | Transactional Outbox | State updates and event publication must agree | Persist events with local state changes |
| Data | [CQRS](cqrs.md) | Reads and writes need different models | Build denormalized read models for queries |
| Data | Event Sourcing | Complete state history is required | Store domain changes as events |
| Reliability | Timeout | Callers can wait indefinitely | Bound every remote call |
| Reliability | Retry | Some failures are temporary | Retry idempotent operations with backoff |
| Reliability | Circuit Breaker | Failed dependencies consume caller resources | Fail fast when a dependency is unhealthy |
| Reliability | [Bulkhead](bulkhead.md) | One workload can exhaust shared resources | Isolate pools, queues, or worker groups |
| Reliability | Dead Letter Queue | Poison messages block processing | Isolate failed messages for inspection and replay |
| Observability | Correlation ID | Requests are hard to follow across services | Propagate workflow identifiers through logs, events, and traces |
| Observability | Distributed Tracing | Latency and dependency paths are unclear | Trace requests across service boundaries |
| Deployment | Sidecar | Runtime behavior repeats in every service | Attach proxies or helpers beside services |
| Deployment | Canary Release | Releases need gradual exposure | Shift traffic to a new version incrementally |
| Security | Token Relay | Downstream services need caller context | Propagate identity safely across calls |
| Testing | Consumer-Driven Contract | Provider changes can break consumers | Verify changes against consumer expectations |

[Back To Top](#top)

---

## Learning Exercises

- Add an API Gateway example that routes to `rest-spring` and one future service.
- Add a Backend for Frontend example for a mobile-optimized TODO read API.
- Extend the `grpc` project with timeout, retry, and circuit breaker examples.
- Add an outbox table to `rest-spring` for TODO events.
- Implement an idempotent message consumer using an inbox table.
- Document a Saga for a multi-step workflow such as order placement.
- Add correlation IDs to REST, gRPC, and asynchronous message flows.
- Add health, metrics, and tracing examples for one service.
- Compare database-per-service with modular-monolith table ownership.
- Add a consumer-driven contract test between two repository services.

[Back To Top](#top)
