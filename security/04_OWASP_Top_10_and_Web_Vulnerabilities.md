# OWASP Top 10 and Web Vulnerabilities

A Senior Backend Developer is responsible for the security of the application. Interviews will test your awareness of common web attack vectors and your ability to write defensive code.

## 1. SQL Injection (SQLi)
*   **The Attack:** An attacker injects malicious SQL into input fields (e.g., `' OR '1'='1`). If the backend uses simple string concatenation to build queries, the attacker can bypass authentication or dump the entire database.
*   **The Defense:** 
    *   **Always use Prepared Statements / Parameterized Queries.** The SQL template is pre-compiled, and input is treated strictly as data, not code.
    *   **Spring/JPA:** Spring Data JPA and Hibernate use prepared statements by default. However, be extremely careful when writing custom `@Query` with native SQL concatenation.

## 2. Cross-Site Scripting (XSS)
*   **The Attack:** An attacker injects a malicious script (usually JavaScript) into your application (e.g., via a comment section). When other users view the comment, the script runs in their browser, potentially stealing their Session Cookies or performing actions on their behalf.
*   **The Defense:**
    *   **Sanitize Input & Escape Output:** Treat all user-generated content as untrusted.
    *   **Content Security Policy (CSP):** An HTTP header that tells the browser only to execute scripts from trusted domains.
    *   **HttpOnly Cookies:** As discussed in previous guides, this prevents JavaScript from reading sensitive session/refresh tokens.

## 3. Cross-Site Request Forgery (CSRF)
*   **The Attack:** A user is logged into `yourbank.com`. They visit `attacker.com`. The malicious site triggers a background POST request to `yourbank.com/transfer`. The browser automatically attaches the bank's session cookies, and the bank processes the transfer thinking it's from the user.
*   **The Defense:**
    *   **Anti-CSRF Tokens:** A unique, non-guessable token required in every state-changing request (POST, PUT, DELETE). The attacker's script cannot read this token, so it cannot forge the request.
    *   **SameSite=Strict/Lax Cookie Attribute:** Prevents the browser from sending cookies on cross-site requests.

## 4. Broken Access Control
*   **The Attack:** Insecure Direct Object References (IDOR). A user logs in and sees their profile at `/api/users/105`. They manually change the URL to `/api/users/106` and are able to see another user's private data.
*   **The Defense:** 
    *   **Server-Side Authorization:** Never trust the ID in the URL. Always check if the *currently authenticated user* (from the security context) actually has permission to access the resource with that ID.

## 5. Server-Side Request Forgery (SSRF)
*   **The Attack:** An attacker provides a URL for the backend to fetch (e.g., "Set profile picture from URL"). Instead of an image URL, the attacker provides `http://localhost:8080/admin` or `http://169.254.169.254/latest/meta-data/` (AWS metadata). The backend server fetches its own internal admin page or cloud credentials and returns them to the attacker.
*   **The Defense:** 
    *   **Allow-listing:** Only allow fetching from a strict list of trusted domains.
    *   **Network Isolation:** Ensure the application server cannot reach sensitive internal IP ranges.

## 6. Security Headers
*   **`X-Content-Type-Options: nosniff`:** Prevents the browser from "guessing" the content type and potentially executing a text file as a script.
*   **`Strict-Transport-Security` (HSTS):** Forces the browser to only use HTTPS.
*   **`X-Frame-Options: DENY`:** Prevents Clickjacking by not allowing your site to be embedded in an iframe.
