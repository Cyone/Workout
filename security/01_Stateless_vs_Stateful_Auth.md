# Stateful vs. Stateless Authentication Architecture

When designing secure backend systems, one of the most fundamental architectural decisions is how to maintain authentication state between HTTP requests, given that HTTP itself is a stateless protocol.

## 1. Stateful Authentication (Session-Based)

In this traditional model, the server bears the burden of remembering who is logged in.

### How It Works
1.  **Login:** The client submits credentials (e.g., username/password).
2.  **Session Creation:** The server validates the credentials, creates a "Session" object in its memory (or database), and generates a cryptographically secure, random Session ID (e.g., `JSESSIONID`).
3.  **Cookie Delivery:** The server returns the Session ID to the client via a `Set-Cookie` header.
4.  **Subsequent Requests:** The browser automatically includes the cookie in all future requests to that domain.
5.  **Validation:** The server receives the cookie, looks up the Session ID in its memory/database, retrieves the user's details, and authorizes the request.

### The Architecture Problem: Horizontal Scaling
*   **Single Server:** Works perfectly. The session is stored in RAM.
*   **Multiple Servers (Load Balanced):** If Server A creates the session, and the Load Balancer routes the next request to Server B, Server B doesn't know the Session ID, and the user is suddenly logged out.
*   **Solutions for Stateful Systems:**
    1.  **Sticky Sessions:** Configure the Load Balancer to always route a specific client to the same server. *Drawback:* Poor load distribution and catastrophic failure if that specific server crashes.
    2.  **Session Replication:** Servers copy their sessions to each other. *Drawback:* Heavy network overhead; scales poorly past a few nodes.
    3.  **Centralized Session Store (The Standard):** Move sessions out of server RAM and into a fast, shared datastore like **Redis**. All servers query Redis to validate the Session ID. *Drawback:* Introduces a single point of failure and network latency for every authenticated request.

## 2. Stateless Authentication (Token-Based / JWT)

In a microservice architecture, relying on a centralized session store (like Redis) for every internal service-to-service call creates massive bottlenecks. Stateless authentication shifts the burden of state to the client.

### How It Works
1.  **Login:** The client submits credentials.
2.  **Token Generation:** The server validates the credentials and generates a JSON Web Token (JWT). This token contains the user's identity (claims) and is **cryptographically signed** by the server.
3.  **Token Delivery:** The server returns the JWT to the client (often in a JSON response payload, to be stored by the client).
4.  **Subsequent Requests:** The client explicitly attaches the JWT to requests, typically in the `Authorization: Bearer <token>` header.
5.  **Validation (The Magic):** The server receives the token. It does **not** query a database. Instead, it recalculates the cryptographic signature. If the signature matches, the server *trusts* the data inside the token.

### The Cryptography (Signatures)
*   **Symmetric (HS256):** The token is signed using a single secret key. The same key is required to create the token and validate it. *Risk:* If you have 50 microservices validating tokens, all 50 must share the master secret. If one is compromised, the attacker can forge tokens.
*   **Asymmetric (RS256):** Uses a Key Pair. The Authentication Server uses a **Private Key** to sign the token. The 50 microservices only need the **Public Key** to validate the signature. They cannot forge new tokens. This is the enterprise standard.

### Advantages of Stateless JWTs
*   **Infinite Scalability:** Servers do not store session state. You can spin up 1,000 instances; as long as they have the public key, they can validate requests instantly with zero network calls to a database.
*   **Decentralization:** Microservices can validate authorization independently.

### Disadvantages of Stateless JWTs
*   **Size:** JWTs contain data (claims) and signatures. They are much larger than a 32-character Session ID, consuming more bandwidth on every request.
*   **Revocation is Difficult:** Because the server doesn't keep a list of active tokens, you cannot easily "log out" a user or revoke a compromised token before it naturally expires. (See *JWT Lifecycle and Revocation*).
