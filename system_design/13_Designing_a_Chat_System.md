# Designing a Chat System (e.g., WhatsApp / Slack)

A chat system requires real-time messaging, persistent storage, presence tracking, and handling users going offline and coming back online.

## 1. Functional Requirements

*   **1:1 messaging:** Send and receive text messages between two users.
*   **Group chat:** Send messages to a group of users (up to N members).
*   **Online/Offline status:** Show who's currently online.
*   **Read receipts:** Indicate when a message has been delivered/read.
*   **Message history:** Users can scroll back through past messages.
*   **Media (Optional):** Images, videos, file attachments.

## 2. Communication Protocol: WebSocket

HTTP is request-response — the server can't push messages to the client. For real-time chat, we need **persistent bidirectional connections**.

| Protocol           | Direction       | Use Case                        |
|--------------------|-----------------|----------------------------------|
| **WebSocket**      | Bidirectional   | Chat, real-time collaboration   |
| **SSE**            | Server → Client | Notifications, live feeds       |
| **HTTP Long Polling** | Simulated push | Fallback when WebSocket unavailable |

### WebSocket Connection Flow
1.  Client opens a WebSocket connection to a **Chat Server**.
2.  The Chat Server maintains a mapping: `userId → WebSocket connection`.
3.  When User A sends a message to User B:
    *   Message arrives at the Chat Server via A's WebSocket.
    *   Server looks up B's connection → delivers the message via B's WebSocket.
4.  If User B is **offline**, the message is stored in a **message queue** for later delivery.

## 3. High-Level Architecture

```
┌─────────┐  WebSocket   ┌──────────────┐       ┌───────────────┐
│ Client A │─────────────→│  Chat Server │──────→│ Message Store │
└─────────┘              │  (Stateful)   │       │ (Cassandra)   │
                          └──────┬───────┘       └───────────────┘
                                 │
                          ┌──────▼───────┐
                          │  Message     │
                          │  Queue       │
                          │  (Kafka)     │
                          └──────┬───────┘
                                 │
┌─────────┐  WebSocket   ┌──────▼───────┐
│ Client B │←────────────│  Chat Server │
└─────────┘              │  (Stateful)   │
                          └──────────────┘
```

### Service Decomposition

| Service                | Responsibility                                           |
|------------------------|----------------------------------------------------------|
| **Chat Service**       | WebSocket management, message routing                    |
| **Presence Service**   | Tracks online/offline status, last seen                  |
| **Group Service**      | Group membership, group metadata                         |
| **Message Store**      | Persistent storage of all messages                       |
| **Push Notification**  | Delivers push notifications to offline users             |
| **Media Service**      | Upload, compress, store, serve media files (S3 + CDN)    |

## 4. Message Storage

### Database Choice

| Requirement               | SQL (PostgreSQL)                 | NoSQL (Cassandra)               |
|---------------------------|----------------------------------|---------------------------------|
| **Write throughput**      | Limited (single master)          | Very high (distributed writes)  |
| **Read pattern**          | Complex queries                  | Simple partition key lookups    |
| **Message ordering**      | ORDER BY with index              | Clustering key (natural order)  |
| **Scale**                 | Vertical + read replicas         | Horizontal (add nodes)          |

**Recommended:** **Cassandra** or **ScyllaDB** — partition key = `conversation_id`, clustering key = `timestamp`. This makes "get messages for conversation X, ordered by time" extremely fast.

```sql
-- Cassandra schema
CREATE TABLE messages (
    conversation_id UUID,
    message_id TIMEUUID,
    sender_id UUID,
    content TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (conversation_id, message_id)
) WITH CLUSTERING ORDER BY (message_id ASC);
```

## 5. Group Chat: Fan-Out

When a message is sent to a group of 500 members:
1.  Message is written to the group's conversation in the Message Store (single write).
2.  Message is published to a Kafka topic for the group.
3.  Chat Servers consuming that topic look up which group members are currently connected and deliver via WebSocket.
4.  For offline members → push notification.

**Optimization:** Don't copy the message 500 times. Store once, reference by `conversation_id`. Each client reads from the same conversation.

## 6. Presence Service (Online/Offline)

*   On WebSocket connect: mark user as `online` in Redis.
*   On WebSocket disconnect: mark user as `offline`, record `last_seen`.
*   **Heartbeat mechanism:** Client sends periodic heartbeats (every 30s). If no heartbeat for 60s → mark offline.
*   **Fan-out presence updates:** When User A goes offline, notify A's contacts who are online. For users with thousands of contacts, use **lazy presence** — only query status when a chat window is opened.

## 7. Handling Offline Users

1.  Message arrives for User B who is offline.
2.  Message is persisted in the Message Store.
3.  A push notification is sent (APNs/FCM).
4.  When User B comes back online and reconnects:
    *   Pull undelivered messages since `last_seen` timestamp.
    *   Mark them as delivered → sender sees delivery receipt.

## 8. Interview Tips

*   **Always start with WebSocket** for the communication protocol. Mention HTTP long polling as the fallback.
*   Discuss **stateful vs stateless** — Chat Servers are stateful (they hold WebSocket connections). Use a **connection registry** (Redis) so any server can look up which server holds a user's connection.
*   **End-to-end encryption (E2EE):** Mention Signal Protocol — messages are encrypted on the sender's device and decrypted only on the recipient's device. The server never sees plaintext.
*   **Message ordering:** Within a conversation, use Snowflake-style IDs or `TIMEUUID` for strict ordering.
