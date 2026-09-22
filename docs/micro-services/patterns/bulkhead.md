# Bulkhead Pattern

The **Bulkhead pattern** is a resilience strategy that isolates resources such as threads, connection pools, memory, or work queues. If one dependency or workload becomes slow or overloaded, its resource limits prevent it from exhausting the resources needed by the rest of the application.

In Spring Boot, bulkheads are commonly implemented with **Resilience4j**, which supports semaphore and thread-pool isolation.

---

## What the Bulkhead Pattern Does

The pattern takes its name from ships, where sealed compartments prevent flooding in one area from sinking the entire vessel. In software, the same idea limits failure to a defined resource partition.

- **Isolates workloads:** Each dependency or workload receives a controlled share of resources.
- **Limits concurrency:** The application rejects or queues excess work instead of allowing unbounded growth.
- **Protects critical paths:** A slow optional dependency cannot consume resources needed by core operations.
- **Reduces cascading failure:** Saturation remains local instead of spreading across unrelated requests.

A bulkhead does not make a dependency healthy. It protects the caller while the dependency is slow, unavailable, or receiving more traffic than it can handle.

---

## Core Concepts

- **Protected operation:** The method or dependency call whose concurrency is limited.
- **Capacity:** The maximum number of calls or worker threads allowed in the compartment.
- **Wait duration:** How long a caller may wait for available capacity before being rejected.
- **Queue capacity:** The number of tasks a thread-pool bulkhead may buffer before rejecting new work.
- **Rejection:** The explicit failure that occurs when the bulkhead has no available capacity.
- **Fallback:** Optional behavior that provides a degraded response when a call is rejected or fails.

Bulkhead limits should reflect downstream capacity and the importance of the workload. Arbitrary limits can simply move the bottleneck or reject healthy traffic.

---

## Implementing Bulkhead in Spring Boot

### 1. Add Dependencies

Add Resilience4j, Spring AOP support for annotations, and Actuator for metrics:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.resilience4j</groupId>
        <artifactId>resilience4j-spring-boot3</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

Use the Resilience4j starter that matches the Spring Boot generation used by the application.

### 2. Configure a Semaphore Bulkhead

A semaphore bulkhead limits concurrent calls while execution remains on the caller's thread:

```yaml
resilience4j:
  bulkhead:
    instances:
      orderServiceBulkhead:
        maxConcurrentCalls: 5
        maxWaitDuration: 10ms
```

- **`maxConcurrentCalls`:** Maximum number of calls allowed to execute concurrently.
- **`maxWaitDuration`:** Maximum time a caller waits for permission before the call is rejected.

Use a zero or short wait duration for latency-sensitive APIs. Long waits can consume request threads and hide overload.

### 3. Apply the Bulkhead

Apply the configured bulkhead to a service method and provide a fallback with a compatible method signature:

```java
@Service
public class OrderService {

    @Bulkhead(
        name = "orderServiceBulkhead",
        fallbackMethod = "fallbackOrder"
    )
    public String getOrder(String orderId) {
        return loadOrderFromDependency(orderId);
    }

    private String fallbackOrder(String orderId, Throwable throwable) {
        return "Order service is temporarily unavailable.";
    }

    private String loadOrderFromDependency(String orderId) {
        return "Order details for ID: " + orderId;
    }
}
```

- **`@Bulkhead`:** Associates the method with the named bulkhead configuration.
- **Fallback method:** Defines an optional degraded response when execution is rejected or fails.

Fallbacks should not report successful business completion when the protected operation did not complete. Return an explicit degraded result or propagate a suitable error when stale or partial data would be unsafe.

### 4. Configure a Thread-Pool Bulkhead

A thread-pool bulkhead runs work on a dedicated executor and may buffer a limited number of tasks:

```yaml
resilience4j:
  thread-pool-bulkhead:
    instances:
      reportServiceBulkhead:
        coreThreadPoolSize: 2
        maxThreadPoolSize: 4
        queueCapacity: 20
        keepAliveDuration: 20ms
```

- **`coreThreadPoolSize`:** Baseline number of worker threads.
- **`maxThreadPoolSize`:** Maximum number of worker threads.
- **`queueCapacity`:** Maximum number of tasks waiting for execution.
- **`keepAliveDuration`:** Idle time before excess worker threads are removed.

Thread-pool bulkheads are useful when a workload needs dedicated execution resources. Queue capacity must remain bounded so overload produces controlled rejection rather than steadily increasing latency and memory use.

### 5. Monitor Bulkhead Behavior

Expose Actuator health and metrics endpoints:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics
```

Monitor at least:

- Available and maximum concurrent calls.
- Permitted and rejected calls.
- Thread-pool saturation and queue depth.
- Fallback rate and fallback failures.
- Protected-operation latency and error rate.

An increasing rejection rate can indicate insufficient bulkhead capacity, excessive caller traffic, or a slow downstream dependency. Increasing the limit is not always the right response.

---

## Types of Bulkheads

| Type | How It Works | Best Fit | Main Risk |
| --- | --- | --- | --- |
| **Semaphore bulkhead** | Limits concurrent calls with permits | Short, synchronous dependency calls | Waiting callers may still occupy request threads |
| **Thread-pool bulkhead** | Runs work in a dedicated executor with a bounded queue | Blocking or resource-intensive workloads | Queued work can increase latency and memory use |
| **Connection-pool bulkhead** | Gives a dependency its own bounded connection pool | Databases, HTTP clients, and brokers | Poor sizing can underuse or overwhelm the dependency |
| **Process or instance bulkhead** | Separates workloads into different processes or deployments | Critical workloads with strong isolation needs | Higher deployment and operational cost |

Choose the smallest isolation boundary that protects the required workload. Stronger isolation usually increases resource and operational overhead.

---

## Relationship to Other Resilience Patterns

- Combine a bulkhead with a **Timeout** so protected calls cannot hold capacity indefinitely.
- Add a **Circuit Breaker** to stop calls when a dependency is consistently unhealthy.
- Use bounded **Retry** policies because retries also consume bulkhead capacity.
- Apply **Rate Limiting** at the edge when callers can generate more work than the service should accept.
- Use **Backpressure** for streaming or queued workloads so producers respond to consumer capacity.

The order and scope of resilience decorators matter. Measure how retries, timeouts, circuit breakers, and bulkheads interact under load.

---

## Practical Guidance for Java Developers

- Start with a **semaphore bulkhead** for short synchronous calls to payment, order, inventory, or other remote services.
- Use a **thread-pool bulkhead** for blocking or resource-intensive work such as reporting and legacy integrations.
- Isolate critical and optional workloads into different bulkheads.
- Size concurrency from downstream capacity, latency targets, and measured traffic rather than guesswork.
- Keep queues bounded and define explicit rejection behavior.
- Alert on sustained saturation, rejected calls, and fallback failures.
- Test bulkhead behavior under slow dependencies, timeouts, traffic spikes, and partial outages.

A well-sized bulkhead preserves capacity for healthy operations. An oversized bulkhead provides little protection, while an undersized one rejects valid traffic unnecessarily.
