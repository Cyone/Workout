# Common Database Acronyms and Principles

When designing enterprise systems using Clean Architecture, you will frequently interact with various database paradigms. Understanding these common acronyms is crucial for designing the Data/Infrastructure layer correctly and knowing the trade-offs of your storage decisions.

## 1. ACID (Relational Databases / SQL)
ACID properties guarantee that database transactions are processed reliably. It is the gold standard for traditional RDBMS (PostgreSQL, MySQL, Oracle, etc.).

*   **A - Atomicity:** "All or nothing." A transaction is treated as a single, indivisible logical unit of work. If any part of the transaction fails, the entire transaction is rolled back, and the database is left unchanged.
*   **C - Consistency:** The database must remain in a valid state before and after the transaction. All rules, constraints, cascades, and triggers must be satisfied.
*   **I - Isolation:** Concurrent transactions do not interfere with each other. The intermediate state of a transaction is invisible to other transactions until it is committed. (Often managed via isolation levels like Read Uncommitted, Read Committed, Repeatable Read, Serializable).
*   **D - Durability:** Once a transaction is committed, it will remain in the system even if there's a system failure (e.g., power outage or crash). It is permanently recorded to non-volatile storage.

## 2. BASE (NoSQL Databases)
BASE is the counterpoint to ACID, often used in distributed NoSQL databases (Cassandra, MongoDB, DynamoDB) where high availability and horizontal scalability are prioritized over strict consistency.

*   **B.A. - Basically Available:** The system guarantees availability. It will respond to a request, even if there is a partial system failure, but the response might be stale or not the most recent data.
*   **S - Soft State:** The state of the system could change over time, even without input, due to eventual consistency operations running in the background. It does not have to be strictly consistent at all times.
*   **E - Eventual Consistency:** Given enough time, if no new updates are made, all data copies across the distributed system will eventually converge and become strictly consistent.

## 3. CAP Theorem
The CAP Theorem (Brewer's Theorem) states that a distributed data store can only simultaneously provide **two of the following three** guarantees:

*   **C - Consistency:** Every read receives the most recent write or an error. (All nodes see the exact same data at the exact same time).
*   **A - Availability:** Every request receives a (non-error) response, without the guarantee that it contains the most recent write.
*   **P - Partition Tolerance:** The system continues to operate despite an arbitrary number of messages being dropped or delayed by the network between nodes.

*Note: Because network partitions (P) are inevitable in distributed systems across networks, modern scalable systems must choose between Consistency (CP - like HBase or MongoDB in certain configs) or Availability (AP - like Cassandra or DynamoDB).*

## 4. Architectural Data Patterns (Clean Architecture Context)

*   **CRUD (Create, Read, Update, Delete):** The four basic operations of persistent storage. In Clean Architecture, these usually map directly to repository interfaces defined in the Use Case layer but implemented in the Infrastructure layer.
*   **CQRS (Command Query Responsibility Segregation):** An architectural pattern where the data structures used for reading (Queries) are completely separated from the models used for writing/updating (Commands). This can easily be mapped in Clean Architecture by having separate Use Cases for reads vs. writes, and potentially entirely different databases.
*   **DAO (Data Access Object):** An object/interface that provides an abstract interface to some type of database or other persistence mechanism. By mapping application calls to the persistence layer, the DAO provides specific data operations without exposing details of the database (e.g., SQL queries).
*   **DTO (Data Transfer Object):** An object that carries data between processes. In Clean Architecture, DTOs are often used to pass data across architectural boundaries (e.g., from the Interface Adapters layer out to the Web layer) without coupling those layers to the actual Domain Entities.
*   **ORM (Object-Relational Mapping):** A technique/library (like Hibernate/JPA in Java, Entity Framework in .NET, Exposed in Kotlin) that lets you query and manipulate data from a database using an object-oriented paradigm.
    *   *Clean Architecture Rule:* ORM-annotated entities (like `@Entity` classes) should generally stay in the Infrastructure/Data layer and be manually mapped to pure Domain Entities before passing them inward to the Use Case layer to avoid polluting your domain with database annotations.

## 5. System Workload Acronyms

*   **OLTP (Online Transaction Processing):** Characterized by a large number of short online transactions (INSERT, UPDATE, DELETE). Emphasis is on fast query processing, maintaining data integrity in multi-access environments, and effectiveness measured by transactions per second. (Typical for Microservice databases).
*   **OLAP (Online Analytical Processing):** Characterized by a relatively low volume of transactions but highly complex queries (often involving heavy aggregations). Optimized for read-heavy analytical/business intelligence workloads. (Typical for Data Warehouses).
