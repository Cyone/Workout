# Ultimate Java Backend Interview Preparation Guide

This guide compiles essential architectural concepts, design principles, and technical "gotchas" for Senior Java/Spring developers.

---

## Part 1: Architecture & Design Patterns

### 1. Clean Architecture (The "Onion")
*Based on Robert C. Martin's principles.*

**The Golden Rule (Dependency Rule):**
Source code dependencies must point only **inward**, toward higher-level policies. The inner circles (Business Logic) must know *nothing* about the outer circles (Database, Web, UI).

**The Layers (Center to Edge):**
1.  **Entities (Yellow):** Enterprise Business Rules. Least likely to change.
2.  **Use Cases (Red):** Application Business Rules. Orchestrates data flow.
3.  **Interface Adapters (Green):** Controllers, Presenters, Gateways. Converts data formats.
4.  **Frameworks & Drivers (Blue):** Database, Web, UI, Devices. The "Details."

**Key Interview Concepts:**
*   **"Details" vs. "Policy":** The Database is a detail. The Web is a detail. We design around business behavior, not schema.
*   **Screaming Architecture:** Project structure should scream intent (`ProcessPayroll`, `OnboardEmployee`), not framework (`models`, `views`, `controllers`).
*   **Boundaries & Plugins:** Use polymorphism (Interfaces) to cross boundaries. The Business Logic defines a `UserRepository` interface; the Database layer implements it. This makes the DB a "plugin."
*   **Humble Object Pattern:** Split hard-to-test components (like UI Views) into two:
    *   **The Logic (Presenter):** Testable.
    *   **The Humble Object (View):** Hard to test but so simple it typically doesn't break.

### 2. SOLID Principles
*Clean Architecture applied at the class level.*

| Acronym | Principle | The "Real" Meaning for Interviews |
| :--- | :--- | :--- |
| **S** | **Single Responsibility** | Separate code based on **who** (the Actor) requests the change. Don't mix CFO logic with COO logic. |
| **O** | **Open/Closed** | Open for extension, closed for modification. Add new features by adding new code (plugins/classes), not by changing old code. |
| **L** | **Liskov Substitution** | Subtypes must be substitutable for base types. If a child class breaks the parent's contract (e.g., Square vs. Rectangle), it violates LSP. |
| **I** | **Interface Segregation** | Avoid "Fat Interfaces." Clients should not be forced to implement methods they don't use. |
| **D** | **Dependency Inversion** | High-level modules should not depend on low-level modules. Both depend on abstractions. (Inverts the flow of control). |

---

## Part 2: The Acronym Dictionary

### General Engineering
*   **DRY (Don't Repeat Yourself):** Single source of truth for logic.
*   **KISS (Keep It Simple, Stupid):** Avoid over-engineering.
*   **YAGNI (You Ain't Gonna Need It):** Don't build for hypothetical futures.
*   **CAP (Consistency, Availability, Partition Tolerance):** Distributed systems trade-off (Choose 2).
*   **ACID (Atomicity, Consistency, Isolation, Durability):** Transaction guarantees.
*   **STAR (Situation, Task, Action, Result):** The format for behavioral interview answers.

### Java Ecosystem
*   **JDK:** Compiler + Tools + JRE.
*   **JRE:** JVM + Libraries (Runner).
*   **JVM:** The engine that runs bytecode.
*   **JIT (Just-In-Time):** Compiles bytecode to native machine code at runtime for speed.
*   **POJO:** Plain Old Java Object (No framework dependencies).
*   **DTO:** Data Transfer Object (Moves data between layers).
*   **DAO/Repository:** Interface for database access.
*   **JPA:** The Specification (Interface).
*   **Hibernate:** The Implementation.

### Spring Framework
*   **IoC (Inversion of Control):** Framework creates objects ("Don't call us, we'll call you").
*   **DI (Dependency Injection):** The pattern used to implement IoC.
*   **AOP (Aspect-Oriented Programming):** Separates cross-cutting concerns (Logging, Transactions).

---

## Part 3: Java Technical "Gotchas" (The Gauntlet)

### Core Language & Internals
1.  **Catch Block:** If a `finally` block throws an exception, the original exception from the `try` block is suppressed/lost.
2.  **String Pool:** `String s = "A"` (Pool) vs `new String("A")` (Heap). `==` compares references, not content.
3.  **Overloading:** `foo(null)` calls the most specific overload (`String` over `Object`).
4.  **Final Collection:** `final List` prevents reassigning the variable, but you can still add/remove items from the list.
5.  **Floating Point:** `0.1 + 0.2 != 0.3` (IEEE 754 precision). Use `BigDecimal` for money.
6.  **Map Mutable Keys:** If you modify a key's hash *after* insertion, the Map cannot find it again.
7.  **Thread Start:** `t.run()` executes on the *current* thread. `t.start()` spawns a *new* thread.
8.  **Integer Cache:** `Integer` objects between -128 and 127 are cached. `==` works for them, but fails for values like 200.
9.  **Generics Erasure:** `List<String>` and `List<Integer>` both become `List<Object>` at runtime.
10. **Optional:** `orElse()` is eager (always creates object). `orElseGet()` is lazy (creates only if needed).
11. **Volatile:** Guarantees visibility (memory barrier) but NOT atomicity. `count++` is not thread-safe with just volatile.
12. **Interface Fields:** Are implicitly `public static final` (Constants).

---

## Part 4: Spring Framework "Gotchas"

### Lifecycle & Magic
1.  **Bean Scope:** Default is **Singleton** (one per app). Storing state in beans is dangerous.
2.  **Transaction Rollback:** By default, `@Transactional` only rolls back on `RuntimeException` (Unchecked), not Checked Exceptions.
3.  **Self-Invocation:** Calling a `@Transactional` method from *within the same class* bypasses the Proxy, so the transaction doesn't start.
4.  **Private Injection:** Spring uses Reflection to inject into `private` fields.
5.  **Circular Dependency:** A needs B, B needs A. Fails with Constructor Injection.
6.  **Filter vs. Interceptor:**
    *   **Filter:** Servlet level (Tomcat). Runs *before* Spring.
    *   **Interceptor:** Spring level. Runs *inside* the Context.
7.  **@SpringBootApplication:** Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.
8.  **Prototype Injection:** Injecting a Prototype bean into a Singleton bean happens only *once*. The Singleton holds the same "prototype" instance forever.
9.  **LazyInitializationException:** Occurs when accessing a Lazy collection (e.g., `@OneToMany`) after the Transaction/Session has closed.
10. **@PostConstruct:** Used because Constructors run *before* dependencies are injected.
11. **@MockBean vs @Mock:**
    *   `@Mock`: For fast Unit Tests (Mockito).
    *   `@MockBean`: Replaces the bean in the Spring Context (Integration Tests).
12. **Actuator Safety:** `spring-boot-starter-actuator` exposes sensitive endpoints (`/heapdump`, `/env`). Must be secured.
