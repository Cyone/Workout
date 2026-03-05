# Caching Strategies at Scale

Database calls are slow and expensive. Network latency and disk I/O are the primary bottlenecks in most backend applications. Caching introduces an ultra-fast, in-memory layer (like Redis or Memcached) to bypass the database.

However, caching introduces a monumental challenge: **Cache Invalidation** (keeping the cache data synchronized with the truth in the database).

## 1. Caching Read Patterns

### Cache-Aside (Lazy Loading)
The most common and safest caching strategy. The application logic manages both the cache and the DB.
1.  Application asks the cache for data.
2.  If **Cache Hit**: Return data instantly.
3.  If **Cache Miss**: Application queries the Database.
4.  Application writes the DB result into the Cache.
5.  Return data.
*   **Pros:** Resilient. If Redis crashes, the application falls back to querying the DB directly (though the DB might get overloaded). Only frequently requested data is cached.
*   **Cons:** Cache misses suffer a latency penalty (Cache check + DB check + Cache write).

### Read-Through
The application only ever interacts with the Cache provider. The Cache provider itself is responsible for fetching missing data from the DB.
*   **Pros:** Simplifies application code.
*   **Cons:** Requires a smart caching technology that supports database integration (less common than standard Redis).

## 2. Caching Write Patterns

How do you handle updates? If you update the DB, the cache holds stale data.

### Write-Through
The application writes data to the Cache. The Cache *synchronously* writes data to the Database before returning success to the application.
*   **Pros:** Absolute consistency. The cache is never stale.
*   **Cons:** High write latency. Every write operation pays the penalty of writing to two systems sequentially.

### Write-Behind (Write-Back)
The application writes data *only* to the Cache and immediately returns success. The Cache asynchronously writes the data to the Database in the background (or in batches).
*   **Pros:** Incredible write performance. Perfect for write-heavy workloads (e.g., YouTube view counters).
*   **Cons:** High risk of data loss. If the cache node crashes before the background sync completes, the data is permanently lost.

### Cache Invalidation (Write-Around)
The application updates the Database directly. It then sends a command to the Cache to either `DELETE` the key or `UPDATE` the key with the new value. (Usually paired with Cache-Aside).
*   **The Invalidation Problem:** If the DB update succeeds, but the network call to delete the cache key fails, the cache will permanently serve stale data.

## 3. Eviction Policies
Caches operate in RAM, which is expensive and limited. When the cache is full, how does it decide what to delete to make room for new data?

*   **LRU (Least Recently Used):** Discards the items that haven't been accessed for the longest time. (The industry standard default).
*   **LFU (Least Frequently Used):** Discards items with the lowest hit count. Harder to implement efficiently.
*   **TTL (Time To Live):** A foolproof safeguard. Every cached item is given an expiration time (e.g., 5 minutes). Even if your invalidation logic fails, the stale data will self-destruct after 5 minutes, guaranteeing eventual consistency. **Always set a TTL.**

## 4. Cache Stampede (The Thundering Herd)
A catastrophic failure mode.
*   **Scenario:** A highly popular item (e.g., the front page of an e-commerce site on Black Friday) expires in the cache (TTL drops to 0).
*   **The Disaster:** At that exact millisecond, 10,000 concurrent requests hit the API. They all experience a Cache Miss. All 10,000 threads simultaneously hit the Database to compute the front page. The Database CPU spikes to 100% and crashes.
*   **The Solution:** Distributed Locking (Mutex). The first thread to experience a miss acquires a Redis lock. The other 9,999 threads must wait for a few milliseconds. The first thread queries the DB, repopulates the cache, and releases the lock. The waiting threads then fetch from the repopulated cache.
