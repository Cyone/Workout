# REST API Design Best Practices

A Senior Backend Engineer must be able to design REST APIs that are intuitive, evolvable, and production-grade. This file covers the key principles and patterns that separate a good API design from a great one.

## 1. Resource Naming & URL Design

### Nouns, Not Verbs
The URL identifies a *resource* (a noun), and the HTTP method specifies the *action* (a verb).
*   ✅ `GET /orders/123` — Correct
*   ❌ `GET /getOrder?id=123` — Wrong: "get" is the HTTP verb, not the resource name.

### Plural Resource Names
Use plural nouns for collections.
*   ✅ `GET /users` (collection), `GET /users/42` (individual item)
*   ❌ `GET /user` — Confusing: is this a singleton or a collection?

### Hierarchical Relationships
Use URL nesting to express ownership or containment. Keep nesting to a maximum of 2 levels to avoid deeply coupled, brittle URLs.
*   ✅ `GET /users/42/orders` — Orders belonging to user 42.
*   ✅ `GET /orders/123/line-items` — Line items within an order.
*   ❌ `GET /users/42/orders/123/line-items/5/discounts` — Too deep. Break this into separate resources.

### Use `kebab-case`, Not `snake_case` or `camelCase`
URLs are case-insensitive and transmitted over the wire. `kebab-case` is the universal convention for multi-word resource names.
*   ✅ `/payment-methods`
*   ❌ `/paymentMethods` or `/payment_methods`

---

## 2. API Versioning Strategies

APIs evolve over time. Introducing breaking changes without versioning destroys clients.

### Option A: URI Versioning (Most Common)
*   **How:** Embed the version in the URL path.
*   **Example:** `GET https://api.example.com/v1/users`
*   **Pros:** Simple, highly visible, trivially cacheable (no special routing logic needed), easy to test with a browser or `curl`.
*   **Cons:** Violates the "resource purity" principle (the URL should identify a resource, not a version of an API representation). Results in URL duplication when maintaining multiple versions.
*   **Best for:** Most public APIs. Used by Stripe, GitHub, Twitter.

### Option B: Header Versioning
*   **How:** The version is passed via a custom `Accept` header (Content Negotiation).
*   **Example:** `GET /users` with `Accept: application/vnd.mycompany.v2+json`
*   **Pros:** Keeps URLs clean and stable. Semantically correct: you're requesting a specific *representation* of a resource.
*   **Cons:** Less discoverable. Not bookmarkable. Harder to test without tooling.
*   **Best for:** Internal APIs where clients are tightly controlled.

### Option C: Query Parameter Versioning
*   **Example:** `GET /users?version=2`
*   **Cons:** Easy to miss, often ignored by load balancers and caches, generally discouraged. Avoid in production APIs.

---

## 3. Pagination: Offset vs. Cursor-Based

When a collection contains thousands of items, you must paginate. Two main strategies exist.

### Offset Pagination
*   **How:** `GET /articles?limit=20&offset=40` (skip the first 40, return the next 20).
*   **Pros:** Simple to implement with SQL (`LIMIT 20 OFFSET 40`). Random access: can jump to any page.
*   **Cons:**
    *   **The Phantom Row / Data Skew Problem:** If items are inserted between pages, items can be duplicated or skipped. Page 3 starts at offset 40, but if 2 new items were added to the collection after you fetched page 2, the items that were at positions 40-41 have shifted to 42-43, and you'll miss them or re-see old ones.
    *   **Performance:** `OFFSET` in SQL causes a full sequential scan up to the offset position, making pages towards the end extremely slow for large datasets.
*   **Best for:** Small datasets (<10k rows), administrative UIs where jumping to a specific page is required.

### Cursor-Based Pagination
*   **How:** The server returns a `next_cursor` (an opaque token, typically a Base64-encoded last-item ID or timestamp). The client passes it back to get the next page.
    ```json
    GET /articles?limit=20
    Response: { "data": [...], "next_cursor": "eyJpZCI6IDEwMH0=" }

    GET /articles?limit=20&cursor=eyJpZCI6IDEwMH0=
    ```
*   **Pros:** Stable: insertions between pages don't cause duplicates or skips. Efficient: the DB can use an index (e.g., `WHERE id > 100 LIMIT 20`), regardless of how deep into the dataset you are.
*   **Cons:** No random access (cannot jump to page 10). The cursor is opaque; clients must not try to interpret it.
*   **Best for:** Social media feeds, real-time data streams, any dataset with frequent insertions.

---

## 4. Idempotency Keys for Safe POST Retries

`POST` is non-idempotent: if a client sends a payment request and the network times out, retrying the `POST` would charge the customer twice.

**Solution:** The client generates a unique `Idempotency-Key` (a UUID) and includes it with the request. The server stores the key and the resulting response. On a retry with the same key, the server returns the *cached* original response instead of re-executing the operation.

```
POST /payments
Idempotency-Key: a6f5b7e4-c1d2-4e3a-b8f9-2a0c3d4e5f6b

{ "amount": 9999, "currency": "USD" }
```

*   **TTL:** Idempotency keys are typically stored for 24 hours or 7 days (configurable).
*   **Implementation:** Store `{key → (status, response_body)}` in Redis or a fast database.
*   **Used by:** Stripe, PayPal, and every major payments API in production.

---

## 5. Backward Compatibility & Breaking Changes

A **breaking change** forces consumers to update their code. Avoid them in versioned APIs. For internal APIs, treat breaking changes with the same care as public contracts.

### Non-Breaking Changes (Safe to deploy anytime)
*   Adding a **new optional field** to a response body.
*   Adding a **new endpoint** (`/v1/reports`).
*   Adding a **new optional query parameter**.
*   Changing the order of fields in a JSON response (clients shouldn't rely on order).

### Breaking Changes (Require a new API version)
*   **Removing a field** from a response.
*   **Renaming a field** (`userId` → `user_id`).
*   **Changing a field's type** (`string` → `integer`).
*   **Changing HTTP status code** semantics (e.g., a 404 is now a 200 with `data: null`).
*   **Requiring a new mandatory field** in the request body.

### Postel's Law (Robustness Principle)
*"Be conservative in what you send, liberal in what you accept."*
Your API server should tolerate unexpected extra fields in the request body (ignore them gracefully) rather than returning a 400 error. Your clients should ignore unexpected extra fields in a response body rather than crashing.

---

## 6. Key HTTP Patterns for API Design

*   **`GET /resources/{id}`** → Fetch a specific resource. Returns `200 OK` or `404 Not Found`.
*   **`POST /resources`** → Create. Returns `201 Created` with a `Location: /resources/new-id` header pointing to the new resource.
*   **`PUT /resources/{id}`** → Full replacement (idempotent). Returns `200 OK` or `204 No Content`.
*   **`PATCH /resources/{id}`** → Partial update. Use JSON Patch (RFC 6902) or JSON Merge Patch for semantics. Returns `200 OK`.
*   **`DELETE /resources/{id}`** → Delete. Returns `204 No Content` on success. Returns `204` (not `404`) on subsequent calls — it's idempotent; the desired end state (resource doesn't exist) has been achieved.
