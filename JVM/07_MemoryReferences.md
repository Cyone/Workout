# 03_Reference_Types.md

## Not All Links Are Created Equal
Standard Java code uses "Strong" references, but `java.lang.ref` provides distinct ways to interact with the Garbage Collector.

### 1. Strong Reference
*   **Code:** `StringBuilder sb = new StringBuilder();`
*   **Behavior:** As long as a strong reference points to an object, the GC **will never** collect it, even if it throws an OOM error.
*   **Use Case:** 99.9% of daily coding.

### 2. Soft Reference
*   **Code:** `SoftReference<StringBuilder> soft = new SoftReference<>(new StringBuilder());`
*   **Behavior:** The GC will only collect this object **if the JVM is running out of memory**.
*   **Use Case:** Perfect for **memory-sensitive caches**. If memory is abundant, keep the cache. If memory is tight, kill the cache to save the app.

### 3. Weak Reference
*   **Code:** `WeakReference<StringBuilder> weak = new WeakReference<>(new StringBuilder());`
*   **Behavior:** The GC will collect this object **as soon as it runs**, regardless of memory status, provided no Strong references exist.
*   **Use Case:** **Metadata association** (e.g., `WeakHashMap`). Useful when you want to store extra data about an object only as long as that object is being used elsewhere in the system.

### 4. Phantom Reference
*   **Behavior:** You cannot retrieve the object from a Phantom reference (get() returns null). It is used to determine exactly *when* an object has been removed from memory.
*   **Use Case:** Scheduling post-mortem cleanup actions (a more flexible alternative to the `finalize()` method).

### 5. Reference Queues & The Cleaner API
When a Soft, Weak, or Phantom reference is cleared by the GC, the reference object itself is appended to a **ReferenceQueue**. This allows the program to perform cleanup logic.

*   **Java 9+ Cleaner:** Replacing the deprecated `finalize()` method, the `java.lang.ref.Cleaner` provides a more efficient and less error-prone way to perform cleanup actions when an object becomes unreachable. Unlike finalizers, Cleaners don't allow "resurrection" of objects and run in a dedicated thread.

### Interview Pro-Tip
**Question:** "What is the difference between Weak and Soft references?"
**Answer:** "It's about eagerness. A Weak reference is collected aggressively (at the next GC). A Soft reference is collected reluctantly (only when memory is low)."
