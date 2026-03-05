# API Rate Limiting and Throttling

Rate limiting is a critical production concern: it protects backend services from abuse, prevents resource exhaustion, and ensures fair usage across all clients.

## 1. Rate Limiting vs. Throttling

These terms are often used interchangeably, but have distinct meanings:
*   **Rate Limiting:** A hard rejection policy. Once a client exceeds the defined rate, requests are rejected with `429 Too Many Requests` until the window resets.
*   **Throttling:** A graceful slowing policy. Requests are queued or delayed rather than immediately rejected. Commonly used inside a single service for downstream calls.

---

## 2. Core Algorithms

### Algorithm 1: Fixed Window Counter
*   **Mechanism:** Maintain a per-client counter that resets at the beginning of each fixed time window (e.g., every 60 seconds).
*   **Storage:** A single counter in Redis: `INCR user:42:requests:2024-01-01T10:00`.
*   **Problem — The Boundary Burst Attack:** A client can send 100 requests at 10:59:59 and 100 more at 11:00:01, effectively making 200 requests in 2 seconds while respecting the "100 per minute" limit technically.

### Algorithm 2: Sliding Window Log
*   **Mechanism:** Store the timestamp of every request. When a new request arrives, count how many timestamps are within the rolling window (e.g., the last 60 seconds). If under the limit, allow it; otherwise, reject.
*   **Pros:** Perfectly accurate. No boundary burst problem.
*   **Cons:** High memory usage—you must store individual timestamps for every request. Impractical at high scale.

### Algorithm 3: Sliding Window Counter (Hybrid) ⭐ *Most Common in Production*
*   **Mechanism:** A compromise. Keep the current window counter AND the previous window counter. Calculate the approximate rolling rate by weighting them:
    ```
    rate = (prev_window_count × (1 - elapsed_fraction)) + current_window_count
    ```
    If `rate < limit`, allow the request.
*   **Pros:** Low memory (2 counters per client). Very accurate (eliminates most boundary edge cases). Used by **Cloudflare**.

### Algorithm 4: Token Bucket ⭐ *Classic Algorithm*
*   **Mechanism:** Each client has a "bucket" with a maximum capacity (e.g., 100 tokens). Tokens are added at a constant refill rate (e.g., 10 tokens/second). Each request consumes 1 token. If the bucket is empty, the request is rejected.
*   **Pros:**
    *   Allows controlled **burst traffic** — if a client hasn't made requests for 5 seconds, their bucket refills, and they can burst 50 requests instantly (if capacity = 50). This is realistic behavior for mobile apps.
    *   Simple mental model.
*   **State stored:** `{tokens: 87, last_refill_timestamp: 1700000000}`.
*   **Used by:** AWS API Gateway, Stripe.

### Algorithm 5: Leaky Bucket
*   **Mechanism:** Requests enter a FIFO queue (the "bucket") and are processed at a constant, fixed rate. If the queue is full, new requests overflow and are dropped.
*   **Pros:** Smooths out bursty traffic into a perfectly constant output rate. Protects the downstream service from any bursts.
*   **Cons:** Does not allow bursty behavior at all. A legitimate burst from a mobile app on startup will be queued or dropped.
*   **Best for:** Protecting a bottleneck resource that can only handle a steady, known rate (e.g., a payment processor).

---

## 3. Distributed Rate Limiting with Redis

A single-instance rate limiter is easy. In a horizontally scaled system with 10 API Gateway nodes, each node must share the counter state.

**The Solution: Redis as a shared atomic counter.**

Using Redis's atomic operations (`INCR`, `SET`, `EXPIRE`), you can implement a correct, race-condition-free counter:

```
# Atomic Lua script (runs transactionally in Redis)
local key = "rl:user:42"
local limit = 100
local window = 60  -- seconds

local count = redis.call("INCR", key)
if count == 1 then
  redis.call("EXPIRE", key, window)
end

if count > limit then
  return 0  -- rejected
else
  return 1  -- allowed
end
```

*   **Why Lua?** Redis processes Lua scripts atomically. The `INCR` + `EXPIRE` pair is a classic race condition if run as two separate commands (a new key could be incremented but never expired). Lua eliminates this.
*   **Redis Cluster:** In a sharded Redis cluster, all keys for the same user must land on the same shard (use a consistent hash key). Gateway nodes talk to Redis on every request — Redis latency (~0.5ms) rarely matters vs. the benefit of accuracy.

---

## 4. Where to Enforce Rate Limits

| Layer | Tool | Advantage |
|---|---|---|
| **Edge / CDN** | Cloudflare, Fastly | Drops malicious traffic before it hits your infrastructure. Best for DDoS protection. |
| **API Gateway** | Kong, AWS API Gateway | Centralized policy enforcement. Quota management per API key/client. |
| **Service Level** | Spring Boot Resilience4j | Per-service protection of internal resources. Fine-grained, business-logic-aware limits. |

**Best Practice:** Use layered enforcement. The CDN drops obvious abuse, the API Gateway enforces per-customer quotas, and the service itself throttles internal downstream calls.

---

## 5. HTTP Response Semantics

When a request is rate-limited, the correct response is:

```
HTTP/1.1 429 Too Many Requests
Retry-After: 30
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1700000060
```

*   **`429 Too Many Requests`** — Standard, unambiguous status code.
*   **`Retry-After`** — Tells the client how many seconds to wait (or an HTTP date). Well-behaved clients use this for exponential backoff.
*   **`X-RateLimit-*` headers** — De facto standard (no official RFC). Proactively tell clients their remaining budget so they can throttle themselves before hitting the limit. Used by GitHub, Twitter, Stripe.
*   **`503 Service Unavailable`** — Also sometimes used for throttling when the server is overloaded (not just per-client limits), typically with a `Retry-After` header.

---

## 6. Rate Limiting vs. Circuit Breaker

A common interview follow-up: what's the difference?

*   **Rate Limiting:** Protects a service *from its clients* (inbound traffic control).
*   **Circuit Breaker:** Protects a service *from its dependencies*. If Service A calls Service B and B starts failing, the circuit breaker "opens" and stops A from hammering a struggling B, giving it time to recover (see Resilience4j / Hystrix).
