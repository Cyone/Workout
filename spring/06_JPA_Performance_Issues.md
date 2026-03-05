# Common JPA Performance Issues & Solutions

Hibernate (the default JPA implementation in Spring) makes database access easy, but it makes performance pitfalls easy to ignore until production.

## 1. The N+1 Select Problem
**The Issue:** You fetch a list of Parent entities (1 query). You then loop over them to access a Child entity. If the fetching is Lazy, Hibernate executes 1 extra query *per parent* to get the child.
*   *Result:* Fetching 100 records results in 101 queries.

**The Fix:**
*   **JPQL with Join Fetch:** `SELECT p FROM Parent p JOIN FETCH p.children`
*   **Entity Graphs:** Use `@EntityGraph` on your repository method to specify which paths to load eagerly for that specific query.

## 2. Cartesian Product (Cross Join)
**The Issue:** You try to eager fetch two different collections (Lists/Bags) in the same query.
*   *Result:* If a Parent has 50 children and 50 tags, the DB returns 2,500 rows (50 * 50). This explodes memory usage.

**The Fix:**
*   Use `Set` instead of `List` for collections (Hibernate handles Sets better in memory deduplication).
*   Fetch distinct collections in **separate queries** (Spring Data will stitch them together).

## 3. LazyInitializationException
**The Issue:** You try to access a `@OneToMany` lazy collection in your View layer or Controller, but the Transaction has already closed.
*   *Result:* The application crashes.

**The Fix:**
*   **Don't:** Do not enable `open-in-view` (OSIV) just to fix this; it causes connection pool exhaustion.
*   **Do:** Fetch what you need in the Service layer (using `JOIN FETCH` or DTO projections) before returning.

## 4. Fetching Entire Entities for Read-Only Views
**The Issue:** fetching the entire `User` entity (with all columns and relationships) just to display a dropdown of "ID" and "Name".

**The Fix:**
*   **Projections:** Define an Interface or Record.
    ```java
    // Spring Data automatically selects only these columns
    public interface UserNameOnly {
        Long getId();
        String getName();
    }
    ```

## How to Detect These Issues?

1.  **Enable SQL Logging:**
    Add to `application.properties`:
    ```properties
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    logging.level.org.hibernate.SQL=DEBUG
    # To see the parameters passed
    logging.level.org.hibernate.orm.jdbc.bind=TRACE
    ```

2.  **Use specific tools:**
    *   **Hypersistence Optimizer:** A library that scans your mappings and configs for issues.
    *   **P6Spy:** A JDBC proxy that logs exactly what SQL (with params) is executed and how long it took.
    *   **Datasource Proxy:** Allows you to assert query counts in your integration tests (e.g., "Assert that this method runs exactly 1 query").
```
