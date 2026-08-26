<a id="top"></a>

# Modular Monolith Architecture

This document explains modular monolith architecture as a practical step between a simple monolith and a distributed system.

## Table of Contents

- [What it is](#what-it-is)
- [Why it is used](#why-it-is-used)
- [Reasons to choose](#reasons-to-choose)
- [Weakness](#weakness)
- [When to choose](#when-to-choose)
- [Module Boundaries](#module-boundaries)
- [Data Ownership](#data-ownership)
- [Communication Between Modules](#communication-between-modules)
- [Testing Strategy](#testing-strategy)
- [Migration Path to Microservices](#migration-path-to-microservices)

## What it is

- A modular monolith is one deployable application organized into clear internal modules.
- The system runs as a single process, but the code is structured around business capabilities rather than only technical layers.

The important idea is modularity first, distribution later.

[Back to top](#top)

## Why it is used

- It keeps deployment and operations simple while still encouraging strong boundaries inside the codebase.
- It gives many design benefits of microservices without immediately accepting distributed system costs.

[Back to top](#top)

## Reasons to choose

- Choose it when the team wants clear ownership, simpler testing, and easier local development.
- Choose it when shared transactions are useful and network complexity is not yet justified.

[Back to top](#top)

## Weakness

- Module boundaries can erode if the codebase allows unrestricted cross-module access.
- The application is still deployed as one unit, so large builds, releases, or runtime failures can affect the whole system.

[Back to top](#top)

## When to choose

- Choose it when the domain is still being learned and service boundaries are not yet stable.
- Choose it when the system needs modular design, but the team is not ready for microservice operations.

[Back to top](#top)

## Module Boundaries

Good modules usually align with business capabilities. A module should own a cohesive part of the domain and expose a small API to the rest of the application.

Boundary rules to practice:

- Do not let every package call every other package.
- Expose module APIs intentionally.
- Keep internal implementation classes private to the module.
- Avoid shared mutable domain models across modules.
- Treat cross-module calls as architectural decisions.

[Back to top](#top)

## Data Ownership

In a modular monolith, modules may share one physical database, but each module should still own its tables or schema area conceptually.

Useful rules:

- One module should not freely update another module's tables.
- Cross-module reads should happen through module APIs or dedicated read models.
- Shared transactions are allowed, but should be intentional.
- Database ownership should be documented.

[Back to top](#top)

## Communication Between Modules

Modules can communicate through:

- Direct method calls through public module interfaces.
- Application services.
- In-process domain events.
- Query/read models for reporting.

Prefer simple communication while the application is in one process. Add asynchronous events when they reduce coupling or represent real domain events.

[Back to top](#top)

## Testing Strategy

Useful test levels:

- Unit tests for domain rules inside a module.
- Module tests for the module API and persistence.
- Integration tests for cross-module workflows.
- Contract-style tests if a module API is expected to remain stable.

[Back to top](#top)

## Migration Path to Microservices

A modular monolith can prepare for microservices if module boundaries are real. A module is a better candidate for extraction when:

- It has clear ownership.
- It has a stable API.
- It owns its data.
- It has independent scaling or deployment needs.
- Its failure modes can be isolated.

Do not extract a module only because it is technically possible. Extract when the operational cost is justified.

[Back to top](#top)
