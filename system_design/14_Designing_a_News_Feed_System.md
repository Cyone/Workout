# Designing a News Feed System (e.g., Twitter / Instagram Feed)

The news feed is one of the most frequently asked system design interview questions. It tests your understanding of fan-out strategies, caching, ranking, and handling massive read loads.

## 1. Functional Requirements

*   **Publish:** Users create posts (text, images, videos).
*   **Feed Generation:** Each user sees a personalized feed of posts from people they follow.
*   **Feed Refresh:** New posts appear in near real-time.
*   **Pagination:** Infinite scroll with cursor-based pagination.

## 2. The Core Problem: Fan-Out

When User A (who has 10M followers) publishes a post, how do you deliver it to all followers' feeds?

### Fan-Out on Write (Push Model)

When a user publishes a post:
1.  Look up all followers of that user.
2.  Write the post (or a reference to it) into each follower's feed cache.
3.  When a follower opens their feed → read directly from their pre-built cache.

```
User A posts → Fan-Out Service → Write to Feed Cache of Follower 1
                               → Write to Feed Cache of Follower 2
                               → ...
                               → Write to Feed Cache of Follower 10M
```

| Pros                              | Cons                                        |
|-----------------------------------|---------------------------------------------|
| Feed read is instant (pre-built)   | Massive write amplification for popular users |
| Simple read path                   | Wasted work for inactive followers           |
| Predictable read latency           | Post creation latency is high for celebrities |

### Fan-Out on Read (Pull Model)

When a user opens their feed:
1.  Fetch the list of people they follow.
2.  Query each followed user's recent posts.
3.  Merge and rank the posts on-the-fly.

| Pros                              | Cons                                        |
|-----------------------------------|---------------------------------------------|
| No wasted work for inactive users  | High read latency (many queries per request) |
| Post creation is instant           | Repeated computation on every feed refresh  |
| No fan-out bottleneck              | Harder to cache effectively                 |

### Hybrid Approach ✅ (What Facebook/Twitter Actually Use)

*   **Regular users (< 10K followers):** Fan-out on write. Pre-build their followers' feeds.
*   **Celebrities (> 10K followers):** Fan-out on read. When a follower opens their feed, celebrity posts are fetched and merged at read time.

## 3. High-Level Architecture

```
┌──────────┐    ┌───────────────┐    ┌──────────────┐
│  Post    │───→│ Fan-Out       │───→│ Feed Cache   │
│  Service │    │ Service       │    │ (Redis)      │
└──────────┘    └───────────────┘    └──────┬───────┘
     │                                       │
     ▼                                       ▼
┌──────────┐                         ┌──────────────┐
│ Post DB  │                         │ Feed Service │───→ Client
│ (Write)  │                         │ (Read)       │
└──────────┘                         └──────────────┘
```

### Components

| Component           | Technology               | Purpose                           |
|---------------------|--------------------------|-----------------------------------|
| **Post Service**    | REST/gRPC                | Create, store, delete posts       |
| **Post DB**         | PostgreSQL / Cassandra   | Persistent post storage           |
| **Fan-Out Service** | Kafka consumers          | Async fan-out to follower feeds   |
| **Feed Cache**      | Redis Sorted Sets        | Pre-built feed per user (post IDs sorted by time) |
| **Feed Service**    | REST API                 | Read and merge feeds for clients  |
| **Social Graph**    | Graph DB / Redis         | Follower/following relationships  |

## 4. Feed Cache Design (Redis)

Each user has a sorted set in Redis:

```
Key: feed:{userId}
Value: Sorted Set of (postId, timestamp)

ZADD feed:user123 1672531260 "post:abc"
ZADD feed:user123 1672531300 "post:def"

// Get latest 20 posts:
ZREVRANGE feed:user123 0 19
```

*   **Cache size limit:** Keep only the latest N posts per user (e.g., 500). Older posts are fetched from the database on demand.
*   **TTL:** Expire feeds of inactive users to save memory.

## 5. Ranking and Relevance

Simple feeds are reverse chronological. Modern feeds use **ranking algorithms**:

*   **Signals:** Recency, user engagement (likes, comments, shares), content type, user affinity (how often you interact with the poster).
*   **ML Model:** A lightweight model scores each candidate post. Top-K posts are returned.
*   **Real-time re-ranking:** As new signals arrive (new likes), re-rank may adjust, but typically this happens on next feed load.

## 6. Pagination: Cursor-Based (Not Offset-Based)

**Offset-based (bad):** `SELECT * FROM feed OFFSET 100 LIMIT 20` — if new posts are added, the offset shifts and users see duplicates or miss posts.

**Cursor-based (correct):** `SELECT * FROM feed WHERE created_at < :cursor ORDER BY created_at DESC LIMIT 20` — the cursor is the timestamp of the last seen post. Stable pagination regardless of new data.

## 7. Interview Tips

*   **Always discuss the fan-out trade-off** — it's the central design decision. Say "hybrid" and explain why.
*   Draw the read path and write path separately.
*   Mention **Redis Sorted Sets** for the feed cache — it naturally supports ranking and range queries.
*   When asked about scale: "For 500M DAU, the fan-out service processes millions of writes per second. We partition by user ID and use Kafka for async processing."
*   Discuss **content deduplication** — if User A follows B and C, and both repost the same content, how do you avoid showing it twice?
