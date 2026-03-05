# 01_JVM_Memory_Structure.md

## The Big Picture: Stack vs. Heap
The most fundamental concept to understand is where data lives.

### 1. The Stack (Thread-Specific)
*   **What it is:** A LIFO (Last-In-First-Out) structure.
*   **Scope:** Each Thread has its own Stack. It is **not** shared between threads.
*   **Content:**
    *   **Stack Frames:** Every time a method is called, a new "Frame" is pushed onto the stack. When the method finishes, the frame is popped.
    *   **Local Primitives:** `int`, `boolean`, `double`, etc., declared inside methods live here.
    *   **Reference Variables:** If you have `Employee e = new Employee();` inside a method, the variable `e` (the remote control) is on the Stack, but the actual `Employee` object is on the Heap.
*   **Error:** If you recurse too deeply, you get a `StackOverflowError`.

### 2. The Heap (Shared)
*   **What it is:** A massive area of memory for dynamic data.
*   **Scope:** Shared by all threads (global).
*   **Content:** **All Objects**. Even if an object is small, if it is created with `new`, it lives here.
*   **Management:** Managed by the Garbage Collector.
*   **Error:** If you create too many objects without releasing them, you get an `OutOfMemoryError` (OOM).

### 3. Advanced Memory Optimizations
*   **Compressed OOPs (Ordinary Object Pointers):** On 64-bit JVMs, pointers are 64 bits. However, the JVM can represent them as 32-bit offsets to save space (up to 32GB heap), effectively reducing memory footprint and cache misses.
*   **Object Header Layout:** Every object on the heap has a header consisting of:
    *   **Mark Word:** Stores synchronization state (locks), hashcode, and GC metadata.
    *   **Class Metadata Address:** Points to the object's class definition in Metaspace.
*   **Off-Heap Memory (Direct Buffers):** Using `java.nio.ByteBuffer.allocateDirect(size)`, memory is allocated outside the Heap. This is managed manually (not by GC) and is used for zero-copy I/O operations (e.g., in Netty).

### 4. Metaspace (The "Method Area")
*   *Note: In Java 8, this replaced the "PermGen" (Permanent Generation).*
*   **Content:** It stores class metadata (definitions of Classes, Methods, static variables).
*   **Location:** It uses native memory (outside the Heap). Its size is limited only by the available RAM unless capped via `-XX:MaxMetaspaceSize`.

### Interview Pro-Tip
**Question:** "Where is a local variable stored?"
**Answer:** "If it's a primitive, it's on the Stack inside the active frame. If it's an object reference, the reference variable is on the Stack, but the object itself is on the Heap. *Exception:* If the JVM's **Escape Analysis** determines the object never leaves the method, it might allocate it on the Stack for better performance."
