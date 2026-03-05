# Spring Proxies and Transactional Pitfalls

## How Spring Creates Proxies

Spring relies heavily on **AOP (Aspect-Oriented Programming)** to add behavior (like transaction management, caching, or security) to your beans without modifying your actual code. It achieves this using **Proxies**.

When the Spring Container starts up, if it creates a bean that has an advice attached to it (like `@Transactional`), it doesn't give you the actual instance of that class. Instead, it gives you a **Proxy** that wraps your bean.

There are two types of proxies Spring uses:

### 1. JDK Dynamic Proxy
*   **Mechanism:** Built into the Java language (`java.lang.reflect.Proxy`).
*   **Requirement:** The target bean must implement an **Interface**.
*   **How it works:** Spring creates a dynamic class at runtime that implements the same interface as your bean. Calls go to the proxy, which executes the advice (e.g., "Start Transaction"), and then delegates to your actual implementation.

### 2. CGLIB Proxy (Code Generation Library)
*   **Mechanism:** Uses bytecode generation.
*   **Requirement:** Used when the target bean **does not implement an interface** (or if `proxyTargetClass=true` is forced).
*   **How it works:** CGLIB creates a subclass of your target class at runtime and overrides its methods.
*   **Note:** In modern Spring Boot, CGLIB is often the default even if interfaces exist, to ensure predictability.

---

## When `@Transactional` Does Not Work

This is a classic interview question. Since `@Transactional` relies on proxies, it fails in specific scenarios where the proxy logic is bypassed or cannot be applied.

### 1. Self-Invocation (The "this" problem)
If a method within a class calls another `@Transactional` method **within the same class**, the transaction will not start.
*   **Why:** You are calling `this.method()`. `this` refers to the actual object instance, not the Spring Proxy. Therefore, the proxy code never triggers.
*   **Fix:** Self-inject the bean or move the method to a different service.

### 2. Private or Final Methods
*   **Private:** A proxy cannot expose/override a private method.
*   **Final:** CGLIB cannot extend a class or override a method if it is marked `final`.
*   **Result:** The code compiles and runs, but the transaction logic is silently ignored.

### 3. Not a Spring Bean
If you instantiate a class manually (e.g., `new Service()`) instead of letting Spring inject it, no proxy is created. Annotations strictly require the Spring Container to manage the lifecycle.

### 4. Checked Exceptions
By default, Spring only rolls back a transaction for **RuntimeExceptions** (unchecked) and **Errors**.
*   **Scenario:** If your method throws a `java.io.IOException` (checked), the transaction will commit by default!
*   **Fix:** Use `@Transactional(rollbackFor = Exception.class)`.

### 5. Swallowing Exceptions
If you wrap your transactional code in a `try-catch` block and do not re-throw the exception, the proxy sees a successful return and commits the transaction.
