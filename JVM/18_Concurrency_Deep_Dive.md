# 18_Concurrency_Deep_Dive.md

## Java Concurrency Tooling — The Complete `java.util.concurrent` Guide

### 1. Atomic Classes (`java.util.concurrent.atomic`)
All use **CAS (Compare-And-Swap)** hardware instructions — no locks.

| Class | What It Wraps |
|-------|---------------|
| `AtomicInteger` / `AtomicLong` | A single `int` / `long` |
| `AtomicBoolean` | A single `boolean` |
| `AtomicReference<V>` | A single object reference |
| `AtomicIntegerArray` | An `int[]` with per-index atomicity |
| `AtomicStampedReference<V>` | Reference + `int` stamp (solves ABA problem) |
| `AtomicMarkableReference<V>` | Reference + `boolean` mark |
| `LongAdder` / `LongAccumulator` | High-contention counters (striped cells, faster than `AtomicLong`) |

```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();                    // CAS loop internally
counter.compareAndSet(5, 10);                 // true if current == 5
counter.updateAndGet(x -> x * 2);             // Java 8+ lambda CAS
```

**ABA Problem:** Thread reads `A`, another thread changes `A→B→A`. CAS succeeds because value is `A` again, but state may have changed. Fix: `AtomicStampedReference` — CAS checks both value AND stamp (version counter).

### 2. `ConcurrentHashMap`
*   **Java 7:** Segment-based locking (16 segments, each a mini-HashMap with its own lock).
*   **Java 8+:** **CAS + per-node `synchronized`** — locks only the **head node** of an individual bucket. Much finer granularity.
*   **Key Operations:**

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("a", 1);
map.computeIfAbsent("b", k -> expensiveCompute(k)); // Atomic compute, no double-computation
map.merge("a", 1, Integer::sum);                    // Atomic increment pattern
map.forEach(2, (k, v) -> process(k, v));            // Parallel bulk op (parallelism threshold = 2)
```

*   **Gotcha:** `size()` is an **estimate** under concurrency. Use `mappingCount()` for a `long` estimate.
*   **Null disallowed:** Both keys and values must be non-null (unlike `HashMap`).
*   **`computeIfAbsent` deadlock:** The lambda runs while holding the bucket lock. If the lambda tries to update the same map, it can deadlock.

### 3. `ConcurrentLinkedQueue` / `ConcurrentLinkedDeque`
*   **Lock-free** implementations using CAS.
*   `ConcurrentLinkedQueue` — FIFO. `ConcurrentLinkedDeque` — double-ended.
*   `size()` is **O(n)** — traverses the entire list. Avoid calling it.
*   Use case: High-concurrency producer-consumer where blocking is unacceptable.

### 4. `BlockingQueue` Family
All support **blocking** `put()` (blocks if full) and `take()` (blocks if empty).

| Implementation | Bounds | Backing Structure | Special Behavior |
|----------------|--------|-------------------|------------------|
| `ArrayBlockingQueue` | Bounded | Circular array | Fair/unfair ordering |
| `LinkedBlockingQueue` | Optionally bounded | Linked nodes | Separate put/take locks (higher throughput) |
| `SynchronousQueue` | Zero capacity | None | Each `put()` blocks until a `take()` — direct handoff |
| `PriorityBlockingQueue` | Unbounded | Binary heap | Elements dequeued in priority order |
| `DelayQueue` | Unbounded | Priority heap | Elements available only after their delay expires |
| `LinkedTransferQueue` | Unbounded | Linked nodes | `transfer()` — blocks until consumer receives |

```java
BlockingQueue<Task> queue = new ArrayBlockingQueue<>(100);
// Producer
queue.put(task);      // Blocks if queue is full
// Consumer
Task t = queue.take(); // Blocks if queue is empty
```

**`SynchronousQueue` use case:** `Executors.newCachedThreadPool()` uses it internally — tasks are handed directly from submitter to a worker thread.

### 5. `ReadWriteLock` / `StampedLock`

#### `ReentrantReadWriteLock`
*   Multiple threads can hold the **read lock** simultaneously.
*   Only one thread can hold the **write lock**, and it excludes all readers.
*   Risk: **Write starvation** — continuous readers can block writers indefinitely. Use fair mode (`new ReentrantReadWriteLock(true)`).

```java
ReadWriteLock rwl = new ReentrantReadWriteLock();
rwl.readLock().lock();    // Multiple readers OK
rwl.writeLock().lock();   // Exclusive
```

#### `StampedLock` (Java 8+)
*   Three modes: **Write**, **Read**, **Optimistic Read**.
*   **Optimistic read** doesn't acquire a lock — just grabs a stamp and validates after reading.

```java
StampedLock sl = new StampedLock();
long stamp = sl.tryOptimisticRead();      // Non-blocking, no lock acquired
double x = this.x; double y = this.y;    // Read shared state
if (!sl.validate(stamp)) {               // Was a write acquired during our read?
    stamp = sl.readLock();                // Fall back to full read lock
    try { x = this.x; y = this.y; }
    finally { sl.unlockRead(stamp); }
}
```

*   **Not reentrant!** If you hold a write and try to acquire again → deadlock.
*   Not `Serializable`, cannot be used with `Condition`.

### 6. `CompletableFuture`
The Swiss army knife of async Java.

```java
CompletableFuture.supplyAsync(() -> fetchUser(id))      // Runs on ForkJoinPool
    .thenApply(user -> enrichProfile(user))              // Transform result (same thread)
    .thenCompose(profile -> fetchOrders(profile.id()))   // Chain another async op (flatMap)
    .thenAccept(orders -> display(orders))               // Consume result
    .exceptionally(ex -> { log(ex); return fallback; }); // Handle errors
```

| Method | Description |
|--------|-------------|
| `thenApply(fn)` | Transform value (like `map`) |
| `thenCompose(fn)` | Chain another `CompletableFuture` (like `flatMap`) |
| `thenCombine(other, fn)` | Combine two futures when both complete |
| `allOf(cf1, cf2, ...)` | Complete when ALL complete (returns `CompletableFuture<Void>`) |
| `anyOf(cf1, cf2, ...)` | Complete when ANY completes |
| `orTimeout(duration)` | Java 9+ — completes exceptionally on timeout |
| `completeOnTimeout(value, duration)` | Java 9+ — completes with default on timeout |

*   **Thread pool control:** `supplyAsync(task, myExecutor)` — use a custom pool to avoid saturating the common `ForkJoinPool`.

### 7. `ForkJoinPool` — Work-Stealing
*   Each worker thread has its own **deque** of tasks.
*   When a worker's deque is empty, it **steals** from the tail of another worker's deque.
*   Used internally by **parallel streams** and `CompletableFuture.supplyAsync()`.

```java
class SumTask extends RecursiveTask<Long> {
    protected Long compute() {
        if (array.length <= THRESHOLD) return directSum();
        SumTask left = new SumTask(leftHalf);
        SumTask right = new SumTask(rightHalf);
        left.fork();           // Submit to deque
        Long rightResult = right.compute(); // Compute in current thread
        Long leftResult = left.join();      // Wait for forked task
        return leftResult + rightResult;
    }
}
ForkJoinPool pool = new ForkJoinPool(4); // 4 worker threads
long sum = pool.invoke(new SumTask(array));
```

### 8. `Phaser`
*   Generalization of `CyclicBarrier` + `CountDownLatch`.
*   **Dynamic registration:** Parties can register/deregister at any time.
*   Supports multiple **phases** (numbered 0, 1, 2, ...) — each phase ends when all registered parties arrive.

```java
Phaser phaser = new Phaser(1); // Self-registered
for (int i = 0; i < 3; i++) {
    phaser.register(); // Dynamic registration
    new Thread(() -> {
        phaser.arriveAndAwaitAdvance();   // Phase 0
        phaser.arriveAndAwaitAdvance();   // Phase 1
        phaser.arriveAndDeregister();     // Done, leave
    }).start();
}
phaser.arriveAndDeregister(); // Main deregisters
```

### 9. `Exchanger`
Two-thread rendezvous point where each thread offers data and receives the other's data.

```java
Exchanger<String> exchanger = new Exchanger<>();
// Thread A:  String fromB = exchanger.exchange("dataFromA");
// Thread B:  String fromA = exchanger.exchange("dataFromB");
```

### 10. `LockSupport`
Low-level parking/unparking primitives. Every other synchronizer is built on top of these.

```java
LockSupport.park();               // Suspends current thread (like wait, but no monitor needed)
LockSupport.unpark(thread);        // Wakes the specific thread (can be called BEFORE park!)
```

*   **Permits model:** `unpark` grants a permit; `park` consumes it. If `unpark` is called before `park`, the `park` returns immediately.

### 11. Virtual Threads (Java 21+ — Project Loom)
*   **Platform threads** = OS threads (expensive, limited to ~thousands).
*   **Virtual threads** = lightweight, JVM-managed, scheduled on a pool of carrier (platform) threads.
*   Ideal for **I/O-bound** workloads (HTTP servers, DB queries). NOT for CPU-bound tasks.

```java
// One virtual thread per task — millions are fine
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 1_000_000; i++) {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1)); // Does NOT block the carrier thread
            return fetchFromDb();
        });
    }
}

// Simpler:
Thread.startVirtualThread(() -> doWork());
```

*   **Pinning:** A virtual thread is **pinned** to its carrier when inside a `synchronized` block or a native (JNI) call. Prefer `ReentrantLock` over `synchronized` in virtual-thread code.
*   **ThreadLocal warning:** Each virtual thread gets its own `ThreadLocal`. With millions of VTs, memory usage explodes. Use `ScopedValue` (preview) instead.

### Interview Pro-Tip
**Question:** "When would you use `StampedLock` over `ReentrantReadWriteLock`?"
**Answer:** "When reads vastly outnumber writes AND you want maximum throughput. `StampedLock`'s optimistic read doesn't acquire any lock at all — it just validates after the read. This avoids reader contention entirely. However, `StampedLock` is not reentrant and doesn't support `Condition`, so it's unsuitable for complex locking protocols. For most applications, `ReentrantReadWriteLock` is simpler and safer."
