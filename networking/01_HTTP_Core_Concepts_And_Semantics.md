# HTTP Core Concepts and Semantics

To excel in senior backend interviews, you must demonstrate a rigorous understanding of the HTTP protocol beyond basic REST verbs. This document explores the foundational mechanics of HTTP.

## 1. The Request/Response Lifecycle
At its core, HTTP is a stateless, application-layer, client-server protocol.
*   **Request Anatomy:**
    *   **Start Line:** `[Method] [Request-URI] [HTTP-Version]` (e.g., `GET /api/v1/users HTTP/1.1`)
    *   **Headers:** Key-value pairs providing metadata (e.g., `Host`, `User-Agent`, `Accept`).
    *   **Empty Line:** Signals the end of headers.
    *   **Body:** Optional payload (required for POST/PUT, generally omitted for GET/DELETE).
*   **Response Anatomy:**
    *   **Status Line:** `[HTTP-Version] [Status-Code] [Reason-Phrase]` (e.g., `HTTP/1.1 200 OK`)
    *   **Headers:** Server metadata (e.g., `Content-Type`, `Content-Length`, `Set-Cookie`).
    *   **Empty Line:** Separator.
    *   **Body:** The requested resource or data.

## 2. HTTP Methods: Idempotency vs. Safety
Understanding the mathematical definitions of these terms is crucial for designing resilient distributed systems.

### Safe Methods
A method is **safe** if it does not modify the state of the resource on the server. Safe methods are essentially "read-only."
*   `GET`, `HEAD`, `OPTIONS`, `TRACE`
*   *Note:* Safe methods *can* have side effects (like logging the request or updating an analytics counter), but the core representation of the resource remains unchanged.

### Idempotent Methods
A method is **idempotent** if applying it multiple times yields the same state on the server as applying it once. This is critical for retry logic (e.g., if a network timeout occurs, can the client safely retry the request?).
*   `GET`, `HEAD`, `OPTIONS`, `TRACE` (All safe methods are inherently idempotent).
*   `PUT`: Replacing a resource with a specific state. `PUT /users/1 {name: "Alice"}`. Sending this 10 times results in the same final state.
*   `DELETE`: `DELETE /users/1`. The first call deletes the user (returns 200 or 204). The second call might return 404, but the *state of the server* (User 1 does not exist) is exactly the same. Therefore, `DELETE` is idempotent.

### Non-Idempotent Methods
*   `POST`: Creates a new resource. Executing `POST /users` 10 times will create 10 distinct users.
*   `PATCH`: Applies a partial modification. While *some* `PATCH` operations are idempotent (e.g., `PATCH {status: "ACTIVE"}`), others are not (e.g., `PATCH {increment_views: 1}`). Because it's not *guaranteed* to be idempotent, the specification classifies it as non-idempotent.

## 3. Crucial HTTP Headers
*   **Content Negotiation (`Accept` & `Content-Type`):**
    *   `Accept`: Sent by the client to indicate what media types it can process (e.g., `Accept: application/json`).
    *   `Content-Type`: Sent by the sender (client in a POST, server in a Response) to indicate the media type of the payload body.
*   **Caching Headers:**
    *   `Cache-Control`: The modern standard for caching directives (`no-store`, `max-age=3600`, `private`, `public`).
    *   `ETag`: An opaque identifier (usually a hash) representing a specific version of a resource. The client sends `If-None-Match: "hash123"`. If the resource hasn't changed, the server returns `304 Not Modified` with an empty body, saving bandwidth.
*   **Host:** Required since HTTP/1.1. Allows a single IP address (and single web server) to host multiple distinct domain names (Virtual Hosting).

## 4. Status Codes Deep Dive
*   **2xx (Success):** `200 OK`, `201 Created` (Must include a `Location` header pointing to the new resource), `204 No Content` (Often used for DELETE or successful OPTIONS requests).
*   **3xx (Redirection):** `301 Moved Permanently` (Browsers cache this aggressively), `302 Found` (Temporary redirect), `304 Not Modified` (Cache validation).
*   **4xx (Client Error):** `400 Bad Request` (Malformed syntax/validation failure), `401 Unauthorized` (Missing or invalid authentication), `403 Forbidden` (Authenticated, but lacks permissions), `404 Not Found`, `409 Conflict` (Optimistic locking failure, duplicate constraint).
*   **5xx (Server Error):** `500 Internal Server Error` (Unhandled exception), `502 Bad Gateway` (Proxy/Load Balancer received an invalid response from the upstream server), `503 Service Unavailable` (Overload or maintenance), `504 Gateway Timeout` (Upstream server took too long).
