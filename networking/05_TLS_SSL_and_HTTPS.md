# TLS, SSL, and HTTPS: Securing the Transport Layer

For a Senior Backend Engineer, understanding how data is encrypted in transit is critical. It's not enough to say "HTTPS encrypts data." You must understand *how* the handshake works and the difference between Server-Side and Mutual TLS (mTLS).

## 1. Core Cryptographic Concepts

### Symmetric Encryption
*   **Mechanism:** Uses a **single shared key** to both encrypt and decrypt data (e.g., AES).
*   **Pros:** Extremely fast and efficient. Perfect for encrypting large streams of data.
*   **Cons:** The Key Distribution Problem. How do the client and server safely share this single key over an insecure internet without someone intercepting it?

### Asymmetric Encryption (Public Key Cryptography)
*   **Mechanism:** Uses a **mathematically linked key pair** (e.g., RSA).
    *   **Public Key:** Given to everyone. It can only be used to *encrypt* data.
    *   **Private Key:** Kept secret by the server. It is the only key that can *decrypt* data encrypted by the Public Key.
*   **Pros:** Solves the key distribution problem.
*   **Cons:** Very slow and computationally expensive. Not suitable for encrypting megabytes of application data.

## 2. The TLS Handshake (How HTTPS Works)

TLS (Transport Layer Security) combines both asymmetric and symmetric encryption to achieve secure, fast communication.

1.  **Client Hello:** The client (browser/app) connects to the server and says, "Let's talk securely. Here are the cipher suites (encryption algorithms) and TLS versions I support."
2.  **Server Hello & Certificate:** The server responds, "Let's use TLS 1.3 and this specific cipher. Here is my **Digital Certificate**."
    *   *The Certificate contains the server's Public Key.*
    *   *The Certificate is digitally signed by a trusted Certificate Authority (CA).*
3.  **Certificate Verification:** The client checks the Certificate's signature against the Root CAs pre-installed in its operating system/browser. This proves the server is who it claims to be (Authentication).
4.  **Key Exchange (The Magic):**
    *   The client generates a random **Symmetric Session Key**.
    *   The client *encrypts* this Session Key using the server's **Public Key** (Asymmetric Encryption).
    *   The client sends the encrypted Session Key to the server.
5.  **Decryption & Secure Channel:**
    *   The server receives the encrypted Session Key and uses its **Private Key** to decrypt it.
    *   Now, *both* the client and the server possess the exact same Symmetric Session Key, but it was never transmitted in plaintext.
6.  **Encrypted Application Data:** From this point on, all HTTP traffic is encrypted using the fast **Symmetric Session Key**. Asymmetric encryption is no longer used.

## 3. Mutual TLS (mTLS)

Standard TLS (described above) only authenticates the *server*. The client proves the server is legitimate, but the server has no idea who the client is (which is why we use JWTs/Passwords at the application layer).

In a microservice architecture, **mTLS** is often required.

*   **Mechanism:** In mTLS, *both* the client and the server have certificates and private keys.
*   **The Handshake Difference:**
    *   Server sends its Certificate to the Client.
    *   *Server requests the Client's Certificate.*
    *   Client sends its Certificate to the Server.
    *   The Server validates the Client's Certificate against a trusted internal CA.
*   **Why use mTLS?**
    *   **Zero-Trust Security:** Service A will absolutely refuse a TCP connection from Service B unless Service B presents a valid internal cryptographic certificate.
    *   **Phishing/Spoofing Prevention:** Even if an attacker steals an internal JWT, they cannot make requests to your microservices because they do not possess the client-side private key and certificate required to establish the underlying TCP/TLS connection.
    *   **Service Mesh:** In modern architectures (like Istio), the mTLS handshake is handled entirely by sidecar proxies (Envoy) injected next to your Spring Boot apps. The Spring Boot app communicates in plaintext over `localhost` to its sidecar, and the sidecars handle the complex mTLS encryption between nodes.

## 4. TLS Termination / Offloading

Encrypting and decrypting traffic requires CPU cycles.
*   **TLS Termination:** Instead of configuring your Spring Boot application (Tomcat/Undertow) to handle certificates, you configure the Load Balancer or API Gateway (e.g., NGINX, AWS ALB) to hold the SSL Certificate.
*   **The Flow:**
    1.  Client connects via HTTPS to the Load Balancer.
    2.  The Load Balancer performs the TLS handshake and decrypts the traffic.
    3.  The Load Balancer forwards the request as plain HTTP (port 80) to the internal Spring Boot application over the private VPC network.
*   **Benefits:** Centralized certificate management and reduced CPU load on the application servers.
