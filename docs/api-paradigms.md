<a id="top"></a>

# API Paradigms

This document compares request-response API styles: REST, RPC, and GraphQL.

## Table of Contents

- [Overview](#overview)
- [REST](#rest)
- [RPC](#rpc)
- [GraphQL](#graphql)
- [Comparison](#comparison)
- [Architecture Learning Notes](#architecture-learning-notes)

## Overview

An API paradigm defines the shape of the contract between a client and a server. Choosing the right paradigm affects coupling, payload size, client complexity, caching, discoverability, and future change.

Request-response APIs commonly expose operations over HTTP or another network protocol. Clients send a request and receive a response.

[Back to top](#top)

## REST

REST exposes data as resources. Clients use URLs to identify resources and HTTP methods to operate on them.

Common method mapping:

| Operation | HTTP Method | Collection URL | Item URL |
| --- | --- | --- | --- |
| Create | POST | `/users` | Not usually used |
| Read | GET | `/users` | `/users/{id}` |
| Update | PUT or PATCH | `/users` | `/users/{id}` |
| Delete | DELETE | `/users` | `/users/{id}` |

REST is a good fit when the API is resource-oriented, CRUD-like, cacheable, and intended for many kinds of clients.

[Back to top](#top)

## RPC

RPC, or Remote Procedure Call, exposes actions or commands. The client asks a remote service to execute a named operation.

RPC is useful when:

- The operation is action-oriented rather than resource-oriented.
- Low overhead matters.
- Services communicate internally.
- The contract can be strongly typed.
- Clients and servers can evolve together.

The `grpc` project in this repository is an RPC example using protobuf and gRPC.

[Back to top](#top)

## GraphQL

GraphQL lets clients request exactly the data shape they need from a single endpoint. It is query-oriented rather than resource-oriented or action-oriented.

GraphQL is useful when:

- Clients need flexible data selection.
- Mobile clients need fewer round trips.
- Data is graph-like.
- Schema introspection is valuable.
- Avoiding many REST endpoints is worth the server-side complexity.

[Back to top](#top)

## Comparison

| Topic | REST | RPC | GraphQL |
| --- | --- | --- | --- |
| Main unit | Resource | Action | Query |
| Common protocol | HTTP | HTTP/2, HTTP, custom protocols | HTTP |
| Strength | Standard web semantics | Efficient service calls | Flexible client queries |
| Risk | Multiple round trips | Tight coupling | Query complexity |
| Best fit | CRUD APIs | Internal service operations | Flexible data APIs |
| Repo example | `rest-spring` | `grpc` | Not implemented yet |

[Back to top](#top)

## Architecture Learning Notes

For modular monoliths, APIs often exist between modules inside one deployable application. These boundaries may be Java interfaces, events, package APIs, or REST controllers.

For microservices, APIs exist across process and network boundaries. This makes contracts, versioning, latency, retries, and observability much more important.

[Back to top](#top)
