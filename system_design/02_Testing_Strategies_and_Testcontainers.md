# Testing Strategies and Testcontainers

A mature backend requires a robust testing pyramid. Relying solely on unit tests leaves integration bugs undetected, while relying solely on end-to-end tests makes the pipeline unbearably slow.

## 1. The Testing Pyramid (Backend Focused)

1.  **Unit Tests (Fast, Numerous, Isolated):**
    *   Test individual classes/functions (Domain logic, Use Cases, utility methods).
    *   Dependencies are mocked (e.g., using Mockito or MockK).
    *   *No Spring Context.* The test should not use `@SpringBootTest`. It should be pure JUnit/KotlinTest.

2.  **Integration Tests (Medium Speed, Focused):**
    *   Test interactions between the application and external systems (Database, Kafka, external REST APIs), or complex interactions between multiple internal Spring beans.
    *   Uses a partial or full Spring Context (e.g., `@DataJpaTest`, `@WebMvcTest`).
    *   *Real Infrastructure:* This is where Testcontainers shines.

3.  **End-to-End (E2E) Tests (Slow, Few, Comprehensive):**
    *   Start the entire application via a Docker container, populate a realistic database state, and hit the public API endpoints via HTTP clients or tools like RestAssured.
    *   Tests the system exactly as a client would use it.

## 2. Mocking Boundaries (When NOT to mock)

*   **DON'T Mock Data Classes/Value Objects/Entities:** Let the real domain objects flow through your tests. Mocking a `User` entity is unnecessary boilerplate and hides internal logic errors.
*   **DON'T Mock at the Edges:** If you are writing an Integration Test for a `UserRepository` adapter, do not mock the internal `CrudRepository`. Use an actual database.
*   **DO Mock External Services in Core Unit Tests:** When testing a `CreateOrderUseCase`, mock the `PaymentService` to simulate various scenarios (success, network timeout, insufficient funds) reliably.
*   **WireMock for external HTTP:** In integration tests, instead of mocking the Java interface that calls an external REST API, use WireMock to spin up a local HTTP server that returns canned JSON responses. This tests your actual HTTP client configuration (timeouts, serialization, retries).

## 3. The Power of Testcontainers

Historically, integration tests relied on in-memory databases like H2. This created a massive discrepancy:
*   *The Problem (H2 vs Postgres):* Code that works against H2 often fails against PostgreSQL in production due to dialect differences, lack of advanced features (like JSONB or specialized indexes), or different transaction locking behaviors.
*   *The Legacy Solution:* Maintaining complex, fragile local `docker-compose` setups that developers had to start manually before running tests.

**Enter Testcontainers:**
A Java library that provides lightweight, throwaway instances of common databases, message brokers, or anything that can run in a Docker container.

### How it completely changes Integration Testing
1.  **Real Dependencies:** Your tests run against identical, real versions of PostgreSQL, Redis, or Kafka. The "works on my machine but not production" problem disappears for infrastructure.
2.  **Ephemeral:** The container starts when the test starts and is automatically destroyed when the test finishes. No leftover state.
3.  **Programmatic Control:** The application starts the container dynamically from within the test code.

### Example: Spring Boot Integration Test with PostgreSQL

```java
@SpringBootTest // Start the Spring Application Context
@Testcontainers // JUnit 5 extension to manage the container lifecycle
class UserRepositoryIntegrationTest {

    // Define a static container (shared across all tests in the class)
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    // Dynamically inject the database URL into Spring's properties
    // Because the container maps to a random available port on the host
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUserFromRealDatabase() {
        // This test interacts with an actual isolated PostgreSQL instance!
        userRepository.save(new User("Alice"));
        assertThat(userRepository.count()).isEqualTo(1);
    }
}
```

*   **Kafka Testcontainers:** You can start a Confluent Kafka container to test your `@KafkaListener` annotations and producer configurations, validating exactly-once semantics locally.
*   **Redis Testcontainers:** Essential for verifying that distributed locks (`SETNX`) or caching behavior works correctly against a real Redis instance, rather than mocking cache operations.
