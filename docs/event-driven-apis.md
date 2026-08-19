<a id="top"></a>

# Event Driven APIs

This document summarizes event-driven API styles from the root README.

## Table of Contents

- [Overview](#overview)
- [Polling](#polling)
- [WebHooks](#webhooks)
- [WebSockets](#websockets)
- [HTTP Streaming](#http-streaming)
- [Comparison](#comparison)
- [Architecture Learning Notes](#architecture-learning-notes)

## Overview

Event-driven APIs help systems react to changes. Instead of only asking for the current state, consumers can receive or observe events such as resource creation, updates, deletion, status changes, or workflow progress.

Event-driven design matters in both modular monoliths and microservices because it can reduce direct coupling between producers and consumers.

[Back to top](#top)

## Polling

Polling means a client repeatedly calls an API to check whether something changed.

Benefits:

- Simple to understand.
- Easy to implement with normal HTTP endpoints.
- Works when clients cannot receive inbound requests.

Drawbacks:

- Wastes resources when there are no changes.
- Can miss timely updates if the interval is too slow.
- Can overload systems if the interval is too fast.

[Back to top](#top)

## WebHooks

A WebHook is an HTTP callback URL. A provider sends an HTTP request to a consumer when an event occurs.

Benefits:

- Good for server-to-server notifications.
- Uses familiar HTTP infrastructure.
- Avoids constant polling.

Drawbacks:

- Requires retry handling.
- Requires signature validation or another trust mechanism.
- Can be difficult behind firewalls.
- Can become noisy at high event volume.

[Back to top](#top)

## WebSockets

WebSocket is a protocol for long-lived, full-duplex communication over a single connection.

Benefits:

- Supports real-time two-way communication.
- Useful for live dashboards, collaboration, chat, and streaming updates.
- Works over common ports such as 80 and 443.

Drawbacks:

- Requires connection management.
- Scaling many long-lived connections needs careful infrastructure.
- Clients need reconnect behavior.

[Back to top](#top)

## HTTP Streaming

HTTP streaming keeps a response open so the server can continue sending data. Server-Sent Events are a common browser-friendly form of one-way HTTP streaming.

Benefits:

- Uses HTTP.
- Good for one-way updates.
- Native browser support exists for Server-Sent Events.

Drawbacks:

- Bidirectional communication is limited.
- Proxies and clients may buffer data.
- Changing subscriptions often requires reconnecting.

[Back to top](#top)

## Comparison

| Style | Direction | Best Fit | Main Risk |
| --- | --- | --- | --- |
| Polling | Client to server | Simple status checks | Waste and stale data |
| WebHooks | Server to server | Event callbacks | Retries and trust |
| WebSockets | Two-way | Live interaction | Connection scaling |
| HTTP streaming | Server to client | One-way live feeds | Buffering and reconnects |

[Back to top](#top)

## Architecture Learning Notes

In a modular monolith, events can be in-process domain events. They help one module react to another module without direct service calls everywhere.

In microservices, events usually cross process boundaries through a broker or stream. This adds concerns such as delivery guarantees, idempotency, ordering, schema evolution, and replay.

[Back to top](#top)
