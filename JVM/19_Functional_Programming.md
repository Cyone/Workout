# 19_Functional_Programming.md

## Java Functional Programming — Streams, Lambdas, Optional

### 1. Lambda Expressions & Functional Interfaces
A lambda is an anonymous implementation of a **functional interface** (exactly one abstract method).

```java
// These are all equivalent:
Comparator<String> c1 = (a, b) -> a.length() - b.length();
Comparator<String> c2 = Comparator.comparingInt(String::length);
```

**Core Functional Interfaces (`java.util.function`):**

| Interface | Method | Signature |
|-----------|--------|-----------|
| `Function<T, R>` | `apply(T)` | `T → R` |
| `BiFunction<T, U, R>` | `apply(T, U)` | `(T, U) → R` |
| `Predicate<T>` | `test(T)` | `T → boolean` |
| `Consumer<T>` | `accept(T)` | `T → void` |
| `Supplier<T>` | `get()` | `() → T` |
| `UnaryOperator<T>` | `apply(T)` | `T → T` (extends `Function<T,T>`) |
| `BinaryOperator<T>` | `apply(T, T)` | `(T, T) → T` (extends `BiFunction<T,T,T>`) |

**Primitive specializations** avoid boxing: `IntPredicate`, `LongFunction<R>`, `ToIntFunction<T>`, etc.

### 2. Method References

| Type | Syntax | Lambda Equivalent |
|------|--------|-------------------|
| Static | `Integer::parseInt` | `s -> Integer.parseInt(s)` |
| Bound instance | `str::toLowerCase` | `() -> str.toLowerCase()` |
| Unbound instance | `String::toLowerCase` | `s -> s.toLowerCase()` |
| Constructor | `ArrayList::new` | `() -> new ArrayList<>()` |

### 3. Lambda Capture Semantics
*   Lambdas can capture **effectively final** local variables (value doesn't change after assignment).
*   Captured variables are **copied** — the lambda doesn't hold a reference to the stack variable.
*   **Mutable state:** Lambdas *can* read and modify fields and array elements (heap-allocated), but this is thread-unsafe.

```java
int count = 0;
list.forEach(x -> count++); // COMPILE ERROR — count is not effectively final
int[] counter = {0};
list.forEach(x -> counter[0]++); // Compiles (array ref is final), but NOT thread-safe!
```

### 4. Stream API
Streams are **lazy**, **single-use** pipelines. No data is processed until a terminal operation is invoked.

```java
List<String> result = employees.stream()
    .filter(e -> e.getSalary() > 50_000)          // Intermediate (lazy)
    .map(Employee::getName)                        // Intermediate (lazy)
    .sorted()                                      // Intermediate (stateful, lazy)
    .distinct()                                    // Intermediate (stateful, lazy)
    .collect(Collectors.toList());                 // Terminal (triggers execution)
```

**Intermediate vs. Terminal:**
*   **Intermediate (lazy):** `filter`, `map`, `flatMap`, `peek`, `sorted`, `distinct`, `limit`, `skip`
*   **Terminal (triggers pipeline):** `collect`, `forEach`, `reduce`, `count`, `toList()`, `findFirst`, `anyMatch`, `min`/`max`
*   **Short-circuiting:** `findFirst`, `findAny`, `anyMatch`, `allMatch`, `noneMatch`, `limit` — can terminate early.

### 5. Key Collectors

```java
// Group by department
Map<Dept, List<Employee>> byDept = emps.stream()
    .collect(Collectors.groupingBy(Employee::getDept));

// Group + count
Map<Dept, Long> counts = emps.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));

// Partition (boolean grouping)
Map<Boolean, List<Employee>> partitioned = emps.stream()
    .collect(Collectors.partitioningBy(e -> e.getSalary() > 100_000));

// Join to string
String names = emps.stream().map(Employee::getName).collect(Collectors.joining(", "));

// toMap (careful: duplicate keys throw!)
Map<Long, Employee> byId = emps.stream()
    .collect(Collectors.toMap(Employee::getId, Function.identity()));
```

*   **Java 16+:** `stream.toList()` returns an **unmodifiable** list — not the same as `Collectors.toList()` which returns a mutable `ArrayList`.

### 6. `flatMap` — Flattening Nested Structures

```java
List<List<String>> nested = List.of(List.of("a", "b"), List.of("c"));
List<String> flat = nested.stream()
    .flatMap(Collection::stream)  // Stream<List<String>> → Stream<String>
    .toList(); // ["a", "b", "c"]
```

### 7. `reduce` — Aggregation

```java
int sum = numbers.stream().reduce(0, Integer::sum);
Optional<Integer> max = numbers.stream().reduce(Integer::max); // No identity → Optional
```

### 8. Parallel Streams
*   `list.parallelStream()` or `stream.parallel()` — splits work across `ForkJoinPool.commonPool()`.
*   **When to use:** Large data sets + CPU-bound operations + no shared mutable state.
*   **When NOT to use:**
    *   Small data sets (parallelism overhead > gain).
    *   I/O-bound operations (threads block, starving the common pool).
    *   Order-dependent operations (`forEachOrdered` negates parallelism).
    *   Operations with side effects.
*   **Gotcha:** The common pool is shared with `CompletableFuture` — saturating it with a parallel stream blocks other async work.

### 9. `Optional<T>`
Represents a value that may or may not be present. **Not a replacement for `null` everywhere** — designed for method return types.

```java
Optional<User> user = findById(id);
String name = user.map(User::getName).orElse("Unknown");
User u = user.orElseThrow(() -> new NotFoundException(id));
```

**Anti-Patterns:**
*   `optional.get()` without `isPresent()` — defeats the purpose, throws `NoSuchElementException`.
*   `Optional` as a field or method parameter — adds overhead and complexity.
*   `Optional` of collections — return empty collection instead.
*   `Optional.of(null)` — throws `NullPointerException`. Use `Optional.ofNullable()`.

### Interview Pro-Tip
**Question:** "What is the difference between `map` and `flatMap` in Streams?"
**Answer:** "`map` transforms each element 1-to-1 (e.g., `Stream<String>` → `Stream<Integer>` via `String::length`). `flatMap` transforms each element into a *stream* and then flattens all those streams into one — it's a 1-to-many transformation. The classic use case is flattening `Stream<List<T>>` into `Stream<T>`. In `Optional`, `flatMap` is for chaining methods that themselves return `Optional`, avoiding `Optional<Optional<T>>`."
