# MongoDB Internals and Architecture

MongoDB is a document-oriented NoSQL database. While early versions had a reputation for data loss in edge cases, modern MongoDB (with the WiredTiger storage engine) guarantees ACID compliance at the document level (and multi-document level since 4.0).

## 1. Storage Architecture: WiredTiger

WiredTiger is the default storage engine for MongoDB. It handles how data is written to memory and flushed to disk.

*   **Document-Level Concurrency:** Unlike older engines (MMAPv1) that locked the entire collection during writes, WiredTiger uses document-level concurrency control. Multiple clients can modify different documents in the same collection simultaneously.
*   **Compression:** It automatically compresses data on disk (using Snappy by default, but Zstd is also supported), significantly reducing storage footprint compared to uncompressed JSON/BSON.
*   **The Cache:** WiredTiger maintains an internal cache alongside the OS page cache. By default, it allocates 50% of (RAM - 1GB) for its internal cache.
*   **Checkpoints:** Similar to relational databases, WiredTiger creates checkpoints (snapshots of data on disk) every 60 seconds or 2GB of journal data, ensuring fast recovery after a crash.

## 2. BSON (Binary JSON)

MongoDB stores data as BSON, not plain string JSON.
*   **Why BSON?**
    1.  **Traversability:** JSON is a string; finding a field requires parsing the whole string. BSON adds length prefixes to elements, allowing the engine to skip over elements it doesn't need to read.
    2.  **Rich Types:** JSON lacks data types like `Date` and `BinData` (byte arrays). BSON supports these natively.

## 3. High Availability: Replica Sets

A Replica Set is a group of `mongod` instances that maintain the same data set.

*   **Primary node:** The only node that accepts write operations. It records all changes in its `oplog` (Operations Log).
*   **Secondary nodes:** Asynchronously replicate the `oplog` and apply the changes to their own data sets.
*   **Automated Failover:** If the Primary goes offline (heartbeat fails for 10 seconds), the remaining nodes hold an election. The node with the most up-to-date `oplog` is elected as the new Primary within seconds. Application drivers automatically route queries to the new Primary.
*   **Read Preference:** By default, clients read from the Primary for strong consistency. You can configure "Read from Secondaries" (e.g., for heavy analytical queries) if eventual consistency is acceptable.

## 4. Scalability: Sharding

When a Replica Set maxes out its hardware capacity (RAM, CPU, Disk I/O), MongoDB scales horizontally via Sharding.

*   **Shards:** Each shard is a full Replica Set holding a *portion* of the data.
*   **Mongos (Routers):** Lightweight proxy servers the application connects to. They know which data lives on which shard.
*   **Config Servers:** Store the metadata and routing tables.
*   **The Shard Key:** The most critical decision in MongoDB. You choose a field (like `user_id` or `country_code`) to distribute the data.
    *   *Ranged Sharding:* Good for range queries, but can cause "hotspots" (e.g., sharding by `timestamp` means all new writes go to a single shard).
    *   *Hashed Sharding:* Guarantees even distribution of writes, but range queries become scatter-gather operations across all shards.

## 5. Advanced Indexing

Beyond standard B-Tree indexing, MongoDB offers specialized indexes:

*   **Multikey Indexes:** Automatically created when you index an array. It creates an index entry for every element in the array, making `db.users.find({ tags: "sports" })` extremely fast.
*   **Compound Indexes:** Indexing multiple fields. Order matters based on the **ESR Rule**:
    1.  **Equality:** Fields queried on exact matches first.
    2.  **Sort:** Fields used to order the results.
    3.  **Range:** Fields filtered by bounds (`$gt`, `$lt`).
*   **Geospatial Indexes:** (`2dsphere`) Optimized for calculating distance on an earth-like sphere, enabling queries like "Find 10 closest restaurants to this lat/long".
*   **Text Indexes:** Supports stemming and stop words for basic unstructured text search within documents.
