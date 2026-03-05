# Network Security: CORS, CSRF, and Cookies

Browser-based security mechanisms are a frequent source of confusion in interviews because they involve complex interplay between headers, cookies, and browser policies. A Senior Backend Engineer must understand these deeply.

## 1. The Same-Origin Policy (SOP) — The Root Cause

The **Same-Origin Policy** is a browser security mechanism that prevents a script loaded from `https://evil.com` from reading data returned by `https://bank.com`. Two URLs share the same "origin" if and only if their **scheme, host, AND port** are identical.

| URL A | URL B | Same Origin? | Reason |
|---|---|---|---|
| `https://bank.com/a` | `https://bank.com/b` | ✅ Yes | Same scheme, host, port |
| `https://bank.com` | `http://bank.com` | ❌ No | Different scheme |
| `https://bank.com` | `https://sub.bank.com` | ❌ No | Different host |
| `https://bank.com:443` | `https://bank.com:8080` | ❌ No | Different port |

The SOP restricts JavaScript's `fetch()` or `XMLHttpRequest` from reading the response of cross-origin requests. It does **not** prevent the browser from *sending* the request — only from *reading* the response. This distinction matters for CSRF (see below).

---

## 2. CORS: Cross-Origin Resource Sharing

CORS is the controlled mechanism by which a server can opt-in to letting specific cross-origin clients read its responses, overriding the SOP.

### Simple Requests
A request is "simple" if it uses `GET`, `HEAD`, or `POST` with only basic headers (no custom headers, no `Content-Type: application/json`). The browser sends the request with an `Origin` header. If the server's response includes `Access-Control-Allow-Origin: *` (or the specific origin), the browser allows the script to read it.

```
GET /api/data HTTP/1.1
Origin: https://frontend.com

HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://frontend.com
```

### Preflight Requests (The Real Mechanism)
For any request using `PUT`, `DELETE`, `PATCH`, `Content-Type: application/json`, or any custom header (like `Authorization`), the browser **automatically** sends a preflight `OPTIONS` request *before* the real request. This is the key interview concept.

```
# Browser sends automatically (you never write this):
OPTIONS /api/users HTTP/1.1
Origin: https://frontend.com
Access-Control-Request-Method: DELETE
Access-Control-Request-Headers: Authorization

# Server must respond with:
HTTP/1.1 204 No Content
Access-Control-Allow-Origin: https://frontend.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Authorization, Content-Type
Access-Control-Max-Age: 86400   ← Cache preflight result for 24 hours

# Then, the real DELETE request is sent.
```

*   **`Access-Control-Max-Age`**: Preflights are expensive (an extra round-trip per cross-origin request). This header tells the browser to cache the preflight result, so it doesn't repeat the `OPTIONS` request for the same endpoint for the specified duration.
*   **`Access-Control-Allow-Credentials: true`**: Required if the request includes cookies. You **cannot** use `Access-Control-Allow-Origin: *` alongside `Allow-Credentials: true` — the origin must be explicitly specified.

**Key Insight:** CORS is enforced entirely by the **browser**. If you use `curl` directly, there is no CORS check. CORS protects users from malicious scripts, not servers from malicious clients.

---

## 3. Cookies and Security Flags

Cookies are a fundamental session management mechanism, but their security properties depend entirely on how they are configured.

### `HttpOnly`
*   **What it does:** The cookie cannot be read by JavaScript (`document.cookie` returns nothing for `HttpOnly` cookies).
*   **Why it matters:** Prevents **XSS (Cross-Site Scripting)** attacks from stealing session cookies. Even if an attacker injects malicious `<script>` tags into your page, they cannot access `HttpOnly` cookies to hijack sessions.
*   **Rule:** All session cookies should be `HttpOnly`.

### `Secure`
*   **What it does:** The browser will only transmit this cookie over HTTPS connections. It is never sent over unencrypted HTTP.
*   **Why it matters:** Prevents session hijacking via man-in-the-middle attacks on unencrypted Wi-Fi networks.
*   **Rule:** All session cookies should be `Secure`.

### `SameSite` ⭐ *Most Important for CSRF*
Controls when the browser includes a cookie in cross-origin requests. This is the key defense against CSRF.

| `SameSite` value | Cookie sent on cross-origin GET? | Cookie sent on cross-origin POST? | Best For |
|---|---|---|---|
| `Strict` | ❌ No | ❌ No | Maximum security. Cookie never sent from another site. May break OAuth flows where the site is redirected to from an external IdP. |
| `Lax` | ✅ Yes (top-level nav) | ❌ No | Balanced. Sent on link clicks from external sites (so you stay logged in), but not on `<img>`, `<iframe>`, or AJAX POST. **The modern default in Chrome.** |
| `None` | ✅ Yes | ✅ Yes | Legacy/third-party cookie use. **Must be combined with `Secure`.** |

```
Set-Cookie: session_id=abc123; HttpOnly; Secure; SameSite=Lax; Path=/
```

---

## 4. CSRF: Cross-Site Request Forgery

### The Attack
1.  Alice logs into `bank.com`. Her browser stores a session cookie.
2.  Alice visits `evil.com` (a malicious site).
3.  `evil.com` has a hidden `<form>` that auto-submits a `POST /transfer-money` request to `bank.com`.
4.  Alice's browser, following its cookie rules, attaches her `bank.com` session cookie to this request.
5.  `bank.com` receives a fully authenticated request to transfer money — that Alice never intended.

### Why this works: The browser *sends* the cookie on cross-origin form submissions by default (pre-`SameSite`). The SOP only prevents reading the response, not sending the request.

### Mitigations

**1. `SameSite=Lax` or `SameSite=Strict` cookies (Modern, Preferred)**
Since Chrome made `SameSite=Lax` the default for cookies without an explicit attribute, the CSRF attack above is automatically blocked — the browser will not send the cookie on the cross-origin `POST` from `evil.com`.

**2. CSRF Token (Classic, Still Required for Pre-SameSite Compatibility)**
*   The server generates an unpredictable, per-session random token and embeds it in every HTML form as a hidden field.
*   On `POST`, the server validates that the submitted token matches the session's stored token.
*   `evil.com` cannot read the CSRF token (blocked by SOP for JSON APIs and by the browser's cross-origin read restriction for HTML pages), so it cannot forge a valid request.

**3. `Origin` / `Referer` Header Validation**
*   The server checks the `Origin` or `Referer` header of the incoming `POST` request.
*   If `Origin: evil.com` doesn't match `bank.com`, reject it.
*   Simple, but can fail when `Referer` is stripped by privacy settings or HTTPS→HTTP transitions.

**Defense in Depth Rule:** Use `SameSite=Lax` cookies + CSRF tokens for state-changing operations. Don't rely on a single mechanism.

---

## 5. Key Interview Questions

**Q: "Why doesn't CORS protect against CSRF?"**
A: Because CORS is enforced by the *browser* only on reading the response. The cross-origin `POST` request is *sent* and *processed* by the server before the browser even checks the `Access-Control-Allow-Origin` header. The damage is already done.

**Q: "What's the difference between `HttpOnly` and `SameSite`?"**
A: `HttpOnly` protects against XSS (JavaScript cannot steal the cookie). `SameSite` protects against CSRF (the browser won't send the cookie on cross-origin requests). They address different attack vectors and should both be set.

**Q: "What does `SameSite=Lax` actually allow?"**
A: It allows the cookie to be sent when the user navigates via top-level navigation (clicking a link from `google.com` to `bank.com`) but blocks it on sub-resource requests (`<img>`, `<script>`) and cross-origin `POST`/`PUT`/`DELETE` AJAX or form submissions.
