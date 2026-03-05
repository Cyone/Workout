# Back-of-Envelope Estimation

System design interviews almost always start with: "Let's estimate the scale." Being able to quickly calculate QPS, storage, bandwidth, and memory requirements demonstrates engineering maturity and grounds your design in reality.

## 1. Latency Numbers Every Engineer Should Know

| Operation                          | Time          | Note                          |
|------------------------------------|---------------|-------------------------------|
| L1 cache reference                 | 0.5 ns        |                               |
| L2 cache reference                 | 7 ns          |                               |
| Main memory reference              | 100 ns        |                               |
| SSD random read                    | 150 µs        | ~1000x slower than memory     |
| HDD random read                    | 10 ms         | ~100x slower than SSD         |
| Send 1 KB over 1 Gbps network     | 10 µs         |                               |
| Read 1 MB from memory             | 250 µs        |                               |
| Read 1 MB from SSD               | 1 ms          |                               |
| Read 1 MB from HDD              | 20 ms         |                               |
| Roundtrip within same datacenter  | 0.5 ms        |                               |
| Cross-continental roundtrip       | 150 ms        |                               |

**Key takeaway:** Memory is ~1000x faster than SSD, SSD is ~100x faster than HDD. Network within a datacenter is fast; cross-continent is slow.

## 2. Power-of-Two Table

| Power  | Value            | Approx.     | Name       |
|--------|------------------|-------------|------------|
| 2^10   | 1,024            | ~1 Thousand | 1 KB       |
| 2^20   | 1,048,576        | ~1 Million  | 1 MB       |
| 2^30   | 1,073,741,824    | ~1 Billion  | 1 GB       |
| 2^40   | ~1.1 Trillion    | ~1 Trillion | 1 TB       |

**Useful shortcut:** 1 day ≈ 100K seconds (86,400 ≈ 10^5).

## 3. Common Estimation Formulas

### QPS (Queries per Second)
```
QPS = Daily Active Users × Avg Requests Per User / 86,400
Peak QPS ≈ 2–3 × Average QPS
```

**Example:** 100M DAU, 10 requests/user/day  
`QPS = 100M × 10 / 86,400 ≈ 11,500 QPS`  
`Peak ≈ 2 × 11,500 ≈ 23,000 QPS`

### Storage Estimation
```
Storage = Daily New Records × Record Size × Retention Period (days)
```

**Example: URL Shortener**  
100M new URLs/day × 500 bytes × 365 × 5 years ≈ 91 TB

### Bandwidth
```
Bandwidth = QPS × Average Response Size
```

**Example:**  
11,500 QPS × 10 KB = 115 MB/s outbound

### Memory (Caching)
Apply the **80/20 rule** — 20% of data generates 80% of traffic.

```
Memory = Daily Requests × Avg Response Size × 0.2 (cache 20%)
```

**Example:**  
1B daily requests × 1 KB × 0.2 = 200 GB → fits in a few Redis instances.

## 4. Estimation Walkthrough: Twitter-like System

| Metric              | Calculation                                | Result        |
|---------------------|--------------------------------------------|---------------|
| DAU                 | Given                                      | 300M          |
| Tweets/day          | 300M × 2 tweets/user                      | 600M          |
| Write QPS           | 600M / 86,400                              | ~7,000        |
| Read QPS (timeline) | 300M × 20 reads/user / 86,400             | ~70,000       |
| Tweet size           | 280 chars × 2 bytes + metadata = ~1 KB    | 1 KB          |
| Daily storage        | 600M × 1 KB                               | 600 GB/day    |
| 5-year storage       | 600 GB × 365 × 5                          | ~1 PB         |
| Media storage/day    | 10% of tweets have images × 500 KB        | ~30 TB/day    |
| Cache (20% of reads) | 70K QPS × 1 KB × 86,400 × 0.2            | ~1.2 TB       |

## 5. Estimation Tips

### Round Aggressively
*   86,400 seconds/day → use **100,000** (10^5).
*   For interview purposes, being within 2x is fine. Don't waste time on exact math.

### State Your Assumptions
*   "I'll assume 100M DAU with 10 requests per user per day."
*   "I'll assume each record is about 500 bytes."
*   Interviewers care about your **thought process**, not the exact number.

### Know the Ranges

| Scale        | QPS          | Storage/day     | Typical Infrastructure       |
|--------------|--------------|-----------------|------------------------------|
| **Startup**  | 10–100       | MBs             | Single server, managed DB    |
| **Growth**   | 1K–10K       | GBs             | Load balancer, read replicas |
| **Scale**    | 10K–100K     | TBs             | Sharding, caching, CDN       |
| **Mega**     | 100K+        | PBs             | Custom infra, global distribution |

## 6. Interview Template

When starting any system design question:

1.  **Clarify requirements** (functional + non-functional).
2.  **Estimate scale:**
    *   Users (DAU/MAU)
    *   Reads/Writes per second
    *   Data size per record
    *   Total storage (over retention period)
    *   Bandwidth
    *   Cache size
3.  **Use these numbers to drive design decisions:**
    *   QPS → "We need horizontal scaling with load balancing."
    *   Storage → "At 1 PB over 5 years, we need sharding."
    *   Read:Write ratio → "100:1 reads, so heavy caching and read replicas."

## 7. Interview Tips

*   Do estimation **at the very beginning** of the interview — it frames every subsequent decision.
*   Write the numbers on the whiteboard. Interviewers love seeing structured estimation.
*   **Don't be afraid to say "Let me assume..."** — assumptions are fine if stated explicitly.
*   Practice estimation for common systems: URL shortener, chat, news feed, and file storage.
