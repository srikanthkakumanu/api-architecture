<a id="top"></a>

# Repository Learning Map

This document connects the repository projects to the architecture learning goals.

## Table of Contents

- [Repository Purpose](#repository-purpose)
- [Current Projects](#current-projects)
- [How to Use modular-monolith](#how-to-use-modular-monolith)
- [How to Use rest-spring](#how-to-use-rest-spring)
- [How to Use grpc](#how-to-use-grpc)
- [Suggested Documentation Growth](#suggested-documentation-growth)
- [Review Notes From README](#review-notes-from-readme)

## Repository Purpose

This repository is a practical learning lab for API design, modular monolith architecture, and microservice architecture.

The topic guides establish the concepts, and the project folders provide runnable architecture exercises.

[Back to top](#top)

## Current Projects

| Project | Current Role | Learning Value |
| --- | --- | --- |
| `rest-spring` | Spring Boot REST TODO API | REST, layered architecture, validation, JDBC, Flyway, configuration |
| `grpc` | Java gRPC hello client/server | RPC, protobuf contracts, generated code, client/server tests |
| `modular-monolith` | Spring Modulith clinic application | Business modules, controlled interfaces, architecture tests, full-stack integration |

[Back to top](#top)

## How to Use modular-monolith

Use `modular-monolith` to study:

- Business-capability modules in a single deployment.
- Public module APIs and private implementation packages.
- Spring Modulith boundary verification.
- Cross-module collaboration without direct access to internal packages.
- A Next.js client consuming a modular Spring Boot backend.

[Back to top](#top)

## How to Use rest-spring

Use `rest-spring` to study:

- REST endpoint design.
- Controller-service-repository layering.
- Input validation.
- Database migrations with Flyway.
- JDBC repository implementation.
- Spring Boot application configuration.
- How a monolith can be reorganized into modules.

Possible modular monolith direction:

- `todo-api`: HTTP controllers and DTOs.
- `todo-application`: use cases and orchestration.
- `todo-domain`: TODO domain model and rules.
- `todo-persistence`: JDBC repository and migrations.

[Back to top](#top)

## How to Use grpc

Use `grpc` to study:

- Protobuf service contracts.
- Generated Java classes.
- gRPC server implementation.
- Blocking client stubs.
- In-process gRPC tests.
- RPC as an internal microservice communication style.

Possible microservice direction:

- Add a second RPC method.
- Add error handling with gRPC statuses.
- Add deadline and timeout examples.
- Add metadata propagation.
- Add contract evolution examples.

[Back to top](#top)

## Suggested Documentation Growth

Add future docs under the topic that owns them:

- `docs/api/rest-design.md`
- `docs/api/grpc-contracts.md`
- `docs/architecture/module-boundaries.md`
- `docs/micro-services/service-discovery.md`
- `docs/micro-services/observability.md`
- `docs/micro-services/testing-strategy.md`
- `docs/micro-services/deployment.md`

Each new document should follow the same table of contents and back-to-top pattern.

[Back to top](#top)

## Review Notes From README

The root README is intentionally a navigation page. Keep it easy to maintain by following these rules:

- Keep detailed material in the appropriate topic folder under `docs/`.
- Link new documents from both their topic index and the main docs index.
- Keep project-specific setup and execution guidance beside the project code.
- Prefer relative links so documentation works in local checkouts and repository browsers.

[Back to top](#top)
