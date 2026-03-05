# 13_Generics_TypeErasure.md

## Generics: Compile-Time Safety, Runtime Erasure

### 1. Why Generics Exist
Before Java 5, collections stored `Object`. You'd get a `ClassCastException` at **runtime** without knowing where. Generics move that error to **compile time**.

```java
// Pre-generics (dangerous)
List list = new ArrayList();
list.add("Hello");
Integer x = (Integer) list.get(0); // Compiles fine, fails at runtime!

// With generics (safe)
List<String> list = new ArrayList<>();
// list.add(42); // Fails at COMPILE TIME — safe!
```

### 2. Type Erasure — The Key JVM-Level Concept
**Generics are a compile-time feature.** The JVM itself does not know generic types. After compilation, the Java compiler **erases** all type parameters:
*   `List<String>` → `List`
*   `T` → `Object` (or its upper bound)
*   **Casts are inserted** at the call site to preserve type safety.

**Consequence:** At runtime, `List<String>` and `List<Integer>` are the identical class `java.util.List`.

```java
List<String> strings = new ArrayList<>();
List<Integer> ints = new ArrayList<>();
System.out.println(strings.getClass() == ints.getClass()); // TRUE
```

### 3. Bounded Type Parameters
```java
// Upper bound: T must be a Number or subclass
<T extends Number> double sum(List<T> list) { ... }

// Lower bound (on wildcards only): type must be Integer or supertype
void addNumbers(List<? super Integer> list) { list.add(42); }

// PECS Rule: Producer Extends, Consumer Super
// Use <? extends T>  when you READ from a collection (it produces T)
// Use <? super T>    when you WRITE to a collection (it consumes T)
```

### 4. Reifiable vs. Non-Reifiable Types
A **reifiable** type is one whose complete type information is available at runtime.
*   `int[]`, `String[]`, `Object` → Reifiable ✅
*   `List<String>`, `T`, `Map<K,V>` → **Not reifiable** ❌ (erased)

This is why you **cannot** do:
```java
new T();                     // ERROR: can't instantiate type parameter
new List<String>[10];        // ERROR: can't create generic array
instanceof List<String>      // ERROR: can't check erased type at runtime
```

### 5. Bridge Methods
When a generic class is subtyped, the compiler generates synthetic **bridge methods** to preserve polymorphism after erasure.

```java
class Node<T> { void set(T value) { ... } }
class IntNode extends Node<Integer> {
    @Override void set(Integer value) { ... }
    // Compiler secretly generates:
    // void set(Object value) { set((Integer) value); }
}
```

### 6. Variance (Invariance in Java)
Java generics are **invariant**: `List<Integer>` is **not** a subtype of `List<Number>`, even though `Integer extends Number`.
*   Arrays are **covariant** (`Number[] arr = new Integer[5]` compiles), which is why generic arrays are banned — they'd break type safety.

### Interview Pro-Tip
**Question:** "How does type erasure interact with overloading?"
**Answer:** "You cannot overload methods that differ only in generic parameter because after erasure they have the same signature. For example, `void process(List<String> x)` and `void process(List<Integer> x)` both become `void process(List x)` and will fail to compile with 'erasure of method... is the same'."
