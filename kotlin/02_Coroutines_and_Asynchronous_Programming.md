# Coroutines and Asynchronous Programming

Coroutines are Kotlin's paradigm for handling massive concurrency without architectural complexity. They enable "structured concurrency" and simplify asynchronous, non-blocking code.

## 1. What is a Coroutine?
*   **Not a Thread:** A coroutine is a lightweight, suspendable computation. It is *not* bound to a specific thread.
*   **"Lightweight Threads":** You can have a thread pool of 4 Java UI Threads but run 100,000 coroutines concurrently on them.
*   **Suspension:** When a coroutine hits an I/O operation (like calling a database), it "suspends." It vacates the underlying OS thread, allowing another coroutine to run on that thread. Once the I/O returns, the original coroutine resumes, potentially on a completely different thread.

### Coroutines vs Project Loom (Java Virtual Threads)
*   **Virtual Threads (Java 21):** Implemented in the JVM layer. The runtime automatically intercepts blocking operations (e.g., `Thread.sleep` or network calls) and yields the underlying OS carrier thread to another virtual thread.
*   **Coroutines (Kotlin):** Implemented via compiler transformations and library support. Kotlin rewrites your `suspend` functions into state machines (CPS - Continuation Passing Style) at compile-time.

## 2. The `suspend` Keyword
You mark a function `suspend` if it can pause the coroutine it's running in.

*   `suspend` functions can only be called from:
    1.  Other `suspend` functions.
    2.  A coroutine builder (like `launch` or `async`).
*   **The State Machine (Under the Hood):** The compiler transforms the `suspend` function. It creates an implicit parameter called `Continuation` (which remembers the function's state, local variables, and where to resume). The function's internal structure is converted into a `switch` statement, breaking the code at every suspension point.

## 3. Coroutine Builders
*   **`launch`:** Fire and forget. It starts a new coroutine but returns a `Job` (does not return a result). Useful for background analytics or triggers.
*   **`async`:** Starts a new coroutine and returns a `Deferred<T>` (Kotlin's equivalent of `Future`/`Promise`). You call `.await()` on to retrieve the result. It forces you to handle suspension properly.
    *   *Interviw Code Pattern:* To make two independent HTTP calls concurrently, use `async` on both, then `await()` on the combined results. Do not call `await` immediately after `async` or they execute sequentially.
*   **`runBlocking`:** Brings the synchronous world into the asynchronous world. It starts a coroutine and *blocks the calling thread* until the coroutine completes. Used to bridge `main()` functions or tests to coroutines. Rarely used in production code paths.

## 4. Dispatchers
A defining aspect of coroutines is explicitly setting execution context.

*   **`Dispatchers.Default`:** Backed by a thread pool equal to the number of CPU cores. Use for CPU-intensive calculations (e.g., parsing huge JSON trees, image processing).
*   **`Dispatchers.IO`:** Backed by a large thread pool (default 64 threads). Use for offloading blocking I/O (Database queries, REST API calls). The threads here spend most of their time waiting.
*   **`Dispatchers.Main`:** Typically UI framework specific (Android/JavaFX).
*   **`Dispatchers.Unconfined`:** Starts in the caller's context but resumes wherever the suspending function finished. (Rarely used, hard to trace).

## 5. Structured Concurrency
Before Coroutines, if you spawn 10 background threads and the main parent process crashes, those 10 threads might leak or run endlessly.

Structured concurrency defines a rigid hierarchy. 
*   Every coroutine must be launched within a `CoroutineScope`.
*   A parent scope cannot complete until all its children complete.
*   If the parent is cancelled or throws an exception, all child coroutines are automatically and recursively cancelled.
*   If a child fails, it propagates the exception up, cancelling its siblings and the parent. This guarantees no leaked resources.
