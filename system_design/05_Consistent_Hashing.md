# Consistent Hashing

Consistent hashing solves the problem of distributing data across a cluster of nodes such that adding or removing a node doesn't require re-mapping the majority of keys.

## 1. The Problem with Simple Hashing

With `N` servers, simple modular hashing assigns keys:
```
server = hash(key) % N
```

**When a server is added or removed**, `N` changes, and virtually **every key** maps to a different server. This causes a cache "stampede" — massive cache misses, all hitting the database simultaneously.

| Event              | Keys Re-mapped       |
|--------------------|----------------------|
| Simple hash + node added   | ~100% of keys |
| Consistent hash + node added | ~1/N of keys (only keys on the affected segment) |

## 2. How Consistent Hashing Works

### The Hash Ring
1.  Imagine a circular number space from `0` to `2^32 - 1` (the "ring").
2.  **Hash each server** (by IP or name) onto this ring. Each server sits at a specific point.
3.  **Hash each key** onto the same ring. A key is assigned to the **first server found clockwise** from its position.

```
         S1
       /    \
     K1      K2 → S2
    /            \
  S4              S2
    \            /
     K3 → S4   K4 → S2
       \    /
         S3
```

### Adding a Server
When server `S5` is added between `S4` and `S1`:
*   Only the keys between `S4` and `S5` (previously routed to `S1`) are re-mapped to `S5`.
*   All other keys remain unaffected.

### Removing a Server
When `S2` goes down:
*   Only the keys on `S2`'s arc are re-assigned to the next server clockwise (`S3`).
*   Minimal disruption.

## 3. Virtual Nodes (Vnodes)

**Problem:** With few physical servers, the hash ring can be unbalanced — one server may own a much larger arc than others, receiving disproportionate traffic.

**Solution:** Each physical server is mapped to **multiple virtual nodes** on the ring (e.g., 100–200 vnodes per server).

```
Physical Server A → VNode_A1, VNode_A2, ..., VNode_A150
Physical Server B → VNode_B1, VNode_B2, ..., VNode_B150
```

*   Virtual nodes are spread around the ring, resulting in a **much more even distribution**.
*   Heterogeneous hardware: A more powerful server can have more vnodes, receiving proportionally more traffic.
*   When a server is removed, its load is spread evenly across many remaining servers (not dumped on one neighbor).

## 4. Real-World Usage

| System          | How It Uses Consistent Hashing                                    |
|-----------------|-------------------------------------------------------------------|
| **Cassandra**   | Partitions data across nodes. Each node owns a token range on the ring. |
| **DynamoDB**    | Internal data partitioning across storage nodes.                  |
| **Memcached**   | Client-side consistent hashing to pick which cache node stores a key. |
| **Nginx**       | `upstream` consistent hashing for sticky load balancing.          |
| **Kafka**       | Consumer group partition assignment uses a form of consistent hashing. |
| **Redis Cluster**| Hash slots (16384 slots) are a form of consistent hashing.       |

## 5. Implementation Sketch (Java)

```java
public class ConsistentHashRing<T> {
    private final TreeMap<Long, T> ring = new TreeMap<>();
    private final int virtualNodes;
    private final HashFunction hashFunction;

    public ConsistentHashRing(int virtualNodes, HashFunction hashFunction) {
        this.virtualNodes = virtualNodes;
        this.hashFunction = hashFunction;
    }

    public void addNode(T node) {
        for (int i = 0; i < virtualNodes; i++) {
            long hash = hashFunction.hash(node.toString() + "#" + i);
            ring.put(hash, node);
        }
    }

    public void removeNode(T node) {
        for (int i = 0; i < virtualNodes; i++) {
            long hash = hashFunction.hash(node.toString() + "#" + i);
            ring.remove(hash);
        }
    }

    public T getNode(String key) {
        long hash = hashFunction.hash(key);
        // Find the first node clockwise from the hash
        Map.Entry<Long, T> entry = ring.ceilingEntry(hash);
        if (entry == null) {
            entry = ring.firstEntry(); // wrap around
        }
        return entry.getValue();
    }
}
```

## 6. Interview Tips

*   **Draw the ring.** Interviewers expect you to draw the circular hash space and show how keys get assigned.
*   **Always mention virtual nodes** — it's the critical improvement that makes consistent hashing practical.
*   Know that consistent hashing is about **minimizing re-mapping**, not eliminating it entirely.
*   When discussing caching or database scaling, consistent hashing is the default answer for "How do you distribute data across nodes?"
