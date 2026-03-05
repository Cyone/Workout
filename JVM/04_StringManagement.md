# 04_String_Pool.md

## Why Strings are Special
Strings are the most used object in Java, so the JVM optimizes them heavily.

### 1. Immutability
*   `String` objects are immutable. Once created, their internal character array (or byte array in modern Java) cannot be changed.
*   **Why?**
    *   **Security:** (e.g., Database passwords, network connections).
    *   **Thread Safety:** Immutable objects are automatically thread-safe.
    *   **HashCode Caching:** Since it can't change, the hash can be calculated once and cached, making Strings great HashMap keys.

### 2. The String Constant Pool (Interning)
The Heap contains a special area called the String Pool.

*   **Literals:** `String s1 = "Hello";` -> The JVM checks the pool. If "Hello" exists, `s1` points to it. If not, it creates it in the pool.
*   **New Keyword:** `String s2 = new String("Hello");` -> This forces the creation of a **new** object on the standard Heap, even if "Hello" is in the pool. *Avoid this usually.*

### 3. Advanced String Optimizations
*   **Compact Strings (Java 9+):** Previously, Strings used a `char[]` (UTF-16), taking 2 bytes per character even for ASCII. Modern Java uses a `byte[]` plus an encoding flag (Latin-1 or UTF-16). This reduces String memory usage by up to **50%** for many applications.
*   **String Deduplication (G1 GC):** If enabled via `-XX:+UseStringDeduplication`, the G1 collector scans the heap for Strings with identical content but different underlying `byte[]`. It replaces the duplicates with a single shared array to save memory.

### 4. String vs. StringBuilder vs. StringBuffer
*   **String:** Immutable. Modifying it creates a new object. Slow for concatenation loops.
*   **StringBuilder:** Mutable. Fast. **Not** thread-safe. (Use this for local string manipulation).
*   **StringBuffer:** Mutable. Thread-safe (synchronized methods). Slower than StringBuilder. (Legacy, rarely used now).

### Interview Pro-Tip
**Question:**
```java
String a = "Java";
String b = "Java";
String c = new String("Java");
System.out.println(a == b);
System.out.println(a == c);
```
**Answer:** `a == b` is `true` because both point to the same object in the **String Pool**. `a == c` is `false` because `new String()` forced a new object on the heap, bypassing the pool.
