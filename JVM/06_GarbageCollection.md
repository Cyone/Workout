## How Java Cleans Up
Java provides automatic memory management. You allocate memory (via `new`), but you don't manually free it (like `malloc/free` in C).

### 1. The Algorithm: Mark and Sweep
1.  **Mark:** The GC starts from "GC Roots" (active threads, local variables on the stack, static variables). It traverses the object graph and marks every reachable object as "alive."
2.  **Sweep:** Any object in the Heap that was *not* marked is considered unreachable (garbage) and its memory is reclaimed.

### 2. Generational Strategy (The "Weak Generational Hypothesis")
Empirical evidence shows that **most objects die young**. Therefore, the Heap is divided into regions to optimize cleanup.

*   **Young Generation (Eden + Survivor Spaces):**
    *   New objects are born in **Eden**.
    *   When Eden fills up, a **Minor GC** occurs.
    *   Survivors move to **Survivor Spaces (S0/S1)**.
    *   Objects here are very cheap to collect.
*   **Old Generation (Tenured):**
    *   Objects that survive many Minor GCs are promoted to the Old Gen.
    *   This area is larger and fills up slower.
    *   Cleaning this triggers a **Major GC** (or Full GC), which is more expensive and often pauses the application ("Stop The World").

### 4. Advanced GC Concepts
*   **TLAB (Thread Local Allocation Buffer):** To avoid synchronization overhead during allocation, each thread is given its own small buffer in the Eden space. Allocation becomes a simple pointer bump, significantly boosting performance.
*   **Card Table / Remembered Sets:** To avoid scanning the entire Old Generation during a Minor GC, the JVM uses a "Card Table" (a bit set) to mark regions of the Old Gen that contain references to objects in the Young Gen.
*   **Tenuring Threshold:** The number of Minor GCs an object must survive before promotion to the Old Gen. Controlled via `-XX:MaxTenuringThreshold`.

### 5. Common GC Algorithms
*   **Serial GC:** Single-threaded. Good for small apps/client-side.
*   **Parallel GC:** Multiple threads for Minor GC. Good for throughput.
*   **G1 GC (Garbage First):** The default in modern Java (since Java 9). Breaks the heap into small regions and cleans the ones with the most garbage first. Great balance of throughput and latency.
*   **ZGC / Shenandoah:** Low-latency collectors designed to keep pause times under 10ms, even on multi-terabyte heaps. Uses "Colored Pointers" and "Load Barriers" to perform most work concurrently with the application.

### Interview Pro-Tip
**Question:** "Can you force Garbage Collection?"
**Answer:** "You can call `System.gc()`, but it is merely a *suggestion* to the JVM. The JVM is free to ignore it. You cannot guarantee immediate execution."
