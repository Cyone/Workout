# Designing a Notification System

Notification systems seem simple but involve complex routing, fan-out, rate management, and multi-channel delivery.

## 1. Notification Channels

| Channel       | Latency    | Delivery Guarantee | Use Case                          |
|---------------|------------|-------------------|-----------------------------------|
| **Push (Mobile)** | Near real-time | Best-effort (device may be offline) | Alerts, reminders, social |
| **Email**     | Seconds–minutes | Reliable (queued) | Transactional, marketing         |
| **SMS**       | Seconds    | Reliable          | 2FA, critical alerts              |
| **In-App**    | Real-time  | Best-effort       | Product updates, social activity  |
| **WebSocket** | Real-time  | Session-bound     | Live dashboards, chat             |

## 2. High-Level Architecture

```
Event Source (services) → Notification Service → Priority Queue
                                                      ↓
                                              Channel Router
                                    ┌────────────┼────────────┐
                                    ↓            ↓            ↓
                              Push Worker   Email Worker  SMS Worker
                                    ↓            ↓            ↓
                                APNs/FCM    SES/SendGrid  Twilio/SNS
```

### Key Components

*   **Notification Service:** Accepts notification requests, validates, enriches with user preferences, and routes to the correct channel queue.
*   **User Preferences Store:** Stores per-user settings — opted-in channels, quiet hours, language, timezone.
*   **Template Engine:** Renders notification content from templates + variables (avoid hardcoding messages in services).
*   **Priority Queue:** Separate queues per priority (P1: OTP/alerts, P2: social, P3: marketing). P1 is processed first.
*   **Rate Limiter:** Prevent notification fatigue (max 5 push notifications per hour per user).
*   **Deduplication:** Same event shouldn't trigger the same notification twice (idempotency key per event+user).

## 3. Fan-Out Strategies

**Scenario:** "Send a notification to all 10M users about a new feature."

| Strategy              | How It Works                                      | Trade-off                 |
|-----------------------|---------------------------------------------------|---------------------------|
| **Fan-out on write**  | Pre-compute: for each user, write a notification record | High write cost, fast read |
| **Fan-out on read**   | Store the event once; when user opens app, check for new events | Low write, slower read    |
| **Hybrid**            | Fan-out for active users; batch for inactive       | Best of both              |

## 4. Reliability Patterns

*   **At-least-once delivery:** Acknowledge the message only after confirmed delivery to the channel provider.
*   **DLQ for failures:** Failed notifications after N retries go to a Dead Letter Queue for manual review.
*   **Delivery tracking:** Store delivery status (pending → sent → delivered → read) with timestamps.
*   **Fallback channels:** If push fails (token expired), fall back to email or SMS.

## 5. Push Notification Flow (Mobile)

```
App Server → FCM/APNs → Device
                ↓
        Delivery Receipt (optional)
```

*   **Device Token Management:** Users register device tokens on app launch. Tokens can expire or change — handle `InvalidRegistration` errors by removing stale tokens.
*   **APNs (Apple):** Requires TLS certificate or token-based auth. Delivers to iOS.
*   **FCM (Google):** HTTP v1 API. Delivers to Android, iOS, and web.

## 6. Preventing Notification Fatigue

*   **Per-user rate limits:** Max notifications per channel per time window.
*   **Quiet hours:** Respect user timezone — no push at 3 AM.
*   **Aggregation/Batching:** Instead of "Alice liked your post", "Bob liked your post" → "Alice and 5 others liked your post."
*   **Unsubscribe with granularity:** Let users disable marketing but keep transactional. Required by CAN-SPAM/GDPR.

## 7. Interview Tips

*   Start by asking: **What types of notifications?** (Transactional vs marketing changes the design significantly.)
*   Separate the **event producer** from the **notification system** — loose coupling via message queue.
*   Mention **template service** — never hardcode notification text in application code.
*   Scale discussion: Separate queues per channel, horizontal scaling of workers, database partitioning by user ID.
