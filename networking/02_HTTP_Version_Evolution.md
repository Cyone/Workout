# HTTP Version Evolution: From 1.0 to 3.0

The evolution of HTTP is driven by the need to reduce latency, maximize bandwidth utilization, and adapt to modern web pages that load hundreds of concurrent resources.

## 1. HTTP/1.0: The Transient Connection Era
*   **Mechanism:** Every request/response cycle required opening a brand new TCP connection.
*   **The Penalty:** Opening a TCP connection requires a 3-way handshake (`SYN`, `SYN-ACK`, `ACK`). If using HTTPS, TLS adds another complex handshake. Doing this for every image, CSS, and JS file resulted in massive latency overhead before any actual data was transmitted.

## 2. HTTP/1.1: Persistent Connections (1997)
*   **Mechanism:** Introduced the `Connection: keep-alive` header (which became the default). A single TCP connection stays open after the response is sent, allowing subsequent requests to reuse the established socket.
*   **Pipelining:** Allowed clients to send multiple requests sequentially without waiting for responses. However, it was plagued by middlebox proxy issues and is practically dead today.
*   **The Problem: HTTP Head-of-Line (HOL) Blocking.** Because pipelining failed, browsers send a request, wait for the response, then send the next request. If the server takes 5 seconds to generate the response for Request A, Request B (even if it's a tiny static file) is blocked waiting in the queue.
*   **The Band-Aid:** Browsers implemented parallel connections, opening ~6 distinct TCP sockets to the same domain simultaneously to bypass the serial bottleneck.

## 3. HTTP/2: The Multiplexing Revolution (2015)
HTTP/2 was born out of Google's SPDY protocol. It fundamentally changed how data is framed on the wire without changing HTTP semantics (methods, headers, status codes remain identical).

*   **Mechanism:** It is a **Binary Protocol** (unlike the plain-text HTTP/1.x). Data is broken down into binary "frames".
*   **Key Feature: Multiplexing over a Single TCP Connection.** Multiple requests and responses can be interleaved simultaneously over a single persistent TCP connection using "Streams." Stream ID 1 might send a chunk of HTML, while Stream ID 2 sends a chunk of CSS.
*   **Result:** This completely solved **HTTP-layer Head-of-Line blocking**. Request B no longer waits for Request A.
*   **Header Compression (HPACK):** HTTP headers are highly repetitive. HPACK compresses them using a stateful dictionary, vastly reducing the bytes sent over the wire.
*   **Server Push:** The server can proactively push resources to the client cache before the client explicitly requests them (e.g., sending CSS immediately after HTML).
*   **The New Problem: TCP Head-of-Line Blocking.** HTTP/2 relies on a single TCP connection. TCP guarantees ordered, reliable delivery. If a single network packet is dropped, TCP halts *all* data delivery to the application layer until that specific packet is retransmitted and acknowledged. Because HTTP/2 multiplexes all streams onto this one TCP connection, a single dropped packet stalls *every single active request*.

## 4. HTTP/3: The UDP Paradigm Shift (2022)
HTTP/3 was built to solve the TCP HOL blocking problem introduced by HTTP/2. To do this, it abandons TCP entirely.

*   **Mechanism:** Built on top of **QUIC (Quick UDP Internet Connections)**. QUIC is a transport layer protocol running over UDP.
*   **Why UDP?** UDP is inherently unreliable and unordered. QUIC implements its own flow control, retransmission, and reliability mechanisms in user space (inside the browser/server application) rather than relying on the OS kernel's TCP stack.
*   **Solving the Blocking:** Because QUIC manages streams independently at the transport layer, if a packet belonging to Stream 1 is lost, only Stream 1 is delayed awaiting retransmission. Stream 2, Stream 3, etc., continue to process their packets without interruption.
*   **Faster Handshakes:** QUIC integrates the transport connection and the cryptographic handshake (TLS 1.3) into a single step. For returning clients, it supports **0-RTT** (Zero Round Trip Time) connection resumption, allowing the client to send data in the very first packet.
*   **Connection Migration:** TCP connections are tied to the client's IP address and Port (the 4-tuple). If you switch from Wi-Fi to Cellular, your IP changes, the TCP connection drops, and HTTP requests fail. QUIC uses a unique Connection ID independent of the IP. You can switch networks seamlessly without dropping the active connection.
