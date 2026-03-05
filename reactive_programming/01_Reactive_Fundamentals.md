# 1. Reactive Fundamentals

Before learning specific frameworks like RxJava or Project Reactor, you must understand the underlying paradigm shift. Reactive programming is not just a syntax change; it is a fundamental difference in how applications handle concurrency and resources.

## 1. The Reactive Manifesto
Reactive systems share four core characteristics:
1.  **Responsive:** The system responds in a timely manner if at all possible.
2.  **Resilient:** The system stays responsive in the face of failure (graceful degradation instead of total collapse).
3.  **Elastic:** The system stays responsive under varying workload (it can scale up and down dynamically).
4.  **Message-Driven:** Reactive systems rely on asynchronous message passing to establish boundaries between components, ensuring loose coupling, isolation, and location transparency.

## 2. The Traditional Paradigm: Thread-Per-Request (Tomcat)
In traditional Spring MVC (running on Tomcat):
*   When an HTTP request arrives, Tomcat assigns an entire OS thread to handle it.
*   If your code makes a database call (e.g., `userRepository.findById()`) or a REST call to another microservice, that thread **blocks**. It sits completely idle, doing nothing, holding onto RAM (usually ~1MB per thread), waiting for the network I/O to return.
*   **The Problem:** If you have 500 concurrent users performing slow database queries, your server needs 500 threads. Threads are wildly expensive to create and context-switch. Once you run out of threads, the server stops accepting new connections (Thread Pool Exhaustion).

## 3. The Reactive Paradigm: The Event Loop (Netty)
Reactive applications (like Spring WebFlux running on Netty) use a different model borrowed from Node.js:
*   A very small number of worker threads (usually exactly equal to the number of CPU cores) handle *all* requests.
*   When a request needs to make a database call, the thread **does not block**. It kicks off the network request and immediately grabs the next incoming HTTP request from the queue to start working on it.
*   When the database eventually responds, an event is fired, and a worker thread picks up where the previous one left off to finish formatting the HTTP response.
*   **The Advantage:** You can handle 10,000 concurrent long-polling connections using only 4 threads. You never waste RAM on blocked threads.

## 4. Backpressure (The Defining Feature)
*What happens if a database returns 1,000,000 rows faster than your application can process them?*
*   **Traditional Iterables (`Iterable<T>`):** The application runs out of memory (OOM crash) trying to hold the rows in RAM.
*   **Modern Reactive Systems (Publisher/Subscriber):** Introduce **Backpressure**. The Subscriber (your app logic) explicitly tells the Publisher (the database driver): *"I can only handle 10 items right now. Give me 10, and wait until I ask for more."*
*   This dynamic feedback mechanism ensures that fast producers do not overwhelm slow consumers, guaranteeing system resiliency.

## 5. Streams over Collections
*   **Collection:** All data is calculated, stored in memory, and *then* returned. 
*   **Stream (Reactive):** Data is processed and pushed identically over time. You might receive the first item immediately and the second item 10 seconds later. Reactive programming is about explicitly setting up *pipelines* (map, filter, reduce) that react to data as it arrives, rather than treating data as static blocks.
