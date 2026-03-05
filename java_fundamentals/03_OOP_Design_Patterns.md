# Java OOP Patterns Guide

An exhaustive list of OOP patterns commonly used in Java development, categorized by their primary purpose.

## 1. Creational Patterns
These patterns deal with object creation mechanisms, trying to create objects in a manner suitable to the situation.

*   **Singleton:** Ensures a class has only one instance and provides a global point of access to it.
    *   *Java Context:* `java.lang.Runtime#getRuntime()`, Spring Beans (default scope).
*   **Builder:** Separates the construction of a complex object from its representation, allowing the same construction process to create different representations.
    *   *Java Context:* `java.lang.StringBuilder`, `Stream.Builder`, generic implementation via Lombok's `@Builder`.
*   **Factory Method:** Defines an interface for creating an object, but lets subclasses decide which class to instantiate.
    *   *Java Context:* `java.util.Calendar#getInstance()`, `java.nio.charset.Charset#forName()`.
*   **Abstract Factory:** Provides an interface for creating families of related or dependent objects without specifying their concrete classes.
    *   *Java Context:* `javax.xml.parsers.DocumentBuilderFactory`, JDBC Connection implementations.
*   **Prototype:** Creates new objects by copying an existing object, known as the prototype.
    *   *Java Context:* `java.lang.Object#clone()`, `java.util.ArrayList` (shallow copy constructor).

## 2. Structural Patterns
These patterns explain how to assemble objects and classes into larger structures while keeping these structures flexible and efficient.

*   **Adapter:** Allows objects with incompatible interfaces to collaborate.
    *   *Java Context:* `java.util.Arrays#asList()` (adapts an array to the List interface), `InputStreamReader` (adapts a Stream to a Reader).
*   **Decorator:** Attaches new behaviors to objects by placing these objects inside special wrapper objects that contain the behaviors.
    *   *Java Context:* The `java.io` package is the classic example (e.g., `new BufferedReader(new FileReader(file))`).
*   **Proxy:** Provides a placeholder for another object to control access to it.
    *   *Java Context:* Java Reflection API (`java.lang.reflect.Proxy`), Hibernate Lazy Loading, Spring `@Transactional` (uses CGLIB or JDK dynamic proxies).
*   **Facade:** Provides a simplified interface to a library, a framework, or any other complex set of classes.
    *   *Java Context:* SLF4J (Simple Logging Facade for Java) which acts as a facade for Logback/Log4j.
*   **Composite:** Composes objects into tree structures and then works with these structures as if they were individual objects.
    *   *Java Context:* `java.awt.Container` and Component, JSF widgets.
*   **Flyweight:** Lets you fit more objects into the available amount of RAM by sharing common parts of state between multiple objects instead of keeping all of the data in each object.
    *   *Java Context:* `java.lang.Integer#valueOf(int)` (caches integers from -128 to 127), String Constant Pool.
*   **Bridge:** Splits a large class or a set of closely related classes into two separate hierarchies—abstraction and implementation—which can be developed independently.
    *   *Java Context:* JDBC (Java Database Connectivity) – separation between the generic API and the specific driver implementations.

## 3. Behavioral Patterns
These patterns are concerned with algorithms and the assignment of responsibilities between objects.

*   **Strategy:** Defines a family of algorithms, puts each of them into a separate class, and makes their objects interchangeable.
    *   *Java Context:* `java.util.Comparator` passed to `Collections.sort()`.
*   **Observer:** Defines a subscription mechanism to notify multiple objects about any events that happen to the object they're observing.
    *   *Java Context:* `java.util.EventListener` (Swing), JMS (Java Message Service) listeners.
*   **Template Method:** Defines the skeleton of an algorithm in the superclass but lets subclasses override specific steps of the algorithm without changing its structure.
    *   *Java Context:* `javax.servlet.http.HttpServlet` (`doGet`, `doPost`), `java.util.AbstractList`.
*   **Chain of Responsibility:** Passes requests along a chain of handlers. Upon receiving a request, each handler decides either to process the request or to pass it to the next handler in the chain.
    *   *Java Context:* Servlet Filters (`javax.servlet.Filter`), Spring Security filter chains, Try-Catch blocks.
*   **Command:** Turns a request into a stand-alone object that contains all information about the request.
    *   *Java Context:* `java.lang.Runnable`, Action Listeners in UI frameworks.
*   **Iterator:** Lets you traverse elements of a collection without exposing its underlying representation.
    *   *Java Context:* `java.util.Iterator`, `java.util.Enumeration`.
*   **State:** Lets an object alter its behavior when its internal state changes.
    *   *Java Context:* Iterator implementations often change state (hasMore vs empty), JSF Lifecycle.
*   **Visitor:** Lets you separate algorithms from the objects on which they operate.
    *   *Java Context:* `java.nio.file.FileVisitor` and `Files.walkFileTree`.
*   **Mediator:** Reduces chaotic dependencies between objects. The mediator restricts direct communications between the objects and forces them to collaborate only via a mediator object.
    *   *Java Context:* `java.util.Timer` (schedules background tasks).
*   **Memento:** Lets you save and restore the previous state of an object without revealing the details of its implementation.
    *   *Java Context:* `java.util.Date` (internal time representation), Serialization.

## 4. Enterprise / J2EE Patterns
These are specific patterns often found in Java Enterprise applications (Spring Boot, Jakarta EE).

*   **Dependency Injection (DI) / Inversion of Control (IoC):** A specific form of the Strategy pattern where the implementation is provided (injected) by an external framework/container rather than created by the client.
*   **Data Access Object (DAO) / Repository:** Abstracts the retrieval of data from a data store (database).
*   **Data Transfer Object (DTO):** An object that carries data between processes (e.g., between the backend API and the frontend) to reduce the number of method calls.
*   **Front Controller:** A controller that handles all requests for a website.
    *   *Context:* Spring's `DispatcherServlet`.
*   **Intercepting Filter:** Used to preprocess or post-process a request/response.
    *   *Context:* Servlet Filters.

## 5. Concurrency Patterns
Given Java's strong threading capabilities, these are highly relevant.

*   **Producer-Consumer:** Decouples the service creating data from the service consuming it.
    *   *Context:* `java.util.concurrent.BlockingQueue`.
*   **Future / Promise:** Represents the result of an asynchronous computation.
    *   *Context:* `java.util.concurrent.Future`, `CompletableFuture`.
*   **Thread Pool (Object Pool):** Manages a pool of worker threads.
    *   *Context:* `ExecutorService`.
