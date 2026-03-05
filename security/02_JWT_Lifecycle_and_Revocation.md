# JWT Lifecycle, Revocation, and Storage Security

The biggest challenge with stateless JWTs is managing their lifecycle. Once a token is issued and signed, it is valid until its expiration time (`exp` claim). If an attacker steals it, they have unhindered access.

## 1. Access Tokens vs. Refresh Tokens
To mitigate the risk of stolen tokens, enterprise systems use a two-token system.

### Access Token
*   **Purpose:** Included in every API request to authorize access to resources.
*   **Lifespan:** Very short (e.g., 5 to 15 minutes).
*   **Storage (Web):** Usually kept in memory (e.g., a JavaScript variable or React state) so it is wiped out if the user closes the tab, minimizing exposure to XSS.
*   **Risk Mitigation:** If stolen, the attacker only has a brief window before the token naturally expires.

### Refresh Token
*   **Purpose:** Used *only* to obtain a new Access Token when the old one expires.
*   **Lifespan:** Long (e.g., 7 days, 30 days, or rolling).
*   **Nature:** Usually a random, opaque string stored securely in a database (stateful), rather than a JWT.
*   **Storage (Web):** Must be stored in an `HttpOnly`, `Secure` cookie.

## 2. Storage Vulnerabilities: XSS vs. CSRF

Where you store tokens on a frontend client dictates the attack vectors you must defend against.

### Option A: LocalStorage / SessionStorage
*   **The Threat: Cross-Site Scripting (XSS).** If an attacker injects malicious JavaScript into your application (e.g., via a comment section), that script can execute `localStorage.getItem('token')` and send the token to their server.
*   **Verdict:** Never store sensitive tokens (especially long-lived Refresh Tokens) in LocalStorage.

### Option B: HttpOnly Cookies
*   **The Mechanism:** The server sends a `Set-Cookie: refreshToken=...; HttpOnly; Secure; SameSite=Strict` header.
*   **XSS Defense:** The `HttpOnly` flag physically prevents JavaScript from reading the cookie. XSS attacks cannot steal the token.
*   **The Threat: Cross-Site Request Forgery (CSRF).** If the user is logged into your bank, and visits `attacker.com`, the attacker can trigger a hidden request to `yourbank.com/transfer`. The browser will *automatically* attach the cookies, and the bank will process the transfer.
*   **CSRF Defense:**
    *   `SameSite=Strict` or `Lax`: Instructs the browser not to send the cookie if the request originates from a different domain.
    *   **Anti-CSRF Tokens:** Requiring a custom header (e.g., `X-CSRF-Token` or relying on the `Authorization: Bearer` header for the access token) defeats CSRF because the attacker's script cannot read the cookie to copy it into a header, nor can it force the browser to send custom headers cross-origin without CORS preflight failures.

## 3. The Revocation Problem (Blacklisting)
If a user clicks "Log Out Everywhere" or an admin suspends a user, their Access Token is still cryptographically valid for its remaining lifespan. How do we stop it?

### Strategy 1: The Redis Blocklist (Denylist)
1.  Every JWT includes a `jti` (JWT ID) claim—a unique identifier.
2.  When a token is revoked, the server extracts its `jti` and expiration time.
3.  The server saves the `jti` to Redis with a Time-To-Live (TTL) exactly matching the token's remaining lifespan.
4.  **The Catch:** On *every single API request*, the Gateway or Microservice must quickly check Redis: "Is this `jti` in the blocklist?"
5.  **Trade-off:** We have reintroduced state and a centralized database query, partially defeating the "stateless" benefit of JWTs. However, checking a key in Redis is exceptionally fast.

### Strategy 2: Refresh Token Revocation (The Pure Stateless Approach)
1.  Accept that Access Tokens cannot be revoked. Keep their lifespan extremely short (e.g., 5 minutes).
2.  When a user is suspended, delete their Refresh Token from the database.
3.  The attacker can use the stolen Access Token for a maximum of 5 minutes. After that, they attempt to use the Refresh Token, but the server rejects it and forces a re-login.
4.  **Trade-off:** Better performance (no blocklist checks), but a slightly larger security window during which an attacker can operate.

## 4. Refresh Token Rotation
To detect stolen Refresh Tokens:
1.  When a client uses Refresh Token 'A' to get a new Access Token, the server invalidates 'A' and issues a brand new Refresh Token 'B'.
2.  If an attacker stole 'A' and used it, the real user's client will eventually try to use 'A' (which they still have).
3.  The server detects that 'A' was used *twice*. This indicates a compromise. The server immediately revokes the entire chain of tokens for that user, forcing them to authenticate with credentials again.
