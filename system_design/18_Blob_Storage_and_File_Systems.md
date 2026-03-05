# Blob Storage and File Systems

Storing and serving large binary objects (images, videos, documents, backups) requires a fundamentally different architecture than transactional databases.

## 1. Object Storage vs File Storage vs Block Storage

| Type              | Abstraction     | Access Pattern           | Example               |
|-------------------|-----------------|--------------------------|-----------------------|
| **Object Storage** | Flat namespace (key-value) | HTTP API (GET/PUT)   | S3, GCS, Azure Blob  |
| **File Storage**   | Hierarchical (directories) | NFS, SMB           | EFS, FSx, NAS        |
| **Block Storage**  | Raw disk blocks | Mounted as volume        | EBS, Persistent Disk  |

**Object storage is the default choice** for web applications — it's infinitely scalable, cheap, and accessible via HTTP.

## 2. Object Storage Architecture (S3-Like)

### How S3 Works Internally (Simplified)

```
Client → PUT /bucket/key → API Gateway → Metadata Service → Data Storage
           │                                    │                  │
           │                              ┌─────▼──────┐    ┌─────▼──────┐
           │                              │ Metadata DB │    │ Storage    │
           │                              │ (key→location)│   │ Nodes      │
           │                              └────────────┘    │ (chunked)  │
           │                                                 └────────────┘
```

1.  **Metadata Service:** Maps object keys to physical storage locations. Stores size, content type, timestamps, user metadata.
2.  **Data Service:** Stores the actual bytes. Large objects are **chunked** (e.g., 64MB chunks) and distributed across storage nodes.
3.  **Replication:** Each chunk is replicated across multiple nodes/AZs for durability (S3: 99.999999999% — eleven 9s).

## 3. Upload Patterns

### Direct Upload (Small Files)
```
Client → PUT /bucket/key → S3
```
Simple, but limited to ~5GB per request.

### Multipart Upload (Large Files)
1.  Client initiates a multipart upload → receives an `uploadId`.
2.  Client uploads parts (5MB-5GB each) in parallel.
3.  Client sends a "complete" request listing all parts.
4.  S3 assembles the parts into a single object.

**Benefit:** Parallelism, resumability (re-upload only failed parts).

### Presigned URLs (Server-Managed Access)
The server generates a time-limited, signed URL that allows the client to upload/download directly to/from S3 **without the server relaying bytes**.

```java
// Generate presigned PUT URL (valid for 15 minutes)
URL presignedUrl = s3Client.presignPutObject(request -> request
    .signatureDuration(Duration.ofMinutes(15))
    .putObjectRequest(put -> put.bucket("my-bucket").key("uploads/photo.jpg"))
);

// Client uses the URL to upload directly to S3
// PUT presignedUrl → S3 (server is not in the data path)
```

```
Without presigned:  Client → Server → S3 (server is bottleneck)
With presigned:     Client → S3 directly (server only issues the URL)
```

## 4. Content Deduplication

**Problem:** Users upload the same file multiple times. Storing duplicates wastes storage.

**Solution:** Content-addressable storage.
1.  Hash the file content (SHA-256).
2.  Use the hash as the storage key.
3.  Before storing, check if the hash already exists → skip upload if duplicate.

**Used by:** Dropbox, Google Drive internally.

## 5. Image/Video Processing Pipeline

```
User uploads image
    → S3 (store original)
    → Lambda/consumer triggered
        → Generate thumbnail (150x150)
        → Generate medium (600x600)
        → Convert to WebP
        → Store resized versions in S3
    → CDN serves optimized images
```

**Lazy generation (alternative):** Don't pre-generate resizes. Use an image proxy (e.g., Imgproxy, Cloudinary) that generates on-the-fly and caches at the CDN.

## 6. Storage Tiers (Cost Optimization)

| Tier                  | Access Frequency | Cost (per GB/month) | Use Case              |
|-----------------------|-----------------|---------------------|-----------------------|
| **Standard**          | Frequent         | $$$$               | Active application data |
| **Infrequent Access** | Monthly          | $$$                | Backups, logs         |
| **Glacier/Archive**   | Yearly           | $                  | Compliance archives   |

**Lifecycle policies:** Automatically transition objects between tiers based on age.
```
Day 0-30: Standard → Day 31-90: IA → Day 91+: Glacier
```

## 7. Signed URLs and Access Control

| Method           | Scope                        | Duration          |
|------------------|------------------------------|-------------------|
| **Presigned URL** | Single object                | Minutes to hours  |
| **IAM Policy**   | Bucket/prefix level          | Permanent         |
| **Bucket Policy** | Public/cross-account access  | Permanent         |
| **CORS Config**  | Browser direct upload        | N/A               |

## 8. Interview Tips

*   **"How would you design Dropbox/Google Drive?"** → Object storage for files, metadata DB for file tree, presigned URLs for upload/download, CDC/webhooks for sync.
*   Always mention **chunking + multipart upload** for large files.
*   **Presigned URLs** are the answer to "How does the client upload files without overloading your server?"
*   For image-heavy applications, discuss the **processing pipeline** (resize, compress, serve via CDN).
