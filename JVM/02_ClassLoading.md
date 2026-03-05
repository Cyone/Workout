# ClassLoadingMechanisms.md

## How Java Loads Code
Unlike C++, where code is linked into a single executable, Java links classes dynamically at runtime. The **ClassLoader** is responsible for locating the `.class` files and loading them into the JVM's Metaspace.

### 1. The Class Loading Subsystem Phases
Loading is not a single step. It involves three distinct phases:

1.  **Loading:**
    *   Finds the binary representation of a class (the `.class` file) by its name.
    *   Creates a `Class` object in the Heap to represent it.
2.  **Linking:**
    *   **Verification:** Ensures the bytecode is valid and safe (prevents malicious code from crashing the JVM).
    *   **Preparation:** Allocates memory for *static* variables and initializes them to default values (e.g., `static int x = 0`).
    *   **Resolution:** Replaces symbolic references (like "String") with direct memory references.
3.  **Initialization:**
    *   This is where the actual code runs.
    *   Executes `static` blocks (`static { ... }`) and assigns initial values to static variables (e.g., `static int x = 10;`).

### 2. The Delegation Hierarchy (Parent-First)
Java uses a "Parent-First" delegation model to ensure security and prevent core classes from being overridden.

1.  **Bootstrap ClassLoader:**
    *   The root loader. Written in C/C++.
    *   Loads core Java libraries (`rt.jar`, `java.lang.*`).
2.  **Platform ClassLoader (formerly Extension):**
    *   Loads platform-specific modules/extensions.
3.  **Application (System) ClassLoader:**
    *   Loads classes from your `-classpath` or `CLASSPATH` environment variable.
    *   This is where your code lives.

**Algorithm:** When `AppClassLoader` is asked to load `java.lang.String`, it typically delegates to the `Platform` loader, which delegates to `Bootstrap`. `Bootstrap` finds it and returns it. If you try to create your own `java.lang.String`, the Bootstrap loader will find the real one first, preventing your malicious version from loading.

### 3. Common Errors (The "Class Not Found" Nightmare)
*   **ClassNotFoundException:**
    *   **When:** Explicitly thrown when you try to load a class by name (e.g., `Class.forName("com.missing.Helper")`) and it's not on the classpath.
    *   **Meaning:** "I looked for the file you asked for, but it's not there."
*   **NoClassDefFoundError:**
    *   **When:** Implicitly thrown. Your code compiles fine, but at runtime, the JVM tries to link a class that was present during compilation but is missing now.
    *   **Meaning:** "I was successfully compiling against this class earlier, but now that I'm running, I can't find its definition anymore."

### Interview Pro-Tip
**Question:** "Can you load two classes with the exact same name?"
**Answer:** "Yes, absolutely. A class is uniquely identified by its **Fully Qualified Name + The ClassLoader that loaded it**. If you have two different custom ClassLoaders, they can both load `com.example.MyClass` independently. This is exactly how Web Containers (like Tomcat) allow multiple web apps to run side-by-side using different versions of the same library without conflict."
