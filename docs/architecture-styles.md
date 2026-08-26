<a id="top"></a>

# Architecture Styles

There are **two categories** of architecture styles:

- **Monolithic**
- **Distributed**

Each style has strengths, weaknesses and use cases.

## Table of Contents

- [Monolithic](#monolithic)
  - [Layered Architecture](#layered-architecture)
  - [Modular Architecture](#modular-architecture)
  - [Pipeline Architecture](#pipeline-architecture)
  - [Microkernel Architecture](#microkernel-architecture)
- [Distributed](#distributed)
  - [Service Oriented Architecture (SOA)](#service-oriented-architecture-soa)
  - [Event-Driven Architecture (EDA)](#event-driven-architecture-eda)
  - [Space-Based Architecture](#space-based-architecture)
  - [Orchestration-Driven Architecture](#orchestration-driven-architecture)
  - [Microservices Architecture](#microservices-architecture)

## Monolithic

### Layered Architecture

**What it is**

- Organizes an application into horizontal layers such as presentation, business logic, persistence, and database.

**Why it is used**

- Separates responsibilities so the system is easier to understand, test, and maintain.

**Reasons to choose**

- Provides a simple structure with clear boundaries.
- Works well for CRUD-heavy applications.

**Weakness**

- Can become rigid when changes need to cross multiple layers.
- May add unnecessary overhead for very small applications.

**When to choose**

- Choose it for enterprise apps, internal systems, and applications with stable business rules.

[Back to top](#top)

### Modular Architecture

**What it is**

- Organizes a single deployable application into clear business-focused modules.
- Each module owns a cohesive part of the domain and exposes a controlled interface.

**Why it is used**

- Keeps monolithic deployment simple while improving code ownership and maintainability.

**Reasons to choose**

- Provides many modularity benefits without distributed system complexity.
- Helps teams define boundaries before considering microservices.

**Weakness**

- Boundaries can erode if module access rules are not enforced.
- Large builds and deployments may still affect the whole application.

**When to choose**

- Choose it when the domain is still evolving and simpler operations are preferred.
- See [Modular Monolith Architecture](modular-monolith.md) for more detail.

[Back to top](#top)

### Pipeline Architecture

**What it is**

- Processes data through a sequence of independent steps.
- Each step transforms, filters, or enriches the input before passing it forward.

**Why it is used**

- Useful when work can be broken into repeatable processing stages.

**Reasons to choose**

- Easy to extend by adding or replacing processing steps.
- Supports reusable steps for streaming or batch workflows.

**Weakness**

- Can be hard to debug when failures happen across many stages.
- Less suitable when processing steps need complex back-and-forth communication.

**When to choose**

- Choose it for data processing, compilers, ETL systems, image processing, and event streams.

[Back to top](#top)

### Microkernel Architecture

**What it is**

- Builds a small core system with optional plugins that add features.

**Why it is used**

- Allows the system to be extended without changing the core logic.

**Reasons to choose**

- Provides high flexibility and plugin support.
- Makes customization easier for different users or products.

**Weakness**

- Plugin compatibility and versioning can become difficult to manage.
- Core design must be stable because many plugins depend on it.

**When to choose**

- Choose it for IDEs, workflow tools, browsers, product platforms, or systems needing third-party extensions.

[Back to top](#top)

## Distributed

### Service Oriented Architecture (SOA)

**What it is**

- Splits functionality into reusable services that communicate over a network.

**Why it is used**

- Enables reuse, integration, and independent ownership of business capabilities.

**Reasons to choose**

- Good for large enterprises with shared business services.
- Supports cross-system communication and integration.

**Weakness**

- Network communication can add latency and failure points.
- Governance and service coordination can become complex.

**When to choose**

- Choose it when multiple applications need to share common business services.

[Back to top](#top)

### Event-Driven Architecture (EDA)

**What it is**

- Designs systems around events that represent things that have already happened.
- Producers publish events, and consumers react to them asynchronously.

**Why it is used**

- Reduces direct coupling between components and supports responsive, scalable workflows.

**Reasons to choose**

- Helps multiple services or modules react to business changes independently.
- Works well for asynchronous processing, notifications, integrations, and audit trails.

**Weakness**

- Debugging and tracing workflows can be harder because work happens across many handlers.
- Requires careful handling of retries, duplicate messages, ordering, and eventual consistency.

**When to choose**

- Choose it when business events need to trigger work across modules, services, or external systems.
- See [Event-Driven Architecture](event-driven-architecture.md) for more detail.

[Back to top](#top)

### Space-Based Architecture

**What it is**

- Distributes application state and processing across multiple nodes.
- Often uses in-memory data grids to share data quickly.

**Why it is used**

- Reduces database bottlenecks and supports high scalability.

**Reasons to choose**

- Supports high throughput and fault tolerance.
- Scales elastically during sudden traffic spikes.

**Weakness**

- Can be complex to design, operate, and troubleshoot.
- Data consistency can be harder to maintain across distributed nodes.

**When to choose**

- Choose it for systems with unpredictable traffic spikes, such as trading, ticket booking, or large-scale e-commerce.

[Back to top](#top)

### Orchestration-Driven Architecture

**What it is**

- Uses a central orchestrator to coordinate services, workflows, or business processes.

**Why it is used**

- Provides control and visibility over complex multi-step operations.

**Reasons to choose**

- Centralizes workflow management and monitoring.
- Gives clear process control for business operations.

**Weakness**

- The orchestrator can become a bottleneck or single point of failure.
- Workflows may become tightly coupled to the orchestration logic.

**When to choose**

- Choose it for order processing, payment workflows, approvals, and systems with long-running business processes.

[Back to top](#top)

### Microservices Architecture

**What it is**

- Splits a system into independently deployable services.
- Each service owns a business capability, exposes explicit contracts, and usually owns its own data.

**Why it is used**

- Enables independent deployment, scaling, ownership, and failure isolation for different parts of a system.

**Reasons to choose**

- Helps teams work independently around clear service boundaries.
- Supports different scaling, release, and technology needs per service.

**Weakness**

- Adds distributed system complexity such as networking, retries, observability, and data consistency.
- Debugging and testing end-to-end workflows can become harder across many services.

**When to choose**

- Choose it when service boundaries are stable and teams need independent deployment or scaling.
- See [Microservice Architecture](microservices.md) for more detail.

[Back to top](#top)
