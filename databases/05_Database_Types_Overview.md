# Comprehensive Guide to Database Types

Choosing the right database architecture is fundamental to system design. No single database is optimal for all use cases (often referred to as Polyglot Persistence).

## 1. Relational Databases (RDBMS)
*   **Examples:** PostgreSQL, MySQL, Oracle, SQL Server
*   **Data Model:** Tables with rows and columns. Strictly enforced schema defined before data entry. Focuses on normalized data and relationships (Foreign Keys).
*   **Primary Use Case:** Transactional systems requiring strict ACID compliance, predictable queries, and complex reporting (e.g., banking, ERP, inventory management).
*   **Pros:** Strict data integrity, mature technology, ubiquitous query language (SQL), powerful multi-table JOINs.
*   **Cons:** Hard to scale horizontally (usually requires complex sharding or reading replicas only), schema rigidity makes rapid iteration difficult, object-relational impedance mismatch.

## 2. Document Databases (NoSQL)
*   **Examples:** MongoDB, Couchbase, Amazon DocumentDB
*   **Data Model:** Collections storing semi-structured documents (usually JSON or BSON). Documents within the same collection can have different fields.
*   **Primary Use Case:** Content management, user profiles, rapid prototyping, read-heavy applications where the data model closely matches the application object in memory.
*   **Pros:** Flexible schema (easy to add fields dynamically), excellent horizontal scalability (built native sharding), easy for developers to map objects to data without a heavy ORM.
*   **Cons:** Lack of strong cross-document ACID transactions ( historically, though improving now), weak or complex JOIN capabilities resulting in data duplication/denormalization, larger storage footprint than relational databases.

## 3. Key-Value Stores (NoSQL)
*   **Examples:** Redis, Amazon DynamoDB, Memcached
*   **Data Model:** The simplest model; a massive hash table. Every item in the database is stored as an attribute name (or "key"), together with its value.
*   **Primary Use Case:** Caching, session management, shopping carts, leaderboards. High velocity read/write of simple atomic data.
*   **Pros:** Phenomenal read/write speed (especially in-memory ones like Redis), effortless horizontal scaling, simple design.
*   **Cons:** Cannot query by value (only by key), no complex relationship support, values are usually opaque strings/blobs to the database.

## 4. Column-Family Stores (Wide-Column NoSQL)
*   **Examples:** Apache Cassandra, HBase, ScyllaDB
*   **Data Model:** Data is stored in columns rather than rows. A "row" can have millions of columns, and different rows can have entirely different column names.
*   **Primary Use Case:** Extremely high write throughput, event logging, IoT telemetry, massive-scale distributed data (handling petabytes).
*   **Pros:** Incredible write performance, masterless architecture means high availability (no single point of failure), scales linearly across commodity hardware across multiple data centers.
*   **Cons:** Very rigid query patterns (you must design your schema specifically for the queries you will run), no JOINs or complex aggregations, high learning curve (e.g., Cassandra Query Language).

## 5. Graph Databases
*   **Examples:** Neo4j, Amazon Neptune, ArangoDB
*   **Data Model:** Nodes (entities), Edges (relationships connecting entities), and Properties (key-value metadata attached to both).
*   **Primary Use Case:** Fraud detection (finding rings), recommendation engines, social networks, complex access control mapping.
*   **Pros:** Traversal queries (finding "friends of friends") traverse linked data almost instantaneously (O(1) hop), extremely intuitive for representing highly interconnected domains.
*   **Cons:** Poor performance for bulk aggregation/scanning queries (e.g., calculating average age of all users), difficult to shard/distribute graph data efficiently across multiple servers.

## 6. Vector Databases
*   **Examples:** Pinecone, Qdrant, Milvus, Weaviate
*   **Data Model:** Stores high-dimensional vectors (arrays of numbers) representing unstructured data (text embeddings, image features).
*   **Primary Use Case:** LLM context injection (Retrieval-Augmented Generation / RAG), semantic similarity search, reverse image search.
*   **Pros:** Optimized heavily for Approximate Nearest Neighbor (ANN) search, critical for generative AI workflows, supports combining metadata filters with vector similarity.
*   **Cons:** Niche use-case, nascent technology ecosystem, generally not suited for primary transactional record keeping (often used alongside a traditional DB).

## 7. Time-Series Databases (TSDB)
*   **Examples:** InfluxDB, Prometheus, TimescaleDB
*   **Data Model:** Append-only data streams where every record is stamped with a time. Optimized for storing events as they happen chronologically.
*   **Primary Use Case:** Application monitoring metrics, financial market data (stock ticks), IoT sensor data logging.
*   **Pros:** Highly compressed storage for sequential data, extremely fast ingest rates, specialized functions for continuous aggregation/downsampling over time windows.
*   **Cons:** Not designed for updating or deleting specific past records, typically very limited CRUD capability outside of sequential appends.
