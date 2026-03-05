# Designing a URL Shortener (e.g., TinyURL / Bitly)

This is the most commonly asked entry-level system design question. It tests your ability to reason about hashing, storage, read-heavy architectures, and scale estimation.

## 1. Functional Requirements

*   **Shorten:** Given a long URL, generate a unique short URL.
*   **Redirect:** Given a short URL, redirect the user to the original long URL.
*   **Custom Alias (Optional):** Allow users to pick a custom short link.
*   **Expiration (Optional):** Links can have a time-to-live (TTL).
*   **Analytics (Optional):** Track click count, geographic data, referrer.

## 2. Non-Functional Requirements

*   **High Availability:** The redirect service must never go down (100:1 read-to-write ratio).
*   **Low Latency:** Redirects must happen in milliseconds.
*   **Shortened URLs should not be guessable** (no simple auto-incrementing IDs exposed).

## 3. Back-of-Envelope Estimation

| Metric               | Estimate                      |
|-----------------------|-------------------------------|
| New URLs/day          | ~100M                         |
| Read:Write ratio      | 100:1 → 10B reads/day        |
| Read QPS              | ~115K QPS (10B / 86400)       |
| Storage per record    | ~500 bytes (URL + metadata)   |
| Storage over 5 years  | ~100M × 365 × 5 × 500B ≈ 91 TB |

## 4. Short URL Generation Strategies

### Option A: Hash + Truncate
1.  Hash the long URL using MD5 or SHA-256 (produces 128/256 bits).
2.  Take the first 7 characters of the Base62 encoding → 62^7 ≈ 3.5 trillion unique keys.
3.  **Collision Handling:** Check if the key exists in DB. If yes, append a counter/salt and re-hash.

### Option B: Pre-generated Key Service (KGS)
1.  A separate Key Generation Service pre-generates all unique 7-character Base62 strings and stores them in a DB.
2.  When a shorten request comes in, the KGS hands out an unused key and marks it as used.
3.  **Advantage:** No collision checking at write time. Decoupled and scalable.
4.  **Disadvantage:** KGS becomes a critical dependency; needs replication.

### Option C: Counter-Based (Snowflake-like)
1.  Use a distributed counter (e.g., Twitter Snowflake) to generate unique 64-bit IDs.
2.  Convert the ID to Base62 for the short URL.
3.  **Advantage:** Globally unique, no collisions.
4.  **Disadvantage:** Sequential IDs may be guessable; need Zookeeper/etcd for coordination.

## 5. High-Level Architecture

```
Client → API Gateway → [Write Path] → URL Service → DB (Write)
                                                   ↘ Cache (Write-through)
         
Client → API Gateway → [Read Path]  → Cache (Hit?) → Return 301/302
                                       ↓ (Miss)
                                       DB → Cache (Populate) → Return 301/302
```

### Database Choice

| Option      | Pros                                   | Cons                                  |
|-------------|----------------------------------------|---------------------------------------|
| **NoSQL** (DynamoDB, Cassandra) | High write throughput, horizontal scaling | No complex queries out of the box |
| **SQL** (PostgreSQL)           | ACID, easy analytics queries            | Harder to scale writes horizontally  |

**Recommendation:** NoSQL (DynamoDB) — the data model is simple (key-value), and the system is heavily read-oriented.

### Caching Layer
*   Use **Redis** or **Memcached** in front of the DB.
*   Cache the most frequently accessed short URLs (80/20 rule — 20% of URLs generate 80% of traffic).
*   Cache eviction: **LRU** (Least Recently Used).

## 6. 301 vs 302 Redirects

| Code  | Meaning             | Behavior                                    |
|-------|---------------------|---------------------------------------------|
| **301** | Moved Permanently | Browser caches the redirect; subsequent requests skip our server. |
| **302** | Found (Temporary) | Browser always hits our server; we can track every click. |

**For analytics:** Use **302** so every click goes through your service.

## 7. Common Interview Follow-Ups

*   **How do you handle hot URLs?** → Cache warming, CDN for extremely hot links.
*   **How do you prevent abuse?** → Rate limiting per API key/IP.
*   **How do you delete expired URLs?** → Background cleanup job scanning for TTL expiry; lazy deletion on read.
*   **How do you ensure uniqueness across data centers?** → KGS with range-based key allocation per DC, or Snowflake IDs with DC prefix bits.
