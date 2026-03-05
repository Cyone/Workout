# 11_Concurrency_Threads.md

## Threads, Pools, and Safety

### 1. Thread Lifecycle
A thread can be in one of these states (from `Thread.State` enum):

| State | Description |
|-------|-------------|
| `NEW` | Created but not yet started (`new Thread()`). |
| `RUNNABLE` | Running or ready to run (scheduler decides). |
| `BLOCKED` | Waiting to acquire a monitor lock (`synchronized`). |
| `WAITING` | Indefinitely waiting (called `wait()`, `join()`, `LockSupport.park()`). |
| `TIMED_WAITING` | Waiting with a timeout (`sleep(ms)`, `wait(ms)`). |
| `TERMINATED` | Finished execution. |

**Interview Trap:** A thread in `RUNNABLE` may not be actually running — the OS scheduler controls that.

### 2. `Runnable` vs. `Callable`
*   **`Runnable`:** `run()` returns `void`. Cannot throw checked exceptions.
*   **`Callable<T>`:** `call()` returns a value `T`. Can throw checked exceptions. Used with `ExecutorService.submit()`, returns a `Future<T>`.

### 3. Thread Pools (`ExecutorService`)
Creating raw `Thread` objects is expensive and uncontrolled. Use `Executors`:

```java
ExecutorService pool = Executors.newFixedThreadPool(4); // 4 worker threads
Future<String> future = pool.submit(() -> "hello");
String result = future.get(); // Blocks until done
pool.shutdown(); // ALWAYS shut down when done
```

*   **`newFixedThreadPool(n)`:** Fixed worker count. Good for known-concurrency workloads.
*   **`newCachedThreadPool()`:** Creates threads on demand, reuses idle ones. Risk: unbounded under load.
*   **`newSingleThreadExecutor()`:** Guarantees sequential execution.
*   **`newScheduledThreadPool(n)`:** Runs tasks with `schedule()` or `scheduleAtFixedRate()`.

### 4. `synchronized` vs. `ReentrantLock`

| Feature | `synchronized` | `ReentrantLock` |
|---------|----------------|-----------------|
| Auto-release | Yes (leaves block) | No — **must** call `unlock()` in `finally` |
| Interruptible lock attempt | No | Yes (`lockInterruptibly()`) |
| Fairness policy | No | Yes (`new ReentrantLock(true)`) |
| Try-acquire without blocking | No | Yes (`tryLock()`) |
| Multiple conditions | One implicit | Multiple (`newCondition()`) |

**Rule:** Prefer `synchronized` unless you need advanced features.

### 5. `ThreadLocal`
Each thread gets its own private copy of a value — no synchronization needed.

```java
private static final ThreadLocal<DateFormat> formatter =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
```

*   **Use Case:** Per-thread context (e.g., user session in a web server, database connection in a thread-pool).
*   **Risk:** **Memory Leak** in thread pools. Since threads are reused, the `ThreadLocal` value persists. Always call `threadLocal.remove()` when done.

### 6. Deadlock
Occurs when two or more threads block forever, each waiting for a resource held by the other.

**Classic Example:**
```
Thread 1 holds Lock A, waits for Lock B.
Thread 2 holds Lock B, waits for Lock A.
```

**Conditions (all 4 must hold):** Mutual Exclusion, Hold & Wait, No Preemption, Circular Wait.

**Prevention:** Always acquire locks in the same global order. Use `tryLock()` with a timeout.

**Detection:** `jstack <pid>` — it explicitly reports deadlocks with `"Found one Java-level deadlock"`.

### 7. `CountDownLatch` vs `CyclicBarrier` vs `Semaphore`

*   **`CountDownLatch(n)`:** One-shot. Threads wait until `countDown()` is called `n` times. *(e.g., wait for all services to initialize)*
*   **`CyclicBarrier(n)`:** Reusable. All `n` threads must reach the barrier together before any proceeds. *(e.g., parallel computation phases)*
*   **`Semaphore(n)`:** Controls access to a resource pool. `acquire()` decrements; `release()` increments. *(e.g., limit concurrent DB connections to 10)*

### Interview Pro-Tip
**Question:** "What is the difference between `wait()` and `sleep()`?"
**Answer:** "Both pause a thread, but `sleep()` is a `Thread` static method — it pauses without releasing any locks. `wait()` is an `Object` method — it **releases the monitor lock** and puts the thread into WAITING until another thread calls `notify()`. Therefore `wait()` must always be called inside a `synchronized` block."
