# Database Scaling and Sharding

As your application grows from thousands to millions of users, a single relational database instance will eventually run out of CPU, RAM, or disk I/O capacity. You must scale the database tier.

## 1. Vertical Scaling (Scaling Up)
*   **Concept:** Buy a bigger server. Add more RAM, faster NVMe SSDs, and more CPU cores to the single database machine.
*   **Pros:** Zero application code changes. Instant performance boost. Maintains all ACID transactional guarantees.
*   **Cons:**
    *   **Hard Limits:** There is a physical limit to how large a single machine can be. You will hit a ceiling.
    *   **Cost:** Hardware prices scale exponentially. A 128-core server is vastly more expensive than four 32-core servers.
    *   **Single Point of Failure:** If that massive machine experiences a hardware failure, the entire system is down.

## 2. Horizontal Scaling (Scaling Out): Read Replicas
Most web applications are heavily read-biased (e.g., 95% reads, 5% writes). We can exploit this to scale easily.

*   **Concept:** Maintain one **Primary (Master)** database node that handles all `INSERT`, `UPDATE`, and `DELETE` queries. Create multiple **Replica (Slave)** nodes. The Primary asynchronously streams its transaction log to the Replicas.
*   **Routing:** The application routes all `SELECT` queries to the Replicas, distributing the read load across many machines.
*   **The Trade-off (Replication Lag):** Because the replication is asynchronous, a replica might be a few milliseconds (or seconds, under heavy load) behind the primary.
    *   *The Bug:* A user updates their profile picture (write to Primary), the page refreshes and queries a Replica. The Replica hasn't received the update yet, so the old picture is shown.
    *   *The Fix:* Applications must be designed to tolerate this eventual consistency, or enforce "Read-Your-Own-Writes" logic (e.g., if a user just updated an entity, force their next read to go to the Primary).

## 3. Horizontal Scaling: Sharding (Data Partitioning)
If your application generates so much data that it physically cannot fit on one disk, or the *write* throughput exceeds what a single Primary node can handle, you must shard.

*   **Concept:** Break the massive database into smaller, independent databases (Shards) spread across multiple servers.
*   **How it works:** You pick a **Shard Key** (e.g., `user_id`). You use a hashing function to determine which server holds that user's data.
    *   User A (ID 105) hashes to Shard 1.
    *   User B (ID 942) hashes to Shard 2.
*   **Pros:** Infinite scalability for both reads and writes. Massive storage capacity.
*   **The Massive Drawbacks:**
    1.  **No Cross-Shard Joins:** You cannot easily perform a SQL `JOIN` between a table on Shard 1 and a table on Shard 2. You have to pull the data into the application layer and join it in memory (very slow).
    2.  **No Cross-Shard Transactions:** Standard ACID guarantees do not work across multiple servers.
    3.  **Resharding Complexity:** What happens when Shard 1 gets full, and you need to add Shard 3? You have to recalculate the hash for millions of rows and move them between live servers without downtime. (This is solved by **Consistent Hashing** algorithms).
    4.  **Celebrity Problem (Hotspots):** If your shard key is `tenant_id`, and one tenant is a massive enterprise corporation while the rest are startups, the shard holding the enterprise tenant will be overwhelmed, defeating the purpose of load distribution.

### Note on Modern Architecture
Because manual sharding of a PostgreSQL/MySQL database is an operational nightmare, modern highly-scalable applications typically rely on distributed NoSQL databases (like Cassandra or DynamoDB) or NewSQL databases (like CockroachDB or Google Spanner) which handle sharding, replication, and rebalancing completely automatically under the hood.
