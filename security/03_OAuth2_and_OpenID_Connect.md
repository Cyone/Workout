# OAuth 2.0 and OpenID Connect (OIDC)

It is a common misconception that OAuth 2.0 is an authentication protocol. It is an **Authorization** framework. It dictates how one application can gain limited access to resources hosted by another application on behalf of a user.

## 1. The OAuth 2.0 Actors
To understand the flows, you must know the actors:
1.  **Resource Owner:** The user (e.g., you).
2.  **Client:** The application requesting access (e.g., a mobile game asking for access to your Google Contacts).
3.  **Authorization Server:** The server verifying identity and issuing tokens (e.g., Google's login server).
4.  **Resource Server:** The API hosting the data, which validates the token (e.g., Google Contacts API).

## 2. OAuth 2.0 Grant Types (Flows)
A "Grant Type" is the specific sequence of HTTP requests used to obtain an Access Token.

### 1. Authorization Code Grant (The Gold Standard)
Used for server-side web applications and modern mobile/Single Page Applications (SPAs) with PKCE.
*   **The Flow:**
    1.  The Client redirects the browser to the Authorization Server (`/authorize`).
    2.  The User logs in and approves access.
    3.  The Authorization Server redirects the browser back to the Client with a short-lived **Authorization Code** in the URL query string.
    4.  The Client's backend server securely sends this Code, along with its `client_secret`, directly to the Authorization Server (`/token`).
    5.  The Authorization Server returns the **Access Token** (and usually a Refresh Token).
*   **Why the extra step?** The Access Token is never exposed to the user's browser or URL history; it is exchanged safely backend-to-backend.

### 2. Authorization Code with PKCE (Proof Key for Code Exchange)
Required for SPAs (React, Angular) or mobile apps that cannot securely store a `client_secret`.
*   **The Problem:** If a malicious app intercepts the redirect URI, it can steal the Authorization Code. Since there's no `client_secret`, the malicious app can exchange the code for a token.
*   **The Fix:** The Client generates a random secret (`code_verifier`) and sends its hash (`code_challenge`) during step 1. In step 4, the Client sends the raw `code_verifier`. The server hashes it and verifies it matches the original challenge. A malicious app won't know the raw verifier.

### 3. Client Credentials Grant
Used for Machine-to-Machine (M2M) communication (e.g., a cron job microservice needs to call a reporting microservice).
*   **The Flow:** There is no user involvement. The Client sends its `client_id` and `client_secret` to the Authorization Server and immediately receives an Access Token.

### 4. Deprecated Grants
*   **Implicit Grant:** Previously used for SPAs. Returned the token directly in the URL fragment. Highly insecure, vulnerable to token leakage in browser history. Replaced by Code + PKCE.
*   **Resource Owner Password Credentials:** The client asks the user for their raw username/password and sends them to the server. An anti-pattern unless the Client is heavily trusted (e.g., a first-party mobile app). Deprecated in OAuth 2.1.

## 3. OpenID Connect (OIDC)
OAuth 2.0 issues Access Tokens. An Access Token says "You have permission to read the contacts API." It does **not** tell the Client application *who* the user is, their name, or their email.

OpenID Connect is a thin identity layer built on top of OAuth 2.0.
*   **The ID Token:** OIDC introduces a new token (always a JWT) alongside the Access Token.
*   **Purpose:** The ID Token contains claims about the user's identity (e.g., `sub` for user ID, `name`, `email`). The Client application parses this token to log the user into its own system and display their profile.
*   **The `openid` Scope:** To trigger OIDC, the Client simply includes the scope `openid` during the initial authorization request.
*   **The UserInfo Endpoint:** OIDC defines a standard `/userinfo` endpoint that the Client can call (using the Access Token) to retrieve more detailed profile data.
