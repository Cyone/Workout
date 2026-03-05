# The Lifecycle of a Request: DNS and Network Routing

A classic system design interview question is: *"What happens when you type google.com into your browser and press Enter?"* Answering this well requires a solid grasp of DNS resolution and basic network routing.

## 1. Domain Name System (DNS) Resolution

Before a browser can open a TCP connection to a server, it must know the server's IP address. Humans read domains (`google.com`); computers route via IPs (`142.250.190.46`). The DNS is the phonebook of the internet.

### The Resolution Steps:
1.  **Browser Cache Check:** The browser first checks its own internal cache. If you visited `google.com` recently, it already knows the IP.
2.  **OS Cache Check:** If the browser doesn't know, it makes a system call (`gethostbyname` or similar). The Operating System checks its local DNS cache.
3.  **Router Cache Check:** The OS queries the router (usually your home Wi-Fi router or office firewall), which may have its own DNS cache.
4.  **ISP Resolver (Recursive Resolver):** If the router fails, the query is sent out to the ISP's DNS resolver (or a public resolver like Google's `8.8.8.8` or Cloudflare's `1.1.1.1`).
    *   *This resolver acts as the "middleman" for the rest of the journey.*
5.  **Root Name Server (.):** The Recursive Resolver doesn't know where `google.com` is, but it knows the IPs of the 13 Root Name Servers globally. It asks one of them.
    *   *The Root Server says: "I don't know `google.com`, but I know who handles all `.com` domains. Go ask the `.com` TLD server."*
6.  **TLD (Top-Level Domain) Server (.com):** The Resolver asks the TLD Server.
    *   *The TLD Server says: "I know who registered `google.com`. Go ask Google's Authoritative Name Server."*
7.  **Authoritative Name Server (google.com):** The Resolver asks Google's own DNS server.
    *   *This server holds the actual A Record (IPv4) or AAAA Record (IPv6) mapping the domain to the specific IP address of the load balancer.*
8.  **The Return:** The Resolver receives the IP, caches it (respecting the Time-To-Live or TTL value), and returns it to the OS, which returns it to the browser.

## 2. Network Routing (Layer 3)

Now that the browser has the IP address, it must build a route through the internet to reach that server.

### The Journey of a Packet:
1.  **The TCP Handshake:** The browser initiates a TCP connection to the destination IP (Port 80 for HTTP, 443 for HTTPS).
2.  **Default Gateway:** The OS encapsulates the TCP segment into an IP packet and sends it to the local router (the Default Gateway).
3.  **BGP (Border Gateway Protocol):** The packet leaves your local network and enters the internet backbone. The internet is a massive web of Autonomous Systems (AS) owned by ISPs, universities, and tech giants.
    *   *Routers at the edge of these networks use BGP to communicate their routing tables.*
    *   *BGP determines the most efficient path (fewest "hops") across these disparate networks to reach the destination IP's subnet.*
4.  **The Load Balancer:** The packet finally arrives at the destination's edge (e.g., an AWS API Gateway or a CDN edge node like Cloudflare).
5.  **The Firewall/WAF:** The traffic is inspected for malicious patterns (SQL injection, DDoS signatures).
6.  **The Proxy:** If using a CDN or Layer 7 Load Balancer, the external TCP/TLS connection terminates here.
7.  **Internal Routing:** The Load Balancer looks at the HTTP request (`GET /search`), determines which internal microservice handles search, and opens a *new* internal TCP connection (often using a private IP space like `10.0.0.0/8`) to forward the request to the application server.

## 3. Key Concepts to Mention in Interviews:
*   **Anycast Routing (CDNs):** CDNs map a single IP address (e.g., `1.1.1.1`) to hundreds of servers globally. BGP routing ensures your request naturally goes to the physically closest server announcing that IP.
*   **Time-To-Live (TTL):** DNS records have a TTL. If a server crashes and you update the DNS record to point to a backup IP, traffic will not switch instantly; clients and resolvers will continue using the old, broken IP until the TTL expires in their caches.
*   **CORS (Cross-Origin Resource Sharing):** A security mechanism implemented by the *browser* to prevent malicious scripts on `evil.com` from making unauthorized API calls to `bank.com`. The browser sends an HTTP `OPTIONS` (preflight) request to check if the server allows cross-origin requests.
