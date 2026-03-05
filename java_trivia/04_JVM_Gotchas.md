# 04_JVM_Gotchas.md

## JVM Gotchas — Trick Interview Questions

---

### Gotcha #1: Class Loading Circular Dependency Deadlock
```java
class A {
    static final B b = new B(); // Loading A → needs B
}
class B {
    static final A a = new A(); // Loading B → needs A
}
```
**What happens?** If Thread 1 triggers loading of `A` and Thread 2 triggers loading of `B` simultaneously, each holds its class's initialization lock and waits for the other → deadlock.

**Note:** This deadlock doesn't show up in `jstack` as a "Java-level deadlock" because it involves JVM-internal initialization locks, not `synchronized` monitors.

**Fix:** Avoid circular dependencies between static initializers. Use lazy initialization.

---

### Gotcha #2: `OutOfMemoryError` in Metaspace
```java
// Common cause: classloader leak
while (true) {
    ClassLoader cl = new URLClassLoader(urls);
    Class<?> clazz = cl.loadClass("com.example.Dynamic");
    // cl is never GC'd because 'clazz' holds a reference to it
}
```
**Why?** Each loaded class references its ClassLoader. The ClassLoader references all its classes. If *anything* holds a strong reference to a loaded class, the entire ClassLoader AND all its classes stay in Metaspace → eventually `OutOfMemoryError: Metaspace`.

**Common in:** Application servers redeploying war files, OSGi, dynamic code generation without limits.

**Diagnose:** `-XX:+TraceClassLoading -XX:+TraceClassUnloading` or use `jmap -clstats <pid>`.

---

### Gotcha #3: `Phantom References` Don't Prevent GC
```java
Object obj = new Object();
ReferenceQueue<Object> queue = new ReferenceQueue<>();
PhantomReference<Object> phantom = new PhantomReference<>(obj, queue);

obj = null; // Object is now eligible for GC
System.gc();
// phantom.get() ALWAYS returns null — that's by design!
Reference<?> ref = queue.poll(); // Non-null after GC — the object was collected
```
**Unlike** `finalize()`, phantom references:
*   Don't prevent collection
*   Can't resurrect the object (`.get()` always returns `null`)
*   Are enqueued **after** the object is finalized and before memory is reclaimed
*   Used by `Cleaner` (Java 9+) for safe resource cleanup

---

### Gotcha #4: `StackOverflowError` in Recursive Lambdas
```java
// This works:
int factorial(int n) { return n <= 1 ? 1 : n * factorial(n - 1); }

// This DOESN'T (lambda can't reference itself by name):
Function<Integer, Integer> factorial = n -> n <= 1 ? 1 : n * factorial.apply(n - 1); // COMPILE ERROR
```
**Why?** The variable `factorial` might not be definitely assigned when the lambda body references it.

**Fix:** Use an array or method trick:
```java
Function<Integer, Integer>[] f = new Function[1];
f[0] = n -> n <= 1 ? 1 : n * f[0].apply(n - 1); // Works!
```

---

### Gotcha #5: Escape Analysis Isn't Guaranteed
```java
void doWork() {
    Point p = new Point(1, 2); // JVM MIGHT allocate this on stack via escape analysis
    int sum = p.x + p.y;
    // 'p' doesn't escape the method
}
```
**Gotcha:** Escape analysis is a **JIT optimization** — it only kicks in after the code is hot enough (thousands of invocations). In interpreted mode or with certain JVM flags, the object is always heap-allocated.

**Also:** Escape analysis is disabled or limited when:
*   The method is too large or complex
*   The object is stored in a field, array, or passed to another method that the JIT can't inline
*   Running with `-XX:-DoEscapeAnalysis`

---

### Gotcha #6: `System.gc()` is a Suggestion
```java
System.gc(); // Polite request. JVM is free to ignore it.
```
*   Not guaranteed to run GC immediately (or at all).
*   Some JVMs with `-XX:+DisableExplicitGC` silently ignore it.
*   Calling it in production is almost always wrong — the GC knows better when to run.

---

### Gotcha #7: Object Header Size
```java
Object obj = new Object(); // How much memory?
```
**Answer (64-bit JVM, compressed oops):**
*   Mark word: 8 bytes
*   Class pointer: 4 bytes (compressed)
*   Padding: 4 bytes (aligned to 8-byte boundary)
*   **Total: 16 bytes** — for an object with zero fields!

An `int[]` of length 0: 16 bytes header + 0 data = 16 bytes.
A `boolean` field class: 16 bytes header + 1 byte field + 7 bytes padding = 24 bytes.

---

### Gotcha #8: `String` Deduplication
```java
// With -XX:+UseStringDeduplication (G1 GC only):
String s1 = new String("hello");
String s2 = new String("hello");
// After GC cycle, both may share the same char[]/byte[] backing array
// but they remain DIFFERENT objects (s1 != s2 still)
```
**Note:** This is different from `intern()`. String deduplication only shares the backing array, not the `String` object itself. Enabled by default in some JDK builds with G1.

---

### Gotcha #9: NaN Comparisons in IEEE 754
```java
double nan = Double.NaN;
System.out.println(nan == nan);           // false! NaN is not equal to ANYTHING, including itself
System.out.println(nan != nan);           // true!
System.out.println(Double.isNaN(nan));   // true — the correct way to check

// In collections:
Set<Double> set = new HashSet<>();
set.add(Double.NaN);
set.contains(Double.NaN); // true — because HashSet uses Double.equals(), which handles NaN specially
```

---

### Gotcha #10: JIT Deoptimization
```java
// JIT compiles this hot method with aggressive optimizations (e.g., inlining, dead code elimination)
void process(Animal a) {
    a.move(); // JIT assumes only 'Dog' reaches here (monomorphic call site → inlined)
}

// Then at runtime, a 'Cat' appears for the first time!
process(new Cat()); // JIT must DEOPTIMIZE: throw away compiled code, recompile with polymorphic dispatch
```
**Why it matters:** Sudden latency spikes in production. The first request with a new subtype triggers deoptimization → interpreted execution until recompilation finishes.
