# 03_Concurrency_Gotchas.md

## Java Concurrency Gotchas — Trick Interview Questions

---

### Gotcha #1: `volatile` Does NOT Make `i++` Atomic
```java
private volatile int counter = 0;

// Thread 1 & Thread 2 both do:
counter++; // NOT ATOMIC!
```
**Why?** `counter++` is three operations: (1) read `counter`, (2) increment, (3) write back. `volatile` ensures visibility (every thread sees the latest value) but does NOT make compound operations atomic.

**Result:** Lost updates. Both threads read 5, both write 6, instead of 7.

**Fix:** Use `AtomicInteger.incrementAndGet()` or `synchronized`.

---

### Gotcha #2: Double-Checked Locking — The Classic Bug
```java
// BROKEN (pre-Java 5):
class Singleton {
    private static Singleton instance;
    static Singleton getInstance() {
        if (instance == null) {                    // 1st check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {             // 2nd check (with lock)
                    instance = new Singleton();     // PROBLEM: can be reordered!
                }
            }
        }
        return instance;
    }
}
```
**Why broken?** Without `volatile`, the JVM can reorder: (1) allocate memory, (2) assign reference to `instance`, (3) call constructor. Another thread sees non-null `instance` but uses an **uninitialized** object.

**Fix:** `private static volatile Singleton instance;` — `volatile` forbids reordering.

**Better fix:** Use the **enum singleton** or **holder class** pattern — no synchronization needed.

---

### Gotcha #3: Spurious Wakeups
```java
synchronized (lock) {
    if (condition == false) {  // BUG: use 'while', not 'if'
        lock.wait();
    }
    // condition may STILL be false here!
}
```
**Why?** The JVM spec allows `wait()` to return **without** `notify()` being called (spurious wakeup). Also, another thread may have changed the condition between `notify()` and this thread acquiring the lock.

**Fix:** Always use a `while` loop:
```java
synchronized (lock) {
    while (!condition) {
        lock.wait();
    }
}
```

---

### Gotcha #4: `Thread.stop()` is Deprecated and Dangerous
```java
thread.stop(); // NEVER DO THIS!
```
**Why?** `stop()` throws a `ThreadDeath` error that unlocks all monitors the thread holds, potentially leaving shared data in an **inconsistent state**. There is no safe way to force-stop a thread from outside.

**Correct approach:** Use a `volatile boolean` flag or `Thread.interrupt()`:
```java
volatile boolean running = true;
void run() {
    while (running) { /* work */ }
}
// To stop: running = false;
```

---

### Gotcha #5: Interrupted Flag Gets Swallowed
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // BUG: silently swallowing the interrupt!
    // Now the calling code doesn't know the thread was interrupted
}
```
**Fix:** Always either re-throw or restore the interrupt flag:
```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore the flag!
    throw new RuntimeException("Interrupted", e);
}
```

---

### Gotcha #6: `synchronized` Methods Don't Lock the Class
```java
class Counter {
    private int count = 0;
    synchronized void increment() { count++; }              // Locks 'this'
    static synchronized void staticMethod() { /* ... */ }   // Locks Counter.class
}
```
**Trap:** Instance `synchronized` methods lock `this`. Static `synchronized` methods lock the `Class<?>` object. They use **different locks** — an instance method and a static method can run concurrently!

---

### Gotcha #7: `notify()` vs `notifyAll()` — The Wrong Choice
```java
synchronized (lock) {
    lock.notify(); // Wakes up ONE arbitrary waiting thread
}
```
**Problem:** If multiple threads are waiting for *different conditions* on the same lock, `notify()` might wake a thread whose condition is still false. That thread goes back to waiting, and the thread with a satisfied condition stays asleep → **liveness failure**.

**Rule:** Prefer `notifyAll()` unless you are certain only one type of waiter exists.

---

### Gotcha #8: `deadlock` with Lock Ordering
```java
// Thread 1:
synchronized (lockA) { synchronized (lockB) { /* ... */ } }

// Thread 2:
synchronized (lockB) { synchronized (lockA) { /* ... */ } }  // OPPOSITE ORDER!
```
**Result:** Classic deadlock. Thread 1 holds A, waits for B. Thread 2 holds B, waits for A.

**Detection:** `jstack <pid>` — prints "Found one Java-level deadlock" with the full thread dumps.

**Prevention:** Always acquire locks in a **deterministic global order**.

---

### Gotcha #9: `ConcurrentHashMap.computeIfAbsent()` Can Deadlock
```java
ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
map.computeIfAbsent("key", k -> {
    return map.computeIfAbsent("key", k2 -> "value"); // DEADLOCK — recursive compute on same key
});
```
**Why?** `computeIfAbsent` holds the bucket lock while executing the mapping function. A recursive call to the same bucket will try to acquire the same lock → deadlock.

---

### Gotcha #10: `ExecutorService.submit()` Silently Swallows Exceptions
```java
ExecutorService pool = Executors.newFixedThreadPool(1);
pool.submit(() -> {
    throw new RuntimeException("Oops!"); // This exception DISAPPEARS!
});
// No error logged, no stack trace, nothing.
```
**Why?** `submit()` returns a `Future`. The exception is stored in the `Future` and only thrown when `future.get()` is called. If nobody calls `get()`, the exception is lost.

**Fixes:**
```java
// Fix 1: Use execute() for fire-and-forget with UncaughtExceptionHandler
pool.execute(() -> { throw new RuntimeException("Oops!"); });

// Fix 2: Always call future.get()
Future<?> f = pool.submit(() -> { throw new RuntimeException("Oops!"); });
f.get(); // Now the exception is thrown

// Fix 3: Wrap tasks with try-catch
```

---

### Gotcha #11: `ThreadLocal` Memory Leak in Thread Pools
```java
static final ThreadLocal<byte[]> cache = ThreadLocal.withInitial(() -> new byte[1024 * 1024]);

// In a thread pool (e.g., Tomcat), threads are REUSED.
// The ThreadLocal value persists across requests → 1MB leaked per thread.
```
**Fix:** Always call `cache.remove()` in a `finally` block after use.
