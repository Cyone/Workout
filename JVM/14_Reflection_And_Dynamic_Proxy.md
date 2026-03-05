# 14_Reflection_And_Dynamic_Proxy.md

## Looking Inside the JVM at Runtime

### 1. What is Reflection?
Reflection is the ability of code to **examine and modify its own structure and behavior at runtime** via `java.lang.reflect`. The `Class<?>` object is the entry point.

```java
Class<?> clazz = Class.forName("com.example.User"); // Load by name (used in frameworks)
Class<?> clazz = user.getClass();                    // From an instance
Class<?> clazz = User.class;                         // Literal (preferred if known at compile time)
```

### 2. Common Operations
```java
// Inspect
Method[] methods = clazz.getDeclaredMethods();  // All methods, including private
Field field = clazz.getDeclaredField("name");

// Invoke (with access override)
field.setAccessible(true);  // Bypass private! (Restricted in Java 9+ modules)
field.set(userInstance, "Alice");

// Instantiate dynamically
Constructor<?> ctor = clazz.getDeclaredConstructor(String.class);
Object obj = ctor.newInstance("Alice");
```

### 3. Performance Cost
Reflection is significantly slower than direct code because:
1.  **No JIT inlining** — the method call target isn't known at compile time.
2.  **Access checks** run on every `invoke()` call (partially solved by `setAccessible(true)`).
3.  Higher GC pressure from boxing/unboxing arguments.

**Rule:** Reflection belongs in framework code (Spring, Hibernate), not hot business logic loops.

### 4. Dynamic Proxy (`java.lang.reflect.Proxy`)
Creates a **proxy object at runtime** that implements given interfaces and intercepts all method calls.

```java
InvocationHandler handler = (proxy, method, args) -> {
    System.out.println("Before: " + method.getName());
    Object result = method.invoke(realService, args);
    System.out.println("After: " + method.getName());
    return result;
};

MyService proxy = (MyService) Proxy.newProxyInstance(
    MyService.class.getClassLoader(),
    new Class[]{MyService.class},
    handler
);
```

*   **Works only with interfaces** (unlike CGLIB which proxies classes via subclassing).
*   **Used by:** Spring AOP, JDK transaction proxies (`@Transactional`), `Mockito`.

### 5. `MethodHandles` — The Modern Alternative
Java 7+ `java.lang.invoke.MethodHandle` provides a reflection-like API that can be fully optimized by the JIT compiler.

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle get = lookup.findVirtual(String.class, "length", MethodType.methodType(int.class));
int len = (int) get.invoke("hello"); // JIT can inline this!
```

*   **Performance:** Near-direct-call speed after JIT warmup.
*   **Module-aware:** Respects Java 9 module boundaries (unlike raw reflection which required `--add-opens`).
*   Used internally by `invokedynamic` (which powers lambda expressions and `String::format`).

### 6. Annotations Processing
Annotations are metadata that can be read at runtime via reflection:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // Must be RUNTIME to be reflectively visible
public @interface Timed {}

// At runtime:
if (method.isAnnotationPresent(Timed.class)) { ... }
```

`RetentionPolicy.SOURCE` → only in source (e.g., `@Override`).
`RetentionPolicy.CLASS` → stored in bytecode but not available at runtime.
`RetentionPolicy.RUNTIME` → available via reflection (Spring, JPA annotations).

### Interview Pro-Tip
**Question:** "How does Spring's `@Autowired` work under the hood?"
**Answer:** "Spring scans the classpath for classes annotated with `@Component`/`@Service` etc. For each, it uses Reflection to inspect fields annotated with `@Autowired`, then injects the correct bean from the application context using `field.setAccessible(true)` followed by `field.set(beanInstance, dependency)`. JDK proxies or CGLIB are used to wrap beans for AOP features like `@Transactional`."
