# CDN and Content Delivery

A Content Delivery Network (CDN) caches content at geographically distributed edge locations, reducing latency and offloading traffic from origin servers.

## 1. How a CDN Works

```
User (Tokyo) → Edge Server (Tokyo PoP) → [Cache HIT] → Return content (5ms)
                                        → [Cache MISS] → Origin Server (US) → Cache at edge → Return (200ms)
```

*   **PoP (Point of Presence):** A CDN data center with cache servers. Major CDNs have 200+ PoPs worldwide.
*   **Edge Server:** A cache server within a PoP that stores copies of content close to users.
*   **Origin Server:** Your actual server (AWS, your data center) that hosts the original content.

## 2. Push vs Pull CDN

| Model    | How It Works                                    | Best For                         |
|----------|------------------------------------------------|----------------------------------|
| **Pull** | Edge fetches from origin on first request (cache miss), then caches. | Dynamic content, sites with changing content |
| **Push** | You manually upload content to the CDN ahead of time. | Static assets, large files, video |

Most CDNs use **Pull by default** — content is lazily cached on the first request.

## 3. Cache Invalidation

The hardest problem with CDNs: how to update stale content at 200+ edge locations?

| Strategy          | How                                                    | Trade-off                     |
|-------------------|--------------------------------------------------------|-------------------------------|
| **TTL-based**     | Set `Cache-Control: max-age=3600`. Content expires after 1 hour. | Stale content during TTL window |
| **Versioned URLs**| `style.v2.css` or `style.css?v=abc123`. New version = new URL = cache miss. | Requires asset pipeline |
| **Purge API**     | Send API call to CDN to invalidate specific URLs/patterns. | Slower propagation (seconds–minutes) |
| **Stale-While-Revalidate** | Serve stale content while fetching fresh copy in the background. | Best UX, slight staleness |

**Best practice:** Use **versioned/hashed filenames** for static assets (JS, CSS, images). Use **short TTLs + stale-while-revalidate** for HTML pages.

## 4. Origin Shield

An intermediate CDN cache layer between edge servers and the origin.

```
Edge (Tokyo) → [MISS] → Shield (US-West) → [HIT] → Return
Edge (London) → [MISS] → Shield (US-West) → [HIT] → Return
// Only 1 request reaches the Shield, not 200 edge servers hitting origin
```

**Benefit:** Reduces load on your origin server dramatically. Without a shield, a cache miss at 200 edges = 200 requests to your origin simultaneously.

## 5. Video Streaming with CDN

### Adaptive Bitrate Streaming (ABR)

The video is encoded at multiple quality levels (240p, 480p, 720p, 1080p, 4K) and split into small **segments** (2–10 seconds each).

*   **HLS (HTTP Live Streaming):** Apple's protocol. Uses `.m3u8` manifests and `.ts` segments.
*   **DASH (Dynamic Adaptive Streaming over HTTP):** Open standard. Uses `.mpd` manifests and `.m4s` segments.

The video player monitors bandwidth and switches quality mid-stream:
```
Good connection → 1080p segments
Network degrades → switch to 480p segments (no buffering)
Recovers → back to 1080p
```

CDNs are critical for streaming — serve segments from the closest edge, reducing buffering.

## 6. Signed URLs and Access Control

For protected content (paid videos, private files), the CDN needs to verify access.

*   **Signed URLs:** The application generates a URL with an expiration timestamp and a cryptographic signature. The CDN validates the signature before serving.
*   **Signed Cookies:** Broader access control — once authenticated, the cookie grants access to all files under a path.

```
https://cdn.example.com/videos/movie.mp4
  ?Expires=1672531260
  &Signature=abc123...
  &Key-Pair-Id=APKA...
```

## 7. Popular CDN Providers

| Provider            | Strengths                                     |
|--------------------|-----------------------------------------------|
| **CloudFront (AWS)** | Deep AWS integration, Lambda@Edge            |
| **Cloudflare**      | DDoS protection, Workers (edge compute)       |
| **Akamai**          | Largest network, enterprise-grade             |
| **Fastly**          | Real-time purge, edge compute (Compute@Edge)  |

## 8. CDN Anti-Patterns

*   **Caching personalized content:** CDN should NOT cache responses with user-specific data (profile pages, authenticated API responses). Use `Cache-Control: private, no-store`.
*   **Cache-busting on every request:** Adding a random query param defeats caching entirely.
*   **No fallback:** If the CDN is down, your application should serve directly from origin (graceful degradation).

## 9. Interview Tips

*   **When to mention CDN in interviews:** Any system serving static content, media, or serving global users.
*   Know the difference between **Pull and Push** models.
*   **Cache invalidation** is the hardest part — always discuss your strategy.
*   For video streaming questions, explain **adaptive bitrate (ABR)** with segment-based delivery over CDN.
