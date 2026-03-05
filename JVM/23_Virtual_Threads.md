# 23_Virtual_Threads.md

## Virtual Threads (Project Loom) — OCP 1Z0-830 Deep Dive

Virtual Threads are the flagship Java 21 feature (GA after previews in 19 & 20) and a **new exam objective** in 1Z0-830.

---

### 1. The Problem Virtual Threads Solve

Traditional Java threads are **platform threads** — a 1:1 wrapper around an OS thread.

| | Platform Thread | Virtual Thread |
|---|---|---|
| Created by | JVM wrapping OS thread | JVM (Project Loom) |
| Stack size | ~1 MB (fixed OS stack) | ~1 KB (heap-backed, grows/shrinks) |
| Max practical count | ~few thousand | **Millions** |
| Blocking cost | Blocks entire OS thread | **Unmounts from carrier**, OS thread freed |
| Best for | CPU-bound work | **I/O-bound, high-concurrency** |

Classic blocking thread:
```
Request → Platform Thread blocked on DB query → OS thread idle, wasting memory
```

Virtual thread:
```
Request → Virtual Thread blocked on DB query → Unmounts, carrier thread free for other work
Request → Virtual Thread resumes when I/O completes → Mounts back on a carrier thread
```

---

### 2. Creating Virtual Threads

```java
// 1. Simple factory — fire and forget
Thread vt = Thread.ofVirtual().start(() -> System.out.println("Hello from VT"));

// 2. Builder — configure before starting
Thread vt2 = Thread.ofVirtual()
        .name("my-vt")
        .unstarted(() -> doWork());   // not started yet
vt2.start();

// 3. Thread factory — for use with ExecutorService and other APIs
ThreadFactory vtFactory = Thread.ofVirtual().factory();

// 4. Dedicated ExecutorService (PREFERRED for structured use)
try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
    exec.submit(() -> callDatabase());
    exec.submit(() -> callExternalApi());
}   // auto-closes and awaits all tasks

// 5. Equivalents — runnable style
Thread.startVirtualThread(() -> System.out.println("Quick way"));
```

---

### 3. Platform Thread vs Virtual Thread — Creation Syntax

```java
// Platform thread
Thread pt = Thread.ofPlatform().name("platform-t").start(() -> doWork());

// Virtual thread
Thread vt = Thread.ofVirtual().name("virtual-t").start(() -> doWork());

// Check at runtime
vt.isVirtual()   // true
pt.isVirtual()   // false
```

---

### 4. How Virtual Threads Work Internally

```
┌─────────────────────────────────────────────────────┐
│  JVM Scheduler (ForkJoinPool — "carrier threads")   │
│  Carrier threads = platform threads = CPU cores     │
└────────────────────┬────────────────────────────────┘
                     │  mount / unmount
         ┌───────────┴────────────┐
         ▼                        ▼
   Virtual Thread A         Virtual Thread B
   (blocking on DB)         (running on CPU)
   → unmounted from         → mounted on a
     carrier thread           carrier thread
```

- **Mount:** A virtual thread is scheduled onto a carrier (platform) thread to run.
- **Unmount:** When a virtual thread blocks (I/O, `sleep`, lock), it is *unmounted* — the carrier thread is freed to run other virtual threads.
- **Continuation:** The virtual thread's stack is stored on the heap as a `Continuation` object.
- **Carrier pool:** Defaults to `ForkJoinPool.commonPool()` (size = number of CPU cores).

---

### 5. What Blocks (Pins) a Virtual Thread to its Carrier

> This is the most important gotcha for the exam and real-world use.

A virtual thread **pins** (cannot unmount) when:

1. **`synchronized` block or method** — blocks the entire carrier thread.
2. **Native methods / JNI calls** — JVM cannot unmount.

```java
// PROBLEMATIC — synchronized pins the carrier thread
synchronized (lock) {
    Thread.sleep(1000);   // virtual thread cannot unmount during sleep here!
}

// FIXED — use ReentrantLock instead
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    Thread.sleep(1000);   // virtual thread can unmount normally
} finally {
    lock.unlock();
}
```

> Java 23 / 24 removes the `synchronized` pinning constraint, but for the Java 21 exam, `synchronized` causes pinning.

---

### 6. Virtual Threads and Thread-Local Variables

- Virtual threads **support** `ThreadLocal` — but using it with millions of virtual threads can create millions of separate copies → memory explosion.
- **Prefer `ScopedValue`** (Java 21 preview, Java 22+ standard) for scoped per-task data sharing.

```java
// ThreadLocal — works but scales poorly with VTs
ThreadLocal<String> tl = new ThreadLocal<>();
tl.set("value");      // a separate copy per virtual thread

// ScopedValue — Java 21 preview (FYI for awareness)
ScopedValue<String> sv = ScopedValue.newInstance();
ScopedValue.where(sv, "contextValue").run(() -> {
    System.out.println(sv.get());  // "contextValue"
});
```

---

### 7. Structured Concurrency (Preview in Java 21)

`StructuredTaskScope` provides a parent-child relationship for virtual threads — all child tasks are completed or cancelled before the scope exits.

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user   = scope.fork(() -> fetchUser(id));
    Future<Integer> order = scope.fork(() -> fetchOrder(id));

    scope.join();           // wait for both
    scope.throwIfFailed();  // propagate any exception

    return new Response(user.resultNow(), order.resultNow());
}
// scope closed → both tasks guaranteed complete or cancelled
```

> For the 1Z0-830 exam, awareness of `StructuredTaskScope` is sufficient; detailed API questions are unlikely.

---

### 8. What Does NOT Change with Virtual Threads

- **Thread API is the same.** `start()`, `join()`, `interrupt()`, `isAlive()` all work identically.
- **Exceptions work the same.** `InterruptedException` is thrown on interrupt.
- **Thread pools are NOT recommended for virtual threads.**

```java
// ANTI-PATTERN — never use a fixed thread pool with virtual threads
ExecutorService pool = Executors.newFixedThreadPool(10);  // pointless
// Virtual threads are cheap — one per task, always
ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();  // CORRECT
```

---

### 9. Practical Comparison

```java
// OLD: Thread pool limits concurrency artificially
ExecutorService pool = Executors.newFixedThreadPool(200);
for (int i = 0; i < 10_000; i++) {
    pool.submit(() -> callRemoteService());  // 9,800 tasks queued, waiting
}

// NEW: Virtual threads — 10,000 tasks run concurrently, no queuing
try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        exec.submit(() -> callRemoteService());  // all start immediately
    }
}
```

---

### 10. Exam Quick-Fire

| Question | Answer |
|----------|--------|
| Are virtual threads daemon threads? | **Yes** — always daemon threads |
| Can virtual threads have a name? | Yes — `.name("name")` or auto-named |
| What is the carrier thread pool? | `ForkJoinPool` (size = CPU cores by default) |
| What causes pinning in Java 21? | `synchronized` blocks and native/JNI calls |
| Is `Thread.sleep()` OK in a virtual thread? | Yes — it unmounts (doesn't block carrier) |
| What replaces thread pools for VTs? | `Executors.newVirtualThreadPerTaskExecutor()` |
| `vt.isVirtual()` returns? | `true` |
| Can you set virtual thread priority? | Ignored — VTs don't have OS-level priority |
| Best lock type with virtual threads? | `ReentrantLock` (not `synchronized`) |
| Are virtual threads in Java 21 GA or preview? | **GA (generally available)** |

---

### 11. Summary — When to Use What

| Scenario | Use |
|----------|-----|
| CPU-bound computation (no blocking) | Platform threads / `ForkJoinPool` |
| I/O-bound: HTTP calls, DB queries, file ops | **Virtual threads** |
| High-concurrency web server | **Virtual threads** |
| Shared mutable state with heavy synchronization | Platform threads (avoid `synchronized` pin) |
| Low-level OS thread control, priority, affinity | Platform threads |
