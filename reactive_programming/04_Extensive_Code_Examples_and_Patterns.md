# 4. Extensive Reactive Code Examples and Architecture Patterns

This document provides massive, interview-ready code snippets demonstrating how to solve common, complex, real-world problems using Project Reactor (`Mono` and `Flux`). These concepts apply nearly identically to RxJava.

---

## Pattern 1: Aggregating Multiple Independent Microservices (Parallel Execution)

**Scenario:** We need to build a User Dashboard. This requires calling the Notification Service, the Billing Service, and the Profile Service simultaneously.

**The Anti-Pattern (Sequential Execution taking 3 seconds):**
```java
public Mono<Dashboard> buildDashboard(String userId) {
    // This executes sequentially because flatMap chains asynchronous calls sequentially.
    return profileClient.getProfile(userId)
        .flatMap(profile -> 
            billingClient.getBilling(userId)
                .flatMap(billing -> 
                    notificationClient.getUnread(userId)
                        .map(notes -> new Dashboard(profile, billing, notes))
                )
        );
}
```

**The Correct Pattern (Parallel Execution taking strictly the time of the slowest call - e.g., 1 second):**
```java
public Mono<Dashboard> buildDashboardOptimized(String userId) {
    Mono<Profile> profileMono = profileClient.getProfile(userId);
    Mono<Billing> billingMono = billingClient.getBilling(userId);
    Mono<NotificationCount> notesMono = notificationClient.getUnread(userId);

    // Mono.zip executes all Monos concurrently. It waits for ALL to complete,
    // then passes the results to a combinator function as a Tuple.
    return Mono.zip(profileMono, billingMono, notesMono)
               .map(tuple -> {
                   Profile profile = tuple.getT1();
                   Billing billing = tuple.getT2();
                   NotificationCount notes = tuple.getT3();
                   return new Dashboard(profile, billing, notes);
               });
}
```

---

## Pattern 2: Graceful Degradation & Fallbacks (The Circuit Breaker Approach)

**Scenario:** We are querying an unreliable third-party API. If they fail or timeout, our system should not fail. We should log the error and serve cached or default data instead of returning an HTTP 500 to our front-end.

```java
public Mono<ProductDetails> fetchProduct(String productId) {
    return unreliableThirdPartyApi.getProduct(productId)
        // 1. Don't wait forever. If the API hangs for 2 seconds, throw a TimeoutException downstream.
        .timeout(Duration.ofSeconds(2))
        
        // 2. The API is flaky. If it 500s or timeouts, automatically try exactly 3 more times.
        .retry(3)
        // Note: For production, use retryWhen(Retry.backoff(3, Duration.ofSeconds(1))) 
        // for Exponential Backoff so you don't slam their servers while they are recovering.

        // 3. We tried 4 times total and it's completely down. Log it.
        .doOnError(error -> log.error("Third party down entirely: {}", error.getMessage()))
        
        // 4. Graceful Degradation: Intercept the error signal, swallow it, and switch to a fallback source.
        .onErrorResume(error -> {
            if (error instanceof TimeoutException) {
                 return redisCacheService.getCachedProduct(productId);
            }
            // If they threw a 404 Not Found, we shouldn't mask it with a cache.
            return Mono.error(error); 
        })
        
        // 5. If everything fails (API down, Cache down), return an empty placeholder.
        .onErrorReturn(new ProductDetails("default-id", "Service Unavailable", 0.0));
}
```

---

## Pattern 3: `flatMap` vs `concatMap` (Controlling Concurrency)

**Scenario:** You have a `Flux<Long>` containing user IDs: `Flux.just(1L, 2L, 3L)`. For each ID, you must call `Mono<User> fetchUser(Long id)`. 

### The `flatMap` Pattern (Maximum Parallelism, Order Lost)
*   **Behavior:** `flatMap` eagerly subscribes to the inner publishers as fast as they arrive. If User 3's DB query takes 10ms, but User 1 takes 500ms, the output will arrive out of order (User 3, then User 2, then User 1).
*   **Use when:** Order does not matter. You want maximum throughput.
```java
public Flux<User> fetchQuickly(Flux<Long> userIds) {
    return userIds.flatMap(id -> myDbRepository.fetchUser(id)); // Executes concurrently
}
```

### The `concatMap` Pattern (Sequential Execution, Order Preserved)
*   **Behavior:** `concatMap` waits for the inner publisher to *complete* before subscribing to the next one. It processes User 1 entirely, then starts DB query for User 2.
*   **Use when:** Order strictly matters. Or, you must avoid overwhelming a downstream service with 100 concurrent HTTP requests.
```java
public Flux<User> fetchSequentially(Flux<Long> userIds) {
    return userIds.concatMap(id -> myDbRepository.fetchUser(id)); // Preserves 1, 2, 3 ordering
}
```

---

## Pattern 4: The Kotlin Coroutine Bridge

Project Reactor is notoriously hard to debug because stack traces are full of meaningless assembly/scheduling classes instead of your business logic. Kotlin Coroutines solve this via Suspensions. 

You can seamlessly bridge the two ecosystems using the `kotlinx-coroutines-reactor` library.

**Transforming Reactor interfaces to Coroutines for readability:**

```kotlin
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono

// Scenario: A Spring WebFlux interface that demands you return a Mono.
fun getUserProfile(userId: String): Mono<UserProfile> {
    
    // We use the 'mono { }' builder native to Kotlin. 
    // INSIDE this block, we can write entirely synchronous, imperative-looking code.
    return mono {
        try {
            // .awaitSingle() converts the Mono into a suspending function.
            // The underlying Netty thread DOES NOT BLOCK here. It suspends and works on other requests.
            val user = userRepository.findById(userId).awaitSingle()
            val billing = billingClient.getBilling(userId).awaitSingle()
            
            UserProfile(user, billing)
        } catch (e: Exception) {
            // Native try/catch blocks work perfectly! No need for convoluted onErrorResume operators.
            log.error("Failed to load profile", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
```
*Note:* This bridge pattern is why many modern Spring WebFlux applications are transitioning to strictly Kotlin suspending functions at the controller and service layers, only relying on Reactor/RxJava deep within DB connection pools.
