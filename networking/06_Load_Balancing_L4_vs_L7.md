# Load Balancing and API Gateways: Layer 4 vs. Layer 7

When scaling a backend system horizontally, you must distribute traffic across multiple instances of your application (e.g., multiple Spring Boot pods). This is the role of a Load Balancer or an API Gateway (like NGINX, HAProxy, Envoy, or AWS ALB/NLB).

Understanding the difference between Layer 4 (Transport) and Layer 7 (Application) load balancing is a fundamental system design concept.

## 1. The OSI Model Refresher
To understand load balancing, you must recall the relevant layers of the Open Systems Interconnection (OSI) model:
*   **Layer 3 (Network):** IP Protocol (Handles IP addresses and routing).
*   **Layer 4 (Transport):** TCP/UDP (Handles ports and connection reliability).
*   **Layer 7 (Application):** HTTP/HTTPS, gRPC, WebSocket (Handles headers, payloads, and application semantics).

## 2. Layer 4 Load Balancing (Transport Level)

A Layer 4 (L4) load balancer routes traffic based *only* on network and transport layer data: IP addresses and TCP/UDP ports.

### How It Works
1.  A client initiates a TCP connection to the Load Balancer's IP address (e.g., on port 443).
2.  The L4 Load Balancer receives the TCP SYN packet.
3.  Without looking at the payload (it doesn't know or care if it's HTTP, SSH, or an encrypted database query), it selects an upstream server from its pool (e.g., using Round Robin).
4.  It performs **Network Address Translation (NAT)**, swapping the destination IP from its own to the chosen backend server's IP, and forwards the packets.

### Characteristics & Use Cases
*   **Performance:** Blisteringly fast. It requires almost zero CPU or memory overhead because it's simply forwarding packets at the kernel level.
*   **Security (TLS Passthrough):** L4 load balancers cannot decrypt HTTPS traffic because they operate below the application layer. The encrypted packets are passed straight through to your application server, meaning your Spring Boot app must hold the SSL certificate and perform the expensive TLS handshake.
*   **Blind Routing:** It cannot route traffic based on URLs (`/api/v1` vs `/api/v2`), HTTP headers, or cookies. It just sees a stream of bytes.
*   **Best For:** Database clusters (PostgreSQL, MongoDB), caching layers (Redis), or when extreme throughput is required and application-level routing isn't needed (AWS Network Load Balancer - NLB).

## 3. Layer 7 Load Balancing (Application Level)

A Layer 7 (L7) load balancer (or API Gateway) understands the application protocol. It terminates the connection, reads the HTTP request, makes a routing decision, and establishes a new connection to the backend.

### How It Works
1.  A client initiates a TCP connection to the Load Balancer (Port 443).
2.  **TLS Termination:** The L7 balancer performs the TLS handshake, decrypting the HTTPS traffic into plain HTTP.
3.  **Application Inspection:** It parses the HTTP request, examining the URI (`GET /users`), Headers (`Authorization: Bearer...`), and even the payload.
4.  **Intelligent Routing:** Based on its rules, it routes the traffic. For example:
    *   If URI starts with `/users`, route to the User Microservice.
    *   If URI starts with `/orders`, route to the Order Microservice.
5.  It opens a *second* TCP connection to the chosen backend service and forwards the plaintext HTTP request.

### Characteristics & Use Cases
*   **Flexibility:** Allows complex routing, path rewrites, header injection/removal, and A/B testing (routing 10% of traffic to a new version based on a header).
*   **Centralized Security:** SSL certificates are managed centrally on the Load Balancer. It can also enforce Rate Limiting or Web Application Firewall (WAF) rules before traffic ever reaches your application.
*   **Overhead:** Slower and more resource-intensive than L4 because it must buffer the request, decrypt it, parse headers, and manage two separate TCP connections.
*   **Best For:** Microservice architectures (API Gateways like Spring Cloud Gateway or Kong), HTTP/gRPC routing, and typical web traffic (AWS Application Load Balancer - ALB).

## 4. Summary: Which to Choose?
*   Need to inspect the URL or headers to decide where the request goes? **Layer 7.**
*   Need to offload SSL decryption from your servers? **Layer 7.**
*   Need to load balance non-HTTP traffic (like a Redis cluster)? **Layer 4.**
*   Need absolute maximum throughput with sub-millisecond latency and don't care about the HTTP payload? **Layer 4.**
