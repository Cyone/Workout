# Design Patterns for Scalability

Scalability is the ability to handle increased load by adding resources. This file consolidates the key patterns and decisions that make a system scalable.

## 1. Horizontal vs Vertical Scaling

| Approach           | How                               | Pros                        | Cons                         |
|--------------------|-----------------------------------|-----------------------------|------------------------------|
| **Vertical (Scale Up)** | Bigger machine (more CPU, RAM) | Simple, no code changes    | Hardware limits, single point of failure |
| **Horizontal (Scale Out)** | More machines              | Virtually unlimited, HA     | Requires distributed design  |

**Rule of thumb:** Start vertical (simple), go horizontal when you hit limits or need HA.

## 2. Stateless Services

**The single most important scalability pattern.** A service is stateless if it doesn't store any session-specific data locally.

```
❌ Stateful: Session stored in memory → request must go to the SAME server
✅ Stateless: Session stored in Redis → ANY server can handle ANY request
```

**Benefits:**
*   Any instance can handle any request → easy to scale with a load balancer.
*   Instances are replaceable → auto-scaling, rolling deployments.
*   No sticky sessions needed.

**What needs to be externalized:**
*   Sessions → Redis, JWT tokens
*   File uploads → S3
*   Cache → Redis, Memcached
*   State → Database

## 3. Connection Pooling

Opening a new database connection for every request is expensive (~50-100ms for TCP + TLS + auth handshake).

**Connection Pool:** Maintain a pool of pre-opened connections. Borrow one for a request, return it when done.

```java
// HikariCP (recommended for JVM)
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:postgresql://localhost:5432/db");
config.setMaximumPoolSize(20);       // max connections
config.setMinimumIdle(5);            // keep 5 warm
config.setConnectionTimeout(3000);   // fail fast if no connection available
config.setIdleTimeout(600000);       // close idle connections after 10 min
```

**Sizing formula:** `Pool Size = Number of Threads × (1 + I/O time / CPU time)`

**Common mistake:** Too large a pool → overwhelms the database with connections. PostgreSQL performs poorly beyond ~100 concurrent connections. Use **PgBouncer** as a connection multiplexer.

## 4. Read Replicas

Separate read traffic from write traffic by routing reads to replicas.

```
Writes → Primary DB
Reads  → Read Replica 1, 2, 3 (behind a load balancer)
```

**Caveat — Replication Lag:** Read replicas may be slightly behind. Mitigate with:
*   Read-your-own-writes: After a write, subsequent reads for that user go to the primary for a short window.
*   Monotonic reads: Pin a user's session to a single replica.

## 5. Caching Patterns

### Cache-Aside (Lazy Loading) ✅ Most Common
```
Read: Check cache → Miss → Read from DB → Write to cache → Return
Write: Write to DB → Invalidate cache
```

### Write-Through
```
Write: Write to cache → Cache writes to DB
Read: Always hits cache (always up to date)
```

### Write-Behind (Write-Back)
```
Write: Write to cache → Return immediately → Cache asynchronously writes to DB
```
*   Fastest writes, but data can be lost if cache crashes before flushing.

### Cache Invalidation Strategies
*   **TTL-based:** Set expiry time. Simple but data may be stale.
*   **Event-based:** On DB write, publish event → invalidate cache entry.
*   **Version-based:** Cache key includes a version number. Increment on write.

## 6. Asynchronous Processing

Move non-critical work out of the request path.

```
Synchronous (blocking):
POST /orders → validate → charge → send email → respond (slow!)

Asynchronous (non-blocking):
POST /orders → validate → charge → publish event → respond (fast!)
                                        ↓
                               Email worker (async)
                               Analytics worker (async)
                               Notification worker (async)
```

**Technologies:** Kafka, RabbitMQ, SQS, Redis Streams.

## 7. Database Sharding Strategies

When a single database can't handle the load, split data across multiple databases.

| Strategy              | How                                          | Consideration              |
|----------------------|----------------------------------------------|----------------------------|
| **Hash-based**       | `shard = hash(key) % N`                     | Even distribution          |
| **Range-based**      | `users A-M → shard 1, N-Z → shard 2`       | Hot spots if ranges uneven |
| **Directory-based**  | Lookup table maps key → shard               | Flexible but adds latency  |
| **Geographic**       | EU users → EU shard, US → US shard          | Low latency, compliance    |

**Challenges:**
*   Cross-shard queries (JOINs) are expensive or impossible.
*   Resharding (changing N) requires data migration.
*   Unique constraints across shards need application-level enforcement.

## 8. Load Balancing at Every Tier

```
DNS → Global Load Balancer (Geographic routing)
    → Regional Load Balancer (L7 HTTP)
        → Application Tier (N instances)
            → Database Tier (Primary + Replicas)
```

**Algorithms:**
*   **Round Robin:** Simple, equal distribution.
*   **Least Connections:** Route to the least busy server.
*   **Weighted:** Give more traffic to more powerful servers.
*   **Consistent Hashing:** For cache/session affinity.

## 9. Auto-Scaling

Automatically adjust the number of instances based on load.

| Metric                  | Scale Out When              | Scale In When               |
|-------------------------|-----------------------------|-----------------------------|
| CPU utilization         | > 70% for 5 minutes        | < 30% for 10 minutes       |
| Request queue depth     | > 100 pending requests      | < 10 pending                |
| Custom metric (QPS)     | > threshold QPS             | < threshold                 |

**Cool-down period:** After scaling, wait 5 minutes before another scaling action to avoid thrashing.

## 10. The Scalability Checklist

When designing for scale, ask yourself:

- [ ] Are services **stateless**? (Session externalized)
- [ ] Is there a **caching layer**? (Redis/Memcached)
- [ ] Can reads and writes scale **independently**? (Read replicas, CQRS)
- [ ] Is heavy work done **asynchronously**? (Message queues)
- [ ] Is the database **sharded** or partitioned?
- [ ] Is there a **CDN** for static content?
- [ ] Is there **auto-scaling** based on load metrics?
- [ ] Is there a **load balancer** at every tier?
- [ ] Are connections **pooled** (DB, HTTP)?
- [ ] Are you using **consistent hashing** for distributed caches?

## 11. Interview Tips

*   **Start simple, scale as needed.** "Initially a single server is fine. As traffic grows to X QPS, we add a load balancer and read replicas. At Y QPS, we introduce caching and sharding."
*   **Know the scaling thresholds:** When does a single PostgreSQL server stop being enough? (~10K QPS for simple queries, ~1K for complex ones.)
*   **Always mention trade-offs:** "Caching improves read latency but introduces cache invalidation complexity."
*   The best answer isn't the most complex — it's the one that grows organically with load.
