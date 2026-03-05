## Java + Spring interview prep materials (curated, practical)

### 1) High-signal topic checklist (what to be ready to explain + code)
#### Core Java (esp. for backend roles)
- **Collections & generics**: `HashMap` internals, `equals`/`hashCode`, `Comparable` vs `Comparator`, immutability, performance tradeoffs.
- **Concurrency**: `synchronized`, `volatile`, happens-before, thread pools (`ExecutorService`), `CompletableFuture`, locks, deadlocks, concurrent collections.
- **JVM basics**: heap vs stack, GC fundamentals (what creates GC pressure), memory leaks in Java (static caches, listeners).
- **I/O & serialization**: streams, NIO basics, JSON mapping concepts (Jackson: annotations, polymorphism pitfalls).
- **Modern Java (8–17)**: lambdas/streams pitfalls, records, sealed classes (basics), `Optional` (when/when not), pattern matching (high-level).

#### Spring / Spring Boot
- **DI & bean lifecycle**: component scanning, stereotypes, `@Configuration` vs `@Component`, proxying, bean scopes, `@PostConstruct`, circular deps.
- **Spring MVC**: request lifecycle, filters vs interceptors vs controllers, validation (`@Valid`), exception handling (`@ControllerAdvice`), content negotiation.
- **Data layer**: Spring Data JPA, transaction boundaries (`@Transactional`), isolation/propagation, lazy loading, N+1, entity mapping pitfalls.
- **Security** (if relevant): authentication vs authorization, filter chain, method security, JWT basics.
- **Actuator & observability**: health, metrics, logs, tracing concepts.
- **Configuration**: profiles, properties binding (`@ConfigurationProperties`), environment precedence.
- **Testing**: slice tests (`@WebMvcTest`, `@DataJpaTest`), full context tests (`@SpringBootTest`), Testcontainers basics.

#### System design (common for mid+)
- API design (idempotency, pagination, versioning)
- Database modeling + indexing + transactions
- Caching (local vs distributed, TTLs, cache stampede)
- Messaging (Kafka/RabbitMQ basics, at-least-once vs exactly-once as a concept)
- Resiliency (timeouts, retries, circuit breakers)
- Consistency and concurrency control

---

### 2) Practice plan (2 weeks, 60–90 min/day)
**Days 1–3 (Java fundamentals + coding)**
- 20–30 min: 1–2 LeetCode-style problems (arrays/strings/maps)
- 30 min: Collections + generics deep dive
- 20 min: concurrency fundamentals (thread pool, `volatile`, race conditions)

**Days 4–7 (Spring core)**
- Build a small REST API:
    - CRUD endpoints
    - validation + global exception handler
    - a service layer with transactions
- Add tests (`@WebMvcTest` + `@DataJpaTest`)
- Explain bean creation and request flow out loud (mock interview style)

**Days 8–10 (Data + transactions + performance)**
- JPA mapping exercises: `@OneToMany`, join fetch, pagination pitfalls
- Diagnose N+1 and fix with fetch joins / entity graphs
- `@Transactional` propagation/isolation scenarios

**Days 11–14 (system design + behavioral + polish)**
- 1 system design per day (30–45 min)
- Prepare “project deep dive” story:
    - what you built, tradeoffs, incident you handled, a hard bug you fixed
- Do 2 timed mock interviews (friend or self-recorded)

---

### 3) “Must-know” interview questions (with what interviewers look for)
#### Java
- What happens if you mutate a key used in a `HashMap`?
- Difference between `==` and `equals`, and why `hashCode` matters
- When to use `volatile` vs `synchronized`
- How `CompletableFuture` differs from using a thread pool directly
- What causes memory leaks in Java apps?

#### Spring
- How does Spring create proxies? When does `@Transactional` *not* work?
- Filter vs interceptor vs AOP advice (where each runs)
- What happens in a typical Spring MVC request?
- How do you handle validation and errors consistently?
- Common JPA performance issues and how to detect/fix them

#### Design
- Design a rate limiter / URL shortener / order processing workflow
- How would you make an endpoint idempotent?
- How do you handle retries without duplicating side effects?

---

### 4) Hands-on mini-project ideas (best ROI)
Pick **one** and implement it end-to-end (small but “real”):
1. **Task manager API**
    - endpoints + validation + pagination
    - JPA + Flyway/Liquibase migrations
    - integration tests with Testcontainers (Postgres)
2. **E-commerce checkout skeleton**
    - order creation (idempotency key)
    - async payment simulation via messaging (or outbox-like table)
3. **Auth + roles**
    - Spring Security + JWT
    - method-level security + integration tests

If you want, tell me which one you choose and I can give you a concrete backlog and code structure.

---

### 5) Recommended resources (reliable and interview-relevant)
- **Spring Guides** (hands-on): https://spring.io/guides
- **Spring Boot Reference Docs** (for specific features): https://docs.spring.io/spring-boot/
- **Baeldung** (good for targeted refreshers; validate understanding): https://www.baeldung.com/
- **Effective Java (Bloch)**: best for Java best practices and “why” answers.
- **Java Concurrency in Practice (Goetz)**: if concurrency is a big part of your interviews.
- **LeetCode / NeetCode**: for coding reps (focus on patterns).

---

### 6) What I need from you to tailor this (so it matches your interviews)
Reply with:
1) **Level**: junior / mid / senior
2) **Role focus**: API dev, microservices, data-heavy, platform, etc.
3) **Interview format**: LeetCode-heavy? take-home? system design?
4) **Tech stack**: Spring MVC vs WebFlux, JPA vs JDBC, Kafka, AWS, etc.
5) **Timeline**: how many days until the first interview

Then I’ll produce a **day-by-day prep schedule**, a **question bank**, and a **mock interview script** matched to your target role.