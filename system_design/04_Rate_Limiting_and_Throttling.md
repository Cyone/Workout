# Rate Limiting and Throttling

Rate limiting protects your system from being overwhelmed by too many requests — whether from legitimate traffic spikes, misbehaving clients, or malicious attacks (DDoS).

## 1. Why Rate Limiting Matters

*   **Prevents resource exhaustion:** Stops a single client from consuming all DB connections, CPU, or memory.
*   **Fair usage:** Ensures all users get a reasonable share of the system.
*   **Cost control:** Limits downstream API calls (e.g., you pay per call to a third-party API).
*   **Security:** Mitigates brute-force login attempts, credential stuffing, and scraping.

## 2. Rate Limiting Algorithms

### Token Bucket
*   A bucket holds up to `N` tokens. Tokens are added at a fixed rate (e.g., 10/sec).
*   Each request consumes 1 token. If the bucket is empty, the request is rejected (HTTP 429).
*   **Allows bursts:** A full bucket can handle `N` requests instantly before throttling kicks in.
*   **Used by:** AWS API Gateway, Stripe.

```
Bucket capacity: 10 tokens
Refill rate: 2 tokens/sec

t=0: bucket=10 → 10 requests arrive → bucket=0 (all pass)
t=1: bucket=2  → 3 requests arrive → 2 pass, 1 rejected
t=2: bucket=2  → 1 request arrives → passes, bucket=1
```

### Leaky Bucket
*   Requests enter a FIFO queue (the bucket). The queue is processed at a **fixed rate**.
*   If the queue is full, new requests are dropped.
*   **Smooths bursts** into a constant output rate.
*   **Difference from Token Bucket:** Token Bucket allows bursts; Leaky Bucket does not.

### Fixed Window Counter
*   Divide time into fixed windows (e.g., 1-minute intervals). Count requests per window.
*   If count exceeds the limit, reject until the next window.
*   **Problem — Boundary Burst:** A client could send `N` requests at `t=0:59` and `N` more at `t=1:00`, getting `2N` requests through in 2 seconds across the window boundary.

### Sliding Window Log
*   Keep a timestamp log of each request. For each new request, count entries within the last `W` seconds.
*   **Precise** but **memory-intensive** (stores every timestamp).

### Sliding Window Counter (Hybrid) ✅ Recommended
*   Combines Fixed Window Counter + weighted overlap from the previous window.
*   Formula: `rate = prev_count × overlap% + current_count`
*   **Best balance** of accuracy and memory efficiency.

## 3. Distributed Rate Limiting with Redis

In a microservices architecture, rate limiting must be centralized (each service instance can't maintain its own counter).

```java
// Pseudo-code: Sliding Window Counter with Redis
String key = "rate:" + userId + ":" + currentMinute;

long count = redis.incr(key);
if (count == 1) {
    redis.expire(key, 60); // TTL = window size
}

if (count > MAX_REQUESTS_PER_MINUTE) {
    return HTTP_429_TOO_MANY_REQUESTS;
}
```

**Redis MULTI/EXEC** or **Lua scripts** ensure atomicity — `INCR` + `EXPIRE` must execute together.

## 4. Where to Apply Rate Limiting

| Layer              | Tool/Technology                  | Use Case                    |
|--------------------|----------------------------------|-----------------------------|
| **API Gateway**    | Kong, AWS API GW, Spring Cloud GW | Per-client/API-key limits   |
| **Load Balancer**  | Nginx (`limit_req_zone`)         | Per-IP limits               |
| **Application**    | Resilience4j, Guava RateLimiter  | Internal service protection |
| **Database**       | Connection pool limits            | Prevent connection exhaustion |

## 5. HTTP Response Headers

When rate limiting, return informative headers:

```
HTTP/1.1 429 Too Many Requests
Retry-After: 30
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1672531260
```

## 6. Interview Tips

*   **"Design a rate limiter"** is a standalone system design question — be prepared to discuss algorithms, Redis atomicity, and distributed challenges.
*   Know the trade-offs: Token Bucket (allows bursts, simple) vs Sliding Window Counter (accurate, slightly more complex).
*   Mention **graceful degradation:** Instead of hard 429, you can queue requests, serve degraded responses, or apply different limits per tier (free vs premium).
*   **Race conditions in distributed systems:** Two requests arriving simultaneously might both read `count=99` (limit=100) and both increment, allowing 101 requests. Solution: Lua scripts or Redis `MULTI`.
