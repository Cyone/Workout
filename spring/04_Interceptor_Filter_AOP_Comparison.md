# Filter vs. Interceptor vs. AOP Advice

While all three concepts allow you to "intercept" logic, they operate at different layers of the application stack and have access to different context objects.

## 1. Servlet Filter (`javax.servlet.Filter` / `jakarta.servlet.Filter`)
*   **Layer:** The outermost layer. It is part of the Servlet Container (like Tomcat), not specifically Spring MVC (though Spring runs inside it).
*   **Execution Point:** Runs **before** the request reaches the `DispatcherServlet`.
*   **Scope:** Has access to the raw `ServletRequest` and `ServletResponse`. It knows nothing about Spring Controllers or Contexts unless explicitly wired.
*   **Use Cases:**
    *   Security (Spring Security is essentially a chain of filters).
    *   Request/Response encoding/compression.
    *   CORS handling.
    *   Logging raw request IP addresses.

## 2. Spring Handler Interceptor (`HandlerInterceptor`)
*   **Layer:** Inside the Spring MVC framework.
*   **Execution Point:** Runs inside the `DispatcherServlet`, **after** the handler mapping determines which controller to use, but **before** the controller executes.
*   **Scope:** Has access to the `HttpServletRequest`, `HttpServletResponse`, and the **handler** (the specific Controller method being called).
*   **Use Cases:**
    *   Authorization checks (e.g., checking if a user has a specific role for a specific handler).
    *   Adding common variables to the Model/View.
    *   Measuring execution time of a specific controller endpoint.

## 3. AOP Advice (Aspect Oriented Programming)
*   **Layer:** The Business/Service layer (mostly).
*   **Execution Point:** Runs around method executions on Spring Beans.
*   **Scope:** Has access to the **Method arguments** and **Return values**. It typically does not have direct access to the web layer (Request/Response) unless you thread-local it (which is bad practice).
*   **Use Cases:**
    *   Transaction Management (`@Transactional`).
    *   Logging (e.g., "Method X called with args Y").
    *   Exception translation.
    *   Caching (`@Cacheable`).

## Summary Flow
When an HTTP request comes in:

1.  **Filter Chain** (Security, Encoding)
    2.  **DispatcherServlet** (Spring Entry point)
        3.  **HandlerInterceptor** (`preHandle`)
            4.  **AOP Advice** (Around/Before Service methods)
                5.  **Controller / Service Logic**
            6.  **AOP Advice** (After returning)
        7.  **HandlerInterceptor** (`postHandle`)
    8.  **DispatcherServlet** (Render view)
9.  **Filter Chain** (Response goes back)
