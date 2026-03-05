# 06_Primitives_Wrappers.md

## Data Handling & Performance

### 1. Primitives (`int`, `double`, `boolean`)

* Stored on the Stack (usually).
* Contains pure binary data.
* Default value: `0` or `false`.
* **Performance:** Very fast. Uses minimal memory.

### 2. Wrappers (`Integer`, `Double`, `Boolean`)

* Stored on the Heap.
* Are actual Objects (metadata overhead).
* Can be `null` (useful for Databases/SQL where values can be missing).
* **Performance:** Slower due to object overhead and indirection.

### 3. Autoboxing & Unboxing

Java automatically converts between the two.

* `Integer x = 10;` (Autoboxing: `int` -> `Integer`)
* `int y = x;` (Unboxing: `Integer` -> `int`)
* **Risk:** Unboxing a `null` Wrapper throws a `NullPointerException`.

### 4. Advanced: Memory Layout & SIMD

* **Layout:** An array of `int` (`int[]`) is a contiguous block of memory, which is CPU cache-friendly. An array of `Integer` (`Integer[]`) is an array of **references**, which point to objects scattered on the heap. This causes **cache misses** and "pointer chasing."
* **SIMD (Single Instruction Multiple Data):** Modern CPUs can perform the same operation on multiple values at once (e.g., adding 4 integers in a single cycle). The JVM's HotSpot compiler can only effectively apply these optimizations to **primitive arrays**, making them significantly faster for high-performance computing.

### 5. Integer Cache

To save memory, Java caches `Integer` objects for values between **-128 and 127**.

```java
Integer a = 100;
Integer b = 100;
System.out.println(a ==b); // TRUE (Same cached object)

Integer c = 200;
Integer d = 200;
System.out.println(c ==d); // FALSE (New objects created outside cache range)
```

### Interview Pro-Tip

**Question:** "Why use `int` instead of `Integer`?"
**Answer:** "Performance and memory. An `int` takes 4 bytes. An `Integer` takes 16+ bytes (header + data + padding) and
requires heap access. However, `Integer` is required for Generic Collections like `List<Integer>` because Generics don't
support primitives yet."
