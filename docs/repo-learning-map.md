<a id="top"></a>

# Repository Learning Map

This document connects the repository projects to the architecture learning goals.

## Table of Contents

- [Repository Purpose](#repository-purpose)
- [Current Projects](#current-projects)
- [How to Use rest-spring](#how-to-use-rest-spring)
- [How to Use grpc](#how-to-use-grpc)
- [Suggested Documentation Growth](#suggested-documentation-growth)
- [Review Notes From README](#review-notes-from-readme)

## Repository Purpose

This repository can become a practical learning lab for API design, modular monolith architecture, and microservice architecture.

The existing root README is a broad conceptual overview of API paradigms. The project folders provide runnable examples that can become architecture exercises.

[Back to top](#top)

## Current Projects

| Project | Current Role | Learning Value |
| --- | --- | --- |
| `rest-spring` | Spring Boot REST TODO API | REST, layered architecture, validation, JDBC, Flyway, configuration |
| `grpc` | Java gRPC hello client/server | RPC, protobuf contracts, generated code, client/server tests |

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

Add future docs when the repo grows:

- `docs/rest-api-design.md`
- `docs/grpc-contracts.md`
- `docs/domain-driven-design.md`
- `docs/module-boundaries.md`
- `docs/service-discovery.md`
- `docs/observability.md`
- `docs/testing-strategy.md`
- `docs/deployment.md`

Each new document should follow the same table of contents and back-to-top pattern.

[Back to top](#top)

## Review Notes From README

The root README has strong breadth and is useful as a starting primer. A few improvements would make it easier to maintain:

- Convert the plain table of contents into links.
- Move detailed sections into the `docs/` folder to reduce README length.
- Fix typos such as `Jarkarta`, `devependency`, `persistance`, and `standarized`.
- Replace raw HTML where Markdown is enough.
- Remove or explain the trailing notes `NSQ` and `Kicklock`.
- Add a short section that explains how `rest-spring` and `grpc` map to the architecture learning goals.

[Back to top](#top)
