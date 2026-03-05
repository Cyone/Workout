# JavaMemoryModelAndConcurrency.md

## The Rules of Threads
The Java Memory Model (JMM) defines how threads interact with memory. It's the "contract" between the JVM and your CPU.

### 1. Visibility and the JMM
Threads do not just share one big memory block. Each CPU core has its own **local cache**.
*   **Problem:** If Thread A writes to `x`, it might only update its *local cache*. Thread B (on another core) might still see the old value of `x` from main memory.
*   **Solution:** The JMM guarantees **Visibility** under specific conditions (the "Happens-Before" relationship).

### 2. The `volatile` Keyword
*   **What it does:** Ensures that any read or write to a `volatile` variable goes directly to **Main Memory**.
*   **Happens-Before:** Writing to a `volatile` variable establishes a "happens-before" relationship with subsequent reads of that same variable. This means any write by Thread A is visible to Thread B immediately.
*   **Use Case:** Status flags (e.g., `volatile boolean running = true;`).
*   **Limit:** It does **not** guarantee atomicity (e.g., `count++` is NOT safe with just `volatile`).

### 3. Synchronization (Under the Hood)
When you use `synchronized(obj)`, the JVM uses the object's **Monitor**.
*   **Mark Word:** Every object header has a "Mark Word" that stores lock information.
*   **Biased Locking:** Optimizes for single-threaded access. The lock "biases" towards the first thread. No real locking overhead unless contention occurs.
*   **Lightweight Locking:** Uses Compare-And-Swap (CAS) operations to acquire the lock without blocking the thread.
*   **Heavyweight Locking:** If contention is high, the JVM inflates the lock to a full "Mutex" (operating system mutex). This puts waiting threads to sleep (context switch), which is expensive.

### 4. Instruction Reordering
Compilers and CPUs reorder instructions for performance.
*   **Example:** Code: `a = 1; b = 2;` -> CPU might execute `b = 2; a = 1;`.
*   **Risk:** In single-threaded code, this is fine. In multi-threaded code, another thread might see `b` as 2 while `a` is still 0!
*   **Memory Barriers:** The JVM inserts special CPU instructions (barriers) around `volatile` and `synchronized` blocks to prevent reordering across them.

### Interview Pro-Tip
**Question:** "What is the difference between `volatile` and `AtomicInteger`?"
**Answer:** "`volatile` guarantees **visibility** (reading the latest value) but not **atomicity** for compound operations like incrementing. `AtomicInteger` uses hardware-level CAS (Compare-And-Swap) instructions to guarantee both visibility and atomicity without using locks. Use `volatile` for flags, `AtomicInteger` for counters."
