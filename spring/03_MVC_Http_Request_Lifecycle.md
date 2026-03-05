# The Spring MVC Request Lifecycle

When a typical HTTP request hits a Spring Boot application, it follows a specific sequence orchestrated by the `DispatcherServlet` (the "Front Controller").

### 1. Request hits the DispatcherServlet
The `DispatcherServlet` is the central coordinator. It receives all requests mapped to it (usually `/`).

### 2. Handler Mapping
The dispatcher asks the **HandlerMapping**: *"Who handles this URL?"*
*   The mapping scans `@RequestMapping`, `@GetMapping`, etc.
*   It returns an execution chain (the Controller method + any Interceptors).

### 3. Handler Adapter
The dispatcher cannot invoke your controller method directly because controllers can be written in many ways. It uses a **HandlerAdapter** to invoke the method.
*   This step involves **Data Binding**: converting JSON to Java Objects (`@RequestBody`), mapping query params (`@RequestParam`), and running Validations (`@Valid`).

### 4. Controller Execution
Your actual code runs.
*   You call Services, Repositories, etc.
*   You return a result. This is usually a `ResponseEntity` (for REST) or a view name (for MVC).

### 5. View Resolution (Classic MVC only)
*   If you are building a REST API (`@RestController`), this step is skipped (see step 6).
*   If you return a String like `"home"`, the **ViewResolver** finds the actual template (e.g., `home.html` or `home.jsp`).

### 6. Serialization / Rendering
*   **REST (`@ResponseBody`):** The `HandlerAdapter` uses `HttpMessageConverters` (usually **Jackson**) to serialize your Java Object into JSON/XML and writes it to the response stream.
*   **MVC:** The View Engine renders the HTML.

### 7. Response
The `DispatcherServlet` returns the response to the user (passing back through the Interceptors and Filters).

---

### Visual Cheat Sheet

**Request** -> `Filter` -> `DispatcherServlet` -> `HandlerMapping` -> `HandlerAdapter` -> **`Controller`** -> `HandlerAdapter` (Serialization) -> `DispatcherServlet` -> `Filter` -> **Response**
