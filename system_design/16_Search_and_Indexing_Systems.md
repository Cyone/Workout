# Search and Indexing Systems

Search is a core feature of almost every application — product search, log analysis, full-text content search. Understanding how search engines work under the hood is a differentiator in system design interviews.

## 1. The Inverted Index

The fundamental data structure behind full-text search.

**Forward Index (what a database does):**
```
Doc1 → "The quick brown fox"
Doc2 → "The lazy brown dog"
```

**Inverted Index (what a search engine does):**
```
"brown" → [Doc1, Doc2]
"fox"   → [Doc1]
"lazy"  → [Doc2]
"quick" → [Doc1]
"the"   → [Doc1, Doc2]
```

Given a search query "brown fox", the engine:
1.  Looks up "brown" → {Doc1, Doc2}
2.  Looks up "fox" → {Doc1}
3.  Intersects → {Doc1}

**Result:** Doc1 matches both terms.

## 2. Elasticsearch Architecture

Elasticsearch is the most widely used full-text search engine in backend systems.

### Core Concepts

| Concept        | Analogy (RDBMS)       | Purpose                                     |
|----------------|-----------------------|---------------------------------------------|
| **Index**      | Database              | Collection of documents with similar structure |
| **Document**   | Row                   | A single JSON record                        |
| **Field**      | Column                | A key in the JSON document                  |
| **Shard**      | Partition             | A subset of the index, distributed across nodes |
| **Replica**    | Read replica          | Copy of a shard for HA and read scaling     |

### How Indexing Works
1.  Document is analyzed: tokenized, lowercased, stemmed ("running" → "run").
2.  Tokens are added to the inverted index for each field.
3.  The inverted index is stored in an immutable **segment** (Lucene segment).
4.  Periodically, small segments are **merged** into larger ones (background compaction).

### Sharding Strategy
*   An index is split into N shards at creation time.
*   Each shard is a self-contained Lucene index.
*   Documents are routed to shards using: `shard = hash(document_id) % num_shards`.
*   **Too few shards:** Limited parallelism. **Too many shards:** High overhead per shard.

## 3. Relevance Scoring

### TF-IDF (Term Frequency – Inverse Document Frequency)
*   **TF:** How often does the term appear in this document? (More = more relevant)
*   **IDF:** How rare is this term across all documents? (Rarer = more important)
*   *"the"* appears in every document → low IDF → low score.
*   *"Kubernetes"* appears in few documents → high IDF → high score.

### BM25 (Modern Default)
*   An improved version of TF-IDF used by Elasticsearch and Lucene.
*   Adds **document length normalization** (short documents with the term rank higher).
*   Adds **saturation** — a term appearing 100 times isn't 100x better than appearing once.

## 4. Autocomplete / Typeahead

When a user types "app", suggest "apple", "application", "app store".

### Data Structure: Trie (Prefix Tree)
```
       root
      / | \
     a  b  c
    /
   p
  / \
 p    l
 |    |
 l    i
 |    |
 e    c
      |
      a
      |
      t
      |
      i
      |
      o
      |
      n
```

### Implementation Options

| Approach                  | Latency    | Complexity | Best For               |
|---------------------------|------------|------------|------------------------|
| **Trie in memory**        | <1ms       | Simple     | Small-medium vocab     |
| **Elasticsearch completion suggester** | ~5ms | Moderate | Production search      |
| **Redis Sorted Sets**     | ~1ms       | Simple     | Popularity-ranked suggestions |

### Redis Sorted Set for Autocomplete
```
ZADD autocomplete 1000 "apple"
ZADD autocomplete 800  "application"
ZADD autocomplete 500  "app store"

// User types "app":
ZRANGEBYLEX autocomplete "[app" "[app\xff" LIMIT 0 10
```

## 5. Full-Text Search vs Vector Search

| Feature            | Full-Text (BM25)            | Vector (Semantic)                  |
|--------------------|-----------------------------|------------------------------------|
| **Matching**       | Keyword matching            | Meaning/similarity matching        |
| **Query**          | "java multithreading"       | "how do I run tasks in parallel"   |
| **Technology**     | Elasticsearch, Solr         | Pinecone, Weaviate, pgvector       |
| **Indexing**       | Inverted index              | HNSW / IVF (ANN algorithms)        |
| **Best for**       | Exact search, filters       | Semantic search, recommendations   |

**Hybrid search** (combining both) is the modern best practice — use BM25 for keyword precision and vector search for semantic recall, then re-rank results.

## 6. Search at Scale

*   **Index partitioning:** Shard by document ID. Each shard handles search independently; results are merged at a coordinator node.
*   **Cache frequent queries:** The same popular searches ("iPhone 16 case") are cached in Redis.
*   **Near real-time indexing:** Elasticsearch has a ~1 second refresh interval. Newly indexed documents become searchable after the next refresh.
*   **Index lifecycle management:** Hot-warm-cold architecture — recent indices on fast SSD nodes, older ones on cheaper storage.

## 7. Interview Tips

*   **"Design a search system"** → Start with inverted index, Elasticsearch architecture, and BM25 scoring.
*   When asked about autocomplete → Trie or Elasticsearch completion suggester.
*   Mention **relevance tuning** — field boosts (title matches rank higher than body matches), custom scoring functions.
*   Know the difference between **full-text search** and **vector search** — interviewers increasingly ask about semantic/AI-powered search.
