# 3. The Dependency Rule and Architecture Layers

Traditional layered architectures (Presentation -> Business -> Data Access) inadvertently couple your business logic to your database or framework. Clean Architecture uses concentric circles to enforce separation of concerns.

## A. The Dependency Rule

**"Source code dependencies must point only inward, toward higher-level policies."**
This is the single most important rule in the book.

*   Nothing in an inner circle can know anything at all about something in an outer circle.
*   Data formats used in an outer circle should not be used by an inner circle (e.g., an `@Entity` class from the DB layer should never reach the Entities layer).
*   *Why?* Changes in UI (Outer) should not force changes in Business Rules (Inner). Changing PostgreSQL to MongoDB should be a standalone operation.

---

## B. The Four Concentric Layers

From the absolute center, moving outward:

### 1. Entities (Enterprise Business Rules)
*   **What it is:** The objects that encapsulate the most general and high-level rules of your business. If you are building a banking app, the `Account` and its `deposit()` and `withdraw()` logic live here.
*   **Dependencies:** None. This layer knows nothing about the outer layers. It is pure Java/Kotlin/POJOs.
*   **Stability:** Highly stable. This code should only change if the fundamental way your enterprise does business changes.

### 2. Use Cases (Application Business Rules)
*   **What it is:** The rules specific to the *application* you are building on top of the enterprise rules. It orchestrates the flow of data to and from the Entities.
*   **Example:** `TransferMoneyUseCase`. It fetches an `Account` entity (using a defined interface), validates the funds, calls the entity's `withdraw()` method, and saves it.
*   **Dependencies:** It depends inwards on the Entities. It does *not* depend outwards on the UI or Database.
*   **Stability:** Less stable than Entities, but stable against changes in databases, frameworks, or UI.

### 3. Interface Adapters
*   **What it is:** A set of adapters that convert data from the format most convenient for the Use Cases and Entities, to the format most convenient for some external agency like the Database or Web.
*   **Common Components:**
    *   **Presenters & Controllers:** (e.g., Spring MVC `@RestController`). They receive HTTP JSON, convert it into an input object the Use Case understands, and call the Use Case.
    *   **Gateways/Repositories:** (e.g., Spring Data JPA). They implement the interfaces defined by the Use Cases. They translate Domain Entities into DB Tables (Rows/Documents) and execute SQL.
*   **Dependencies:** Depends inwards on Use Cases and Entities.

### 4. Frameworks and Drivers
*   **What it is:** The outermost circle. This is where all the details go. The Web framework (Spring MVC), the Database (PostgreSQL), the Message Broker (Kafka).
*   **What lives here?** Mostly glue code. You don't write much business logic here. You configure how the outer ring connects to the inner ring (e.g., Spring `@Configuration` classes wiring up the Use Cases).
*   **Volatility:** Highly volatile. You should be able to upgrade Spring Boot 2 to 3, or swap Kafka for Redis Streams, solely by changing the code in this outermost layer. The inner three layers compile and run seamlessly without noticing.

---

## C. Crossing Boundaries (The Dependency Inversion Principle)

How does a `UseCase` save data if it cannot depend on the `Database` adapter?

**It uses Polymorphism to invert the dependency.**

1.  The `UseCase` needs to save a user. It defines an interface in its own layer: `interface UserRepository { fun save(user: User) }`.
2.  The `UseCase` calls `userRepository.save(user)`. It only knows about the interface.
3.  In the *Adapter Layer*, you create a `PostgresUserRepository` that implements the `UserRepository` interface.
4.  At runtime (usually via Spring DI), the `PostgresUserRepository` object is injected into the `UseCase`.

**The Result:** The flow of control crosses the boundary going outwards (UseCase calls DB), but the source code dependency points inwards (DB implements UseCase's interface).
