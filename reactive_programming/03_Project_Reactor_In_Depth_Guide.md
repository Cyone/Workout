# 3. Project Reactor In-Depth Guide

Project Reactor is the foundational reactive library powering Spring WebFlux (Spring Framework 5+). Unlike RxJava 2/3, which supports legacy systems and Android, Reactor was designed aggressively for Java 8+ backend services. It natively and fully supports the Reactive Streams specification (meaning backpressure is baked into every operator).

## 1. The Core Types: Flux and Mono

Where RxJava has five core types (`Observable`, `Flowable`, `Single`, `Maybe`, `Completable`), Reactor simplifies the mental model to just two.

### 1. `Mono<T>` (0 or 1 Item)
*   **What it is:** The equivalent of Java's `CompletableFuture<T>` or RxJava's `Single`, `Maybe`, and `Completable` rolled into one. It emits at most one item in the future, and then terminates (successfully or with an error).
*   **Use Cases:**
    *   Fetching a single User by ID (`Mono<User>`).
    *   Saving a record to MongoDB (`Mono<Void>` -> represents a `Completable` action that emits nothing but signals completion).
    *   Handling HTTP requests in Spring WebFlux controllers (e.g., returning a `Mono<ResponseEntity<User>>`).
*   **Key Operators:** `map`, `flatMap`, `defaultIfEmpty`, `switchIfEmpty`, `zipWith`.

### 2. `Flux<T>` (0 to N Items)
*   **What it is:** The equivalent of Java 8 `Stream<T>` but designed for asynchronous, unbounded, push-based data. It is directly equivalent to RxJava's `Flowable` (meaning it supports backpressure out of the box).
*   **Use Cases:**
    *   Reading rows from a Postgres database using R2DBC (`Flux<User>`).
    *   Streaming Server-Sent Events (SSE) to a frontend application line by line.
    *   Consuming a Kafka topic where messages arrive indefinitely.
*   **Key Operators:** `buffer`, `window`, `reduce`, `collectList` (converts a `Flux<T>` into a `Mono<List<T>>`), `concatMap`, `merge`.

## 2. Execution & Schedulers

Reactor behaves similarly to RxJava: nothing happens until you `.subscribe()`. And by default, execution happens synchronously on the thread that called `.subscribe()` (which in Spring WebFlux is usually a Netty event loop thread).

### The Core Schedulers
Reactor's Schedulers are specifically tuned for backend web server workloads.

1.  **`Schedulers.immediate()`:** The default. Runs on whatever thread calls it. No thread switching occurs.
2.  **`Schedulers.parallel()`:** A fixed pool of workers tailored for CPU-bound tasks. Size defaults to the number of CPU cores. **Never use this for blocking I/O (like legacy JDBC connections); you will hang your entire application.**
3.  **`Schedulers.boundedElastic()`:** The replacement for RxJava's `Schedulers.io()`. It is designed specifically for **legacy blocking I/O tasks** (e.g., waiting for an HTTP response from an old REST template, or querying an Oracle database with JDBC).
    *   *Why "bounded"?* Older "elastic" pools could create 10,000 threads during a traffic spike, crashing the server. `boundedElastic()` caps the number of threads (e.g., 10x CPU cores) and queues the rest. This provides vital backpressure against thread exhaustion.

### Thread Switching (`publishOn` vs `subscribeOn`)
These function identically to RxJava, but with different names.

*   **`subscribeOn(Scheduler)`:** (Same as RxJava). Determines which thread the *source* data structure originates on when subscribed. It affects the entire upstream chain. You use this to wrap legacy JDBC calls.
*   **`publishOn(Scheduler)`:** (Equivalent to RxJava's `observeOn`). Determines the thread for all operators *downstream* of this call. Used when you switch from a fast I/O thread to a heavy CPU computation thread mid-pipeline.

**Example: Wrapping a Legacy Blocking Call safely:**
```java
// Spring WebFlux Controller endpoint
@GetMapping("/users/{id}")
public Mono<User> getUser(@PathVariable Long id) {
    // 1. You MUST wrap the blocking call inside Mono.fromCallable.
    // Otherwise, the JVM will execute the blocking call instantly on the Netty thread BEFORE the Mono is even constructed!
    return Mono.fromCallable(() -> legacyJdbcUserRepository.findById(id))
               // 2. Offload the execution of the callable to the specialized blocking thread pool
               .subscribeOn(Schedulers.boundedElastic())
               // 3. Map operates on the boundedElastic thread when data returns
               .map(entity -> UserDto.fromEntity(entity));
}
```

## 3. Dealing with Errors

Reactive streams terminate immediately upon error. An exception thrown inside a `map` block does not crash the JVM thread; instead, an `onError` signal is propagated all the way down to the subscriber, bypassing all other operators.

*   **`onErrorReturn(Object)`:** The simplest recovery. If an exception occurs, catch it and emit a default, safe fallback value instead of failing.
*   **`onErrorResume(Function)`:** If an exception occurs, subscribe to a completely different fallback Publisher (Mono/Flux). Like a `catch` block that triggers a secondary API call.
*   **`retry(long)` / `retryWhen()`:** Extremely powerful mechanisms to transparently re-subscribe to the source if it fails due to transient network glitches.
