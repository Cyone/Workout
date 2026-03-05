# Spring Boot 3 & Jakarta EE Migration

Migrating an enterprise application from Spring Boot 2.x to 3+ is heavily focused on the shift from Java EE to Jakarta EE. This was a massive ecosystem disruption.

## 1. The Core Change: `javax.*` to `jakarta.*`
When Oracle transferred Java EE to the Eclipse Foundation, they did not transfer the copyright to the `javax` namespace. Consequently, all enterprise Java specifications were renamed to Jakarta EE, requiring a package namespace change to `jakarta.*`.

Spring Boot 3 embraced Jakarta EE 10, meaning it completely dropped support for the old `javax.*` packages.

### Major APIs Impacted
If your codebase used any of the following, a find-and-replace was mandatory:
*   **JPA (Hibernate):** `javax.persistence.Entity` -> `jakarta.persistence.Entity`
*   **Validation:** `javax.validation.constraints.NotNull` -> `jakarta.validation.constraints.NotNull`
*   **Servlets:** `javax.servlet.http.HttpServletRequest` -> `jakarta.servlet.http.HttpServletRequest`
*   **JMS, JTA, WebSocket, Mail, etc.**

### The Transitive Dependency Nightmare
You cannot simply change your imports and compile. If your Spring Boot 3 app uses a third-party library that relies on `javax.servlet`, it will crash at runtime (Class Not Found exceptions) because the Spring container only provides `jakarta.servlet`.
*   **The Fix:** You must audit and upgrade *every single dependency* in your `pom.xml` or `build.gradle` to a version that supports Jakarta EE.

## 2. Hibernate 6 Changes
Spring Boot 3 updates the default JPA provider from Hibernate 5 to Hibernate 6. This introduces breaking changes beyond the package rename.

*   **Dialect Resolution:** You no longer need to specify `spring.jpa.properties.hibernate.dialect`. Hibernate 6 automatically determines the optimal dialect based on the JDBC driver metadata.
*   **Sequence Generators:** Hibernate 6 changed how it generates IDs for `@GeneratedValue(strategy = GenerationType.SEQUENCE)`. It is more compliant with the JPA spec but may cause sequences to jump or behave differently than in Hibernate 5, potentially requiring migration scripts for production databases to align sequence states.
*   **Query Parsing:** Hibernate 6 rewrote its SQM (Semantic Query Model) parser. Some complex or custom HQL queries that worked via loopholes in Hibernate 5 might fail validation in Hibernate 6.

## 3. Configuration Properties Changes
Spring Boot 3 removed or renamed many application properties.
*   **Trailing Slashes:** By default, Spring MVC no longer matches trailing slashes. `@GetMapping("/users")` will return 404 for a request to `GET /users/`.
*   **`spring.factories` Deprecation:** The mechanism for auto-configuration changed. Custom starters must now use `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` instead of `spring.factories`.

## 4. Migration Strategies
*   **OpenRewrite:** The industry standard for this migration. OpenRewrite provides automated refactoring recipes that scan your codebase, update imports, modify pom.xml dependency versions, and fix deprecated API usages automatically.
*   **The "Big Bang" vs. Incremental:** Because dependencies are deeply intertwined, a staggered migration is difficult. The standard approach is a branch-based "Big Bang" refactor, utilizing OpenRewrite, followed by rigorous integration testing.
