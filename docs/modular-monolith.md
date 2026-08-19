<a id="top"></a>

# Modular Monolith Architecture

This document introduces modular monolith architecture as a learning path for this repository.

## Table of Contents

- [Definition](#definition)
- [Why Use a Modular Monolith](#why-use-a-modular-monolith)
- [Module Boundaries](#module-boundaries)
- [Data Ownership](#data-ownership)
- [Communication Between Modules](#communication-between-modules)
- [Testing Strategy](#testing-strategy)
- [Migration Path to Microservices](#migration-path-to-microservices)
- [Learning Exercises](#learning-exercises)

## Definition

A modular monolith is one deployable application organized into clear internal modules. The system runs as a single process, but the code is structured around business capabilities rather than purely technical layers.

The important idea is modularity first, distribution later.

[Back to top](#top)

## Why Use a Modular Monolith

A modular monolith is useful when:

- The domain is still being learned.
- The team wants simpler deployment and operations.
- Strong transaction boundaries are useful.
- Network complexity is not yet justified.
- The system still needs clear ownership boundaries.

It gives many design benefits of microservices without immediately accepting distributed system costs.

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

## Learning Exercises

- Refactor the `rest-spring` TODO app into modules such as `todo`, `api`, and `persistence`.
- Create a package boundary rule that prevents controllers from directly using repositories.
- Add an in-process event such as `TodoCompleted`.
- Document which module owns the `todo` table.
- Add module-level tests around the TODO service API.

[Back to top](#top)
