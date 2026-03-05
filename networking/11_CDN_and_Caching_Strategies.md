# CDN and HTTP Caching Strategies

"There are only two hard things in Computer Science: cache invalidation and naming things." — Phil Karlton. Caching is the single most impactful lever for reducing latency and server load. A senior engineer must understand caching at every layer, from the browser to the CDN edge.

## 1. The Cache-Control Header (The Modern Standard)

`Cache-Control` is the primary mechanism for controlling what gets cached, by whom, and for how long.

### Freshness Directives (How Long?)
*   **`max-age=N`**: The response is fresh for `N` seconds from the moment it was served. After `N` seconds, the cache must revalidate.
*   **`s-maxage=N`**: Like `max-age`, but applies *only* to shared caches (CDNs, proxies). Overrides `max-age` for CDN purposes. Allows you to set `max-age=60, s-maxage=86400` — the browser revalidates after 1 minute, but the CDN holds it for 24 hours.
*   **`stale-while-revalidate=N`**: Allow the cache to serve a stale response while it *asynchronously* fetches a fresh one in the background. Eliminates the revalidation latency from the user's perspective.
*   **`stale-if-error=N`**: Serve stale content if the origin server returns an error (5xx) for up to `N` seconds. Improves resilience during origin outages.

### Permission Directives (Who Can Cache?)
*   **`public`**: Any cache (browser, CDN, proxy) may cache this response. Typically used for static assets.
*   **`private`**: Only the end-user's browser may cache this. CDNs must not. Used for authenticated, user-specific responses (e.g., a user's profile page).
*   **`no-store`**: Under no circumstances should this response be cached anywhere. Use for highly sensitive data (bank balances, medical records).
*   **`no-cache`**: The response *can* be stored, but it must be revalidated with the server before every use. Counterintuitive name—it does not mean "don't cache."

### Practical Examples:
| Resource | `Cache-Control` | Reasoning |
|---|---|---|
| Static JS/CSS (versioned) | `public, max-age=31536000, immutable` | Hash in filename means content never changes. Cache for 1 year. `immutable` tells browsers not to revalidate even on explicit reload. |
| API Response (user data) | `private, no-cache` | User-specific, must always check freshness. |
| Public HTML page | `public, max-age=60, stale-while-revalidate=30` | Cache for 60s. If stale, serve old version while fetching fresh. |
| Session cookies | `no-store` | Never cache sensitive authentication data. |

---

## 2. Cache Revalidation: ETags and Last-Modified

When `max-age` expires, the cache has two choices: fetch from origin unconditionally, or *revalidate*. Conditional requests let the cache check "has it actually changed?" before downloading the full response body.

### ETag (Entity Tag) — Preferred
1.  The origin server computes a hash of the resource content and includes it: `ETag: "abc123def456"`.
2.  The browser/CDN stores the ETag alongside the cached response.
3.  On revalidation: `GET /image.png` with `If-None-Match: "abc123def456"`.
4.  If unchanged: `304 Not Modified` (empty body, tiny response). Cache is refreshed with a new `max-age`. **No data is transferred.**
5.  If changed: `200 OK` with the new body and a new ETag.

### Last-Modified
*   Similar concept but uses timestamps: `Last-Modified: Mon, 01 Jan 2024 12:00:00 GMT`.
*   Revalidation header: `If-Modified-Since: Mon, 01 Jan 2024 12:00:00 GMT`.
*   **Weakness:** Timestamps have 1-second granularity. ETags are always preferred (more precise). Multiple servers might compute the same timestamp even for different content.

---

## 3. CDN Architecture

A Content Delivery Network (CDN) is a globally distributed network of servers (**Points of Presence**, or PoPs) that cache and serve content close to users.

### Request Flow
```
User (London) → Cloudflare London PoP → [Cache HIT? Serve directly]
                                       → [Cache MISS?] → Origin Shield (EU) → [Cache HIT?]
                                                                              → [Cache MISS?] → Origin Server
```

### Key Architectural Concepts

*   **Edge Node / PoP (Point of Presence):** Geographically distributed servers (e.g., 200+ global PoPs for Cloudflare). When you request `example.com/logo.png`, you hit the nearest PoP. With `max-age` set, that image is served from the edge with single-digit millisecond latency.

*   **Origin Shield:** An intermediate caching layer between the edge PoPs and your origin servers. If you have 200 PoPs and 1,000 cache misses per PoP, that's 200,000 requests hitting the origin. With an Origin Shield, cache misses from all PoPs are collapsed into a single request to the shield, which then proxies to the origin. Reduces origin load drastically.

*   **Pull CDN (Most Common):** The CDN does not have a resource cached. The first user who requests it triggers a "pull" from the origin. The CDN stores the response and serves all subsequent users from cache.

*   **Push CDN:** You proactively upload content to CDN storage (like a bucket) before any user requests it. Best for large files (video content) where you want to pre-warm the cache globally.

*   **Anycast Routing:** CDNs use BGP Anycast — the same IP address (e.g., `1.1.1.1`) is announced from hundreds of different geographic locations. The internet's routing protocol naturally directs your packet to the *closest* server announcing that IP address.

---

## 4. Cache Invalidation Strategies

This is the hard part. How do you remove something from the cache when it changes?

### Strategy 1: URL Versioning / Cache-Busting (Best for static assets)
Embed a content hash in the filename or URL. When the content changes, the URL changes, so the old cache entry is never referenced again, and new content gets its own fresh cache entry indefinitely.
*   `main.a3f8bc.js` → Content changes → `main.d4e9cf.js`.
*   Set `Cache-Control: public, max-age=31536000, immutable`.
*   ⭐ **This is what Webpack, Next.js, and Vite do automatically.**

### Strategy 2: Surrogate Keys / Cache Tags
Assign semantic tags to CDN-cached responses. Invalidate by tag, not by URL, allowing you to purge groups of related URLs at once.
*   Tag the response: `Surrogate-Key: product-123 category-electronics`
*   When Product 123's price changes, issue a single API call to purge all cached responses tagged with `product-123`.
*   **Used by:** Fastly, Cloudflare Cache Reserve.

### Strategy 3: Short TTL with Stale-While-Revalidate
Instead of complex invalidation, use a short `max-age` (e.g., 60 seconds) so content becomes slightly stale but is always roughly up-to-date. Combine with `stale-while-revalidate` to eliminate user-facing latency on revalidation.

### Strategy 4: CDN Purge API
All major CDNs provide an API to forcibly delete a cached URL immediately. Useful for emergency invalidation. Downside: must be triggered manually or via a deployment pipeline hook. Does not scale to millions of URLs.

---

## 5. Browser, CDN, and Application Cache Layers

A request may be served by any of multiple cache layers:

| Layer | Cache Type | Scope | Eviction |
|---|---|---|---|
| **Browser** | HTTP cache | Per user, per browser | TTL, manual clear |
| **CDN Edge** | Distributed shared cache | All users globally | TTL, purge API |
| **Load Balancer** | Varnish, NGINX cache | Single region | TTL |
| **Application** | In-memory (Caffeine) / Redis | Per service instance | TTL, LRU |
| **Database** | Query result cache | Per DB instance | Invalidated on write |

The golden rule: a cache hit at a lower-numbered layer (browser) is always faster and cheaper than a hit at a higher layer. Design for the broadest possible browser and CDN cacheability first.
