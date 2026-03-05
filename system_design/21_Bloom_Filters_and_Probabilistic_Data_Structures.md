# Bloom Filters and Probabilistic Data Structures

When you need to check membership, count unique items, or estimate frequencies at massive scale, exact data structures (HashSets, databases) consume too much memory. Probabilistic data structures trade a small error rate for dramatic space savings.

## 1. Bloom Filter

A space-efficient structure that answers: **"Is element X in the set?"**

*   **False positives are possible:** It may say "probably yes" when the element is NOT in the set.
*   **False negatives are impossible:** If it says "definitely no", the element is guaranteed not in the set.

### How It Works

1.  A bit array of `m` bits, initialized to all 0s.
2.  `k` independent hash functions.
3.  **Insert(x):** Hash `x` with all `k` functions. Set the corresponding `k` bit positions to 1.
4.  **Query(x):** Hash `x` with all `k` functions. If ALL `k` positions are 1 → "probably in set". If ANY position is 0 → "definitely not in set".

```
m = 10 bits, k = 3 hash functions

Insert("apple"):  h1=2, h2=5, h3=8      → [0,0,1,0,0,1,0,0,1,0]
Insert("banana"): h1=1, h2=5, h3=9      → [0,1,1,0,0,1,0,0,1,1]

Query("cherry"):  h1=2, h2=7, h3=9      → bits[2]=1, bits[7]=0 → DEFINITELY NOT
Query("grape"):   h1=1, h2=5, h3=8      → bits[1]=1, bits[5]=1, bits[8]=1 → PROBABLY YES
                                           (but grape was never inserted → false positive!)
```

### Tuning Parameters
*   **m (bit array size):** Larger → fewer false positives, more memory.
*   **k (hash functions):** Optimal `k = (m/n) × ln(2)` where `n` = expected items.
*   **False positive rate:** With `m=10 bits/element` and optimal `k` ≈ 1% FP rate.

### Real-World Usage

| System           | Use Case                                                   |
|------------------|------------------------------------------------------------|
| **Cassandra**    | Before reading from disk, check Bloom filter: "Could this key be in this SSTable?" Avoids unnecessary disk I/O. |
| **Chrome**       | Check URLs against a Bloom filter of known malicious sites. Only query the full database on positive matches. |
| **Medium**       | Track which articles a user has already seen to avoid recommending duplicates. |
| **CDN/Cache**    | Avoid caching one-hit-wonders: only cache items that appear in the Bloom filter (seen at least once). |

## 2. HyperLogLog (HLL)

Answers: **"How many unique items are in the set?"** (Cardinality estimation)

Counting unique visitors, unique IPs, or unique search queries with exact `HashSet` requires `O(n)` memory. With 1 billion unique items, that's GBs of RAM.

**HyperLogLog uses ~12 KB of memory** regardless of cardinality, with ~0.81% standard error.

### How It Works (Simplified)
1.  Hash each element to a binary string.
2.  Count the number of leading zeros in the hash. More leading zeros → rarer pattern → implies larger cardinality.
3.  Use multiple "registers" (sub-estimates) and harmonically average them for accuracy.

### Usage in Redis
```
PFADD unique_visitors "user_123"
PFADD unique_visitors "user_456"
PFADD unique_visitors "user_123"   // duplicate, no effect

PFCOUNT unique_visitors → 2 (approximately)

// Merge two HLLs:
PFMERGE combined page1_visitors page2_visitors
PFCOUNT combined → unique visitors across both pages
```

**Used by:** Redis (`PFADD`, `PFCOUNT`), Google BigQuery (`APPROX_COUNT_DISTINCT`), Presto/Trino.

## 3. Count-Min Sketch

Answers: **"How many times has element X appeared?"** (Frequency estimation)

Like a Bloom filter for counting. Uses a 2D array of counters with `d` hash functions.

*   **Never underestimates:** The true count ≤ estimated count.
*   **May overestimate** due to hash collisions.

### Real-World Usage
| Use Case                          | How                                         |
|-----------------------------------|---------------------------------------------|
| **Heavy hitters detection**       | Find the top-K most frequent items in a stream |
| **Rate limiting**                 | Count requests per user without exact per-user counters |
| **Network monitoring**            | Estimate packet frequency per source IP      |

## 4. Comparison

| Structure         | Question Answered           | Space    | Error Type       |
|-------------------|-----------------------------|----------|------------------|
| **Bloom Filter**  | Is X in the set?            | O(n) bits| False positives  |
| **HyperLogLog**   | How many unique items?      | ~12 KB   | ±0.81%           |
| **Count-Min Sketch** | How many times has X appeared? | O(d×w) counters | Overestimates |
| **Cuckoo Filter** | Is X in the set? (supports delete) | Similar to Bloom | False positives |

## 5. Interview Tips

*   **"How do you check if a username is taken without hitting the database?"** → Bloom filter as a first-pass filter. If "definitely not" → available (no DB query needed). If "probably yes" → check the database to confirm.
*   **"How do you count unique users per day at scale?"** → HyperLogLog. 12KB per day, merge across time periods.
*   Mentioning these structures in a system design interview shows you think about **space-efficient, scalable solutions** — a strong differentiator.
*   Know the trade-off: **small memory + small error** vs **exact answer + large memory**.
