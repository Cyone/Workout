# Clean Architecture & Hexagonal Architecture

As applications grow, traditional Layered Architectures (Controller -> Service -> Repository) often degrade into "Big Balls of Mud" where domain logic becomes inextricably tied to framework implementation details (like Spring `@Entity` annotations creeping into business rules).

## 1. The Core Philosophy
Both Clean Architecture (Robert C. Martin) and Hexagonal Architecture (Alistair Cockburn, Ports & Adapters) share one fundamental rule: **Dependencies point inward.**

*   **The Center:** The inner layers contain the enterprise/application business rules (Domain Models and Use Cases). They must have **zero dependencies** on external frameworks, UI, databases, or infrastructure.
*   **The Outer Layers:** These contain the implementations (Spring MVC Controllers, Hibernate Repositories, Kafka Listeners). They depend on the inner layers.

## 2. Hexagonal Architecture (Ports and Adapters)
This is the most practical implementation for modern microservices. It visualizes the application as a central hexagon of business logic, surrounded by "Ports" and "Adapters".

### Ports (Interfaces)
Ports are the entry and exit points to the application core. They define *what* the application needs to do or what it needs from the outside world, without defining *how*.

*   **Primary (Driving) Ports:** Interfaces that external actors (users, other systems) use to interact *with* the application. (e.g., `interface CreateUserUseCase`).
*   **Secondary (Driven) Ports:** Interfaces the application uses to interact with external systems (databases, external APIs). (e.g., `interface UserRepository`).

### Adapters (Implementations)
Adapters translate between the outside world's format and the application's internal format, plugging into the Ports.

*   **Primary (Driving) Adapters:** They call the Primary Ports. Example: A Spring `@RestController` that receives an HTTP request, parses the JSON, and calls the `CreateUserUseCase` port. Or a Kafka listener that consumes a message and calls the same port.
*   **Secondary (Driven) Adapters:** They implement the Secondary Ports. Example: A `JpaUserRepository` that translates the domain `User` object into a Hibernate `@Entity` and runs SQL to save it to PostgreSQL.

## 3. The Dependency Rule in Practice (Spring Boot)
The hardest part for Java developers is completely decoupling the Domain from Spring.

**The Anti-Pattern (Standard Layered):**
```java
// Domain Object is polluted with DB annotations
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column
    private String name;
}
```
Here, the core Domain object depends on JPA (`javax.persistence.*`). If you want to switch from Postgres (JPA) to MongoDB, you must modify your core business logic.

**The Clean Architecture Approach:**
1.  **Domain Layer:**
    ```java
    // Pure Java (POJO). No external libraries.
    public class User {
        private String id;
        private String name;
        // Business rules regarding name validation go here.
    }
    ```
2.  **Secondary Port (Domain Interface):**
    ```java
    public interface UserRepository {
        void save(User user);
    }
    ```
3.  **Adapter Layer (Infrastructure):**
    ```java
    // The Spring Data JPA Entity (hidden from the Domain)
    @Entity
    @Table(name="users")
    class UserEntity { /* JPA annotations */ }

    // The Spring Component implementing the Port
    @Component
    public class PostgresUserRepositoryAdapter implements UserRepository {
        private final SpringDataJpaUserRepository repository; // actual Spring repo
        
        @Override
        public void save(User domainUser) {
            // 1. Map Domain User -> UserEntity (Adapter responsibility)
            UserEntity entity = UserMapper.toEntity(domainUser);
            // 2. Save using Spring Data
            repository.save(entity);
        }
    }
    ```

## 4. Why go through this effort?
*   **Testability:** You can test the entire business logic (Domain + Use Cases) completely without Spring or a database. You just mock the Secondary Ports. Unit tests run in milliseconds instead of seconds.
*   **Framework Independence:** Upgrading Spring Boot versions or swapping libraries (e.g., ActiveMQ to Kafka) only requires rewriting a small Adapter, not the entire codebase. The domain remains untouched.
*   **Deferring Decisions:** You can start writing the business logic before you decide whether to use Postgres, Mongo, or Redis. You can just write an `InMemoryUserRepositoryAdapter` to start.
