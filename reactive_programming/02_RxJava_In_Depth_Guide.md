# 2. RxJava In-Depth Guide

RxJava is the Java implementation of the ReactiveX (Reactive Extensions) API. While Spring has largely moved to Project Reactor for its ecosystem, RxJava remains massive in Android development and many older backend microservices.

## 1. The Core Types

RxJava 2/3 distinguishes types strictly based on how many items they emit and whether they support backpressure.

### 1. `Observable` (Zero to N Items - NO Backpressure)
*   **Use case:** Emits a stream of asynchronous data.
*   **Limitation:** It does NOT support backpressure. If the source emits 10,000 clicks per second and the observer takes 1 second to process each click, the `Observable` will eventually crash the app with a `MissingBackpressureException` (or run out of memory).
*   **Best for:** UI events (button clicks, mouse movements) where events can't be throttled at the source.

### 2. `Flowable` (Zero to N Items - WITH Backpressure)
*   **Use case:** Exactly like `Observable`, but it implements the Reactive Streams standard. The consumer can tell the producer how many items it is ready to receive.
*   **Best for:** Reading large files, querying databases (where the DB driver can throttle extraction), or consuming Kafka topics where the consumer shouldn't be overwhelmed.

### 3. `Single` (Exactly 1 Item or 1 Error)
*   **Use case:** Represents a task that returns a single value. It is the reactive equivalent of a standard method return or a Java `CompletableFuture`.
*   **Example:** Making an HTTP request to get User Details (`Single<User>`). It will fire `onSuccess()` with the User, or `onError()` with an exception.

### 4. `Maybe` (0 or 1 Item or Error)
*   **Use case:** Like `Single`, but the item might not exist. It fires `onSuccess(item)`, `onComplete()` (if empty), or `onError()`.
*   **Example:** Querying a database for a user by ID (`Maybe<User>`).

### 5. `Completable` (No Item, just Completion/Error)
*   **Use case:** A task that runs and finishes, but yields no result type. Equivalent to a `void` or `Unit` returning method.
*   **Example:** A database `UPDATE` query (`Completable`). It either fires `onComplete()` (success) or `onError()`.

## 2. Managing Concurrency (Schedulers)

By default, RxJava is **synchronous**. If you create an Observable and subscribe to it, the entire pipeline executes on the *main thread* (or whatever thread called `.subscribe()`). To achieve concurrency, you must explicitly shift work to other threads using Schedulers.

### The Core Schedulers
*   **`Schedulers.io()`:** Backed by an unbounded thread pool (it creates threads as needed and caches them). This is the workhorse. **Use this for blocking I/O** like network calls, reading files, or connecting to legacy synchronous JDBC databases.
*   **`Schedulers.computation()`:** Backed by a fixed thread pool sized to exactly the number of CPU cores. **Use this for intense CPU work** (image parsing, complex math, heavy JSON transformations). Never use this for I/O; if all 4 threads block on network calls, your whole app halts.
*   **`Schedulers.single()`:** A single backing thread. Useful for strictly sequential operations or accessing thread-unsafe resources.

### The Operators: `subscribeOn` vs. `observeOn`

This is the most common interview trap regarding RxJava.

*   **`subscribeOn(Scheduler)`:** Dictates which thread the *source* of the Observable starts doing its work on (e.g., the thread that connects to the database). The operator's position in the chain doesn't matter; it applies upstream to the very top.
*   **`observeOn(Scheduler)`:** Dictates which thread all the operators *downstream* from this point will execute on. This is where you switch threads mid-pipeline.

**Example Scenario:**
```java
// We want to fetch data on a background thread, but format it on the main UI thread.
getUserFromSlowDatabase()                  // Returns Single<User>
    .subscribeOn(Schedulers.io())          // 1. Database query runs on an I/O background thread
    .map(user -> generateComplexHash(user))// 2. Map still runs on the I/O thread
    .observeOn(Schedulers.computation())   // 3. Switch! Downstream operators move to a CPU thread
    .map(hash -> encrypt(hash))            // 4. Encrypt runs fast on the Computation thread
    .observeOn(AndroidSchedulers.mainThread()) // 5. Switch! Move back to UI thread
    .subscribe(result -> display(result)); // 6. Display runs on Main Thread safely
```
