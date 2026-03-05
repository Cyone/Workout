# Idempotency and Retry Patterns

In distributed systems, **failures are inevitable** — networks drop packets, services crash, deployments restart pods. Designing for resilience means embracing retries, but retries without idempotency lead to duplicate side effects (double charges, duplicate orders).

## 1. Idempotency

An operation is **idempotent** if executing it multiple times produces the same result as executing it once.

| Operation                    | Idempotent? | Why                                       |
|------------------------------|-------------|-------------------------------------------|
| `GET /users/123`             | ✅ Yes      | Reading doesn't change state              |
| `PUT /users/123 {name:"A"}` | ✅ Yes      | Setting the same value repeatedly = same  |
| `DELETE /users/123`          | ✅ Yes      | Deleting something already gone = no-op   |
| `POST /orders`               | ❌ No       | Each call creates a **new** order         |
| `POST /payments/charge`      | ❌ No       | Each call charges the card **again**      |

### Making Non-Idempotent Operations Idempotent

**Use an Idempotency Key:** The client generates a unique key (UUID) and sends it with the request. The server checks if it has already processed this key.

```java
@PostMapping("/payments")
public ResponseEntity<?> charge(@RequestHeader("Idempotency-Key") String key,
                                 @RequestBody PaymentRequest request) {
    // 1. Check if this key was already processed
    Optional<PaymentResult> existing = idempotencyStore.find(key);
    if (existing.isPresent()) {
        return ResponseEntity.ok(existing.get()); // Return cached result
    }
    
    // 2. Process the payment
    PaymentResult result = paymentService.charge(request);
    
    // 3. Store the result with the key (atomically with step 2 if possible)
    idempotencyStore.save(key, result);
    
    return ResponseEntity.ok(result);
}
```

**Stripe** uses this pattern — every API call accepts an `Idempotency-Key` header.

### Database-Level Idempotency
*   **Unique constraints:** `INSERT ... ON CONFLICT DO NOTHING` (PostgreSQL).
*   **Conditional updates:** `UPDATE ... WHERE version = @expectedVersion` (optimistic locking).
*   **Deduplication tables:** Store processed message IDs. Before processing, check if the ID exists.

## 2. Retry Patterns

### Exponential Backoff with Jitter

**Naive retry (bad):** Retry every 1 second → All failed clients retry at the **same time** → Thundering herd overwhelms the recovering service.

**Exponential backoff:** Wait `2^attempt` seconds between retries.  
**With jitter:** Add random variation to spread out retries.

```java
// Exponential Backoff with Full Jitter
int maxRetries = 5;
for (int attempt = 0; attempt < maxRetries; attempt++) {
    try {
        return callService();
    } catch (TransientException e) {
        long baseDelay = (long) Math.pow(2, attempt) * 1000; // 1s, 2s, 4s, 8s, 16s
        long jitter = ThreadLocalRandom.current().nextLong(0, baseDelay);
        Thread.sleep(jitter); // Random delay between 0 and baseDelay
    }
}
throw new ServiceUnavailableException("Max retries exceeded");
```

### When to Retry (and When NOT To)

| Situation                     | Retry? | Reason                                   |
|-------------------------------|--------|------------------------------------------|
| HTTP 500 Internal Server Error | ✅     | Server might recover                     |
| HTTP 503 Service Unavailable   | ✅     | Server is overloaded, may clear up       |
| Network timeout                | ✅     | Transient network issue                  |
| HTTP 400 Bad Request           | ❌     | Client error — retrying won't fix it     |
| HTTP 401 Unauthorized          | ❌     | Invalid credentials — needs human action |
| HTTP 409 Conflict              | ⚠️     | Retry with updated state (re-read first) |

## 3. Circuit Breaker Pattern

**Problem:** If a downstream service is completely down, retrying is wasteful and adds latency. The caller should **fail fast** instead.

**States:**
1.  **Closed (normal):** Requests flow through. Failures are counted.
2.  **Open (tripped):** After `N` failures in a window → all requests immediately fail with a fallback response. No actual calls to the downstream service.
3.  **Half-Open (probing):** After a timeout, allow a few requests through. If they succeed → Close. If they fail → Open again.

```java
// Using Resilience4j (Spring Boot ecosystem)
@CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
public PaymentResult charge(PaymentRequest req) {
    return restTemplate.postForObject("/payments", req, PaymentResult.class);
}

public PaymentResult paymentFallback(PaymentRequest req, Throwable t) {
    // Queue for async retry, return pending status, or use cached data
    return PaymentResult.pending("Payment queued for processing");
}
```

### Resilience4j Configuration (Typical)

```yaml
resilience4j.circuitbreaker:
  instances:
    paymentService:
      slidingWindowSize: 10          # last 10 calls
      failureRateThreshold: 50       # open at 50% failure rate
      waitDurationInOpenState: 30s   # stay open for 30 seconds
      permittedNumberOfCallsInHalfOpenState: 3
```

## 4. Bulkhead Pattern

**Problem:** One slow dependency (e.g., Payment Service) consumes all threads in the thread pool, blocking requests to healthy services (e.g., User Service, Product Service).

**Solution:** Isolate each downstream dependency into its own thread pool or semaphore.

```
Thread Pool "payment"   → max 10 threads → Payment Service
Thread Pool "inventory" → max 10 threads → Inventory Service
Thread Pool "email"     → max 5 threads  → Email Service
```

If Payment Service hangs, only its 10 threads are consumed. User-facing endpoints remain responsive.

## 5. Timeout Strategy

**Always set timeouts.** A missing timeout turns a transient failure into a permanent hang.

| Layer                  | Timeout                         | Typical Value |
|------------------------|---------------------------------|---------------|
| HTTP client            | Connection timeout              | 1–3s          |
| HTTP client            | Read/response timeout           | 3–10s         |
| Database               | Query timeout                   | 5–30s         |
| Circuit breaker        | Slow call duration threshold    | 5s            |
| Message queue consumer | Visibility timeout              | 30–300s       |

## 6. Combining the Patterns

The recommended order of resilience layers:

```
Request → Timeout → Retry (with backoff) → Circuit Breaker → Bulkhead → Call
```

*   **Timeout** prevents infinite waits.
*   **Retry** handles transient failures.
*   **Circuit Breaker** stops cascading failures.
*   **Bulkhead** isolates the blast radius.

## 7. Interview Tips

*   **"How do you handle duplicate messages in Kafka?"** → Idempotent consumer (deduplication table keyed by message ID).
*   **"What happens if the payment service is down?"** → Circuit breaker with fallback (queue for later), retry with exponential backoff once it recovers.
*   **"How do you prevent double charging?"** → Idempotency key on the payment API.
*   Always mention **Resilience4j** for Spring Boot / JVM projects — it's the modern replacement for Hystrix.
