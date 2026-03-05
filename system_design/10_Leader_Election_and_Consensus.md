# Leader Election and Consensus

In distributed systems, nodes must agree on shared state — who is the leader, what is the current value, what order did events happen. **Consensus algorithms** solve this, even when nodes crash or networks partition.

## 1. Why Do We Need Leader Election?

Many distributed systems require **exactly one node** to perform a particular role:
*   **Kafka:** One broker is the leader for each partition (handles all reads and writes).
*   **Database replication:** One node is the primary (accepts writes).
*   **Distributed locks:** One service instance holds the lock.
*   **Scheduled jobs:** Only one instance should run the cron job.

**The challenge:** If two nodes both believe they are the leader (**split-brain**), data corruption occurs (both accept conflicting writes).

## 2. The Split-Brain Problem

```
        Network Partition
              ↓
  [Node A]  ╳╳╳╳╳  [Node B]
  "I'm the leader!"    "I'm the leader!"
      ↓                    ↓
  Accepts writes        Accepts writes
      ↓                    ↓
     💥 Conflicting data 💥
```

**Solution:** Require a **quorum** (majority) to make decisions. With 3 nodes, a quorum is 2. Neither side of a partition can have a majority alone (at most 1 on one side, 2 on the other).

**Quorum formula:** `(N / 2) + 1` — for 5 nodes, quorum = 3.

## 3. Consensus Algorithms

### Raft (Most Practical, Easiest to Understand)

Raft is the consensus algorithm used by **etcd**, **Consul**, and **CockroachDB**.

**Three roles:**
*   **Leader:** Handles all client requests. Sends log entries to followers.
*   **Follower:** Passive. Replicates the leader's log.
*   **Candidate:** A follower that hasn't heard from the leader and starts an election.

**Leader Election in Raft:**
1.  Each node has a randomized **election timeout** (e.g., 150–300ms).
2.  If a follower doesn't receive a heartbeat from the leader within its timeout, it becomes a candidate.
3.  The candidate increments its **term** number and votes for itself.
4.  It requests votes from other nodes. Each node votes for **at most one** candidate per term.
5.  If the candidate gets votes from a majority → it becomes the new leader.
6.  The leader sends periodic heartbeats to prevent new elections.

**Log replication:** The leader appends commands to its log and replicates them to followers. A command is committed once a majority of nodes have it in their log.

### Paxos (The Original, Harder to Understand)

*   Invented by Leslie Lamport. Theoretically proven correct.
*   More flexible than Raft but notoriously difficult to implement correctly.
*   **Multi-Paxos** is the practical variant used in production systems (Google Chubby, Spanner).
*   **For interviews:** Know that Paxos exists and inspired Raft. You rarely need to explain the full protocol.

### ZAB (ZooKeeper Atomic Broadcast)

*   Used by **Apache ZooKeeper**.
*   Similar to Raft but with different terminology. The leader is called the "Leader" and followers are "Followers".
*   Provides **strong ordering guarantees** for all writes.

## 4. Coordination Services

Instead of implementing consensus yourself, use a coordination service:

| Service        | Algorithm | Used By                                           |
|----------------|-----------|---------------------------------------------------|
| **ZooKeeper**  | ZAB       | Kafka (< v3.x), HBase, Hadoop, Solr              |
| **etcd**       | Raft      | Kubernetes, CockroachDB, CoreDNS                  |
| **Consul**     | Raft      | HashiCorp ecosystem, service discovery            |

### What They Provide
*   **Leader election:** Nodes compete for an ephemeral lock. The winner is the leader.
*   **Distributed locking:** Prevent concurrent access to a shared resource.
*   **Configuration management:** Centralized, consistent config storage.
*   **Service discovery:** Services register themselves; clients look up available instances.

## 5. Distributed Locking

### Using Redis (RedLock)
```java
// Acquire lock
String lockValue = UUID.randomUUID().toString();
boolean acquired = redis.set("lock:order:123", lockValue, "NX", "EX", 10);
// NX = only if not exists, EX = TTL 10 seconds

// Release lock (only if we still own it!)
if (redis.get("lock:order:123").equals(lockValue)) {
    redis.del("lock:order:123");
}
```

**Caution:** Redis-based locks are not as strong as ZooKeeper/etcd locks because Redis is AP (not CP under partitions). Use **RedLock** (lock across multiple independent Redis instances) for better guarantees.

### Using ZooKeeper
*   Create an **ephemeral znode** (auto-deleted when the session ends).
*   If the node crashes, the session expires, and the lock is automatically released.
*   Stronger guarantees than Redis but higher latency.

## 6. Kafka and Leader Election (KRaft)

*   **Before Kafka 3.x:** ZooKeeper managed broker metadata and partition leader election.
*   **Kafka 3.x+ (KRaft mode):** Kafka uses its own Raft-based consensus (KRaft) to elect a **controller quorum** that manages metadata internally. No more ZooKeeper dependency.

## 7. Interview Tips

*   **"How do you prevent two instances from running the same job?"** → Distributed lock (ZooKeeper/etcd/Redis).
*   **"What happens when the leader dies?"** → Followers detect missing heartbeats, start election (Raft), new leader elected in seconds.
*   **Always mention quorum** — it's the fundamental mechanism that prevents split-brain.
*   Know the **CAP trade-off:** ZooKeeper/etcd are **CP** systems (consistent + partition-tolerant, sacrifice availability during partitions).
