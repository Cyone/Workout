# 06_Generics_And_Types_Gotchas.md

## Java Generics & Type System Gotchas — Trick Interview Questions

---

### Gotcha #1: Type Erasure Makes Overloading Fail
```java
// This does NOT compile:
void process(List<String> strings)  { }
void process(List<Integer> numbers) { }
// Error: both have the same erasure: process(List)
```
**Why?** After type erasure, both methods have the signature `process(List)`. The JVM has no way to distinguish them.

---

### Gotcha #2: Generic Array Creation is Forbidden
```java
List<String>[] array = new List<String>[10]; // COMPILE ERROR!
// But this works (with raw type):
List<String>[] array = new List[10]; // Warning, but compiles.
```
**Why?** Arrays carry runtime type info and are covariant. Generics are erased and invariant. Allowing generic arrays would break type safety:
```java
Object[] oArray = array;      // Covariant — legal at compile time
oArray[0] = List.of(123);     // No ArrayStoreException (erased to List)
String s = array[0].get(0);   // ClassCastException at runtime!
```

---

### Gotcha #3: Unchecked Cast That Actually Crashes (Later)
```java
@SuppressWarnings("unchecked")
<T> T unsafeCast(Object obj) {
    return (T) obj; // Warning suppressed. No ClassCastException HERE.
}

String s = unsafeCast(42);  // ClassCastException HERE — at the CALL SITE, not the cast!
```
**Why?** Due to type erasure, `(T) obj` erases to `(Object) obj` — always succeeds. The actual cast happens at the call site where the compiler inserts `(String)` based on the inferred type.

**Trap:** The error points to a line that looks perfectly safe, far from the buggy cast method.

---

### Gotcha #4: Raw Types Bypass All Generic Safety
```java
List<String> typed = new ArrayList<>();
typed.add("hello");

List raw = typed;     // Raw type — loses all generic info
raw.add(42);          // Compiles with warning! No type check.

String s = typed.get(1); // ClassCastException at runtime!
```
**Rule:** Never use raw types in new code. They exist only for backward compatibility with pre-Java-5 code.

---

### Gotcha #5: `Class<T>` vs `Class<?>`
```java
Class<String> specific = String.class;               // Known type
Class<?> wildcard = Class.forName("java.lang.String"); // Unknown at compile time

String s = specific.cast(obj);   // Clean cast, returns String
Object o = wildcard.cast(obj);   // Returns Object (? = unknown)
```
**Usage:** Use `Class<T>` as a "type token" for type-safe heterogeneous containers:
```java
class TypeSafeMap {
    private Map<Class<?>, Object> map = new HashMap<>();
    <T> void put(Class<T> type, T value) { map.put(type, value); }
    <T> T get(Class<T> type) { return type.cast(map.get(type)); }
}
```

---

### Gotcha #6: `instanceof` Can't Check Generics
```java
if (list instanceof List<String>) { } // COMPILE ERROR — type erasure!
if (list instanceof List<?>) { }      // OK — unbounded wildcard
if (list instanceof List) { }          // OK — raw type
```
**Rule:** You can only use `instanceof` with reifiable types (non-parameterized, unbounded wildcard, or raw).

---

### Gotcha #7: Wildcard Capture Confusion
```java
void swap(List<?> list, int i, int j) {
    list.set(i, list.get(j)); // COMPILE ERROR!
    // The compiler doesn't know that get() and set() operate on the same unknown type
}
```
**Fix — capture helper:**
```java
void swap(List<?> list, int i, int j) {
    swapHelper(list, i, j);
}
<T> void swapHelper(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
}
```

---

### Gotcha #8: `Comparable<T>` Self-Referencing Bound
```java
// Why is the bound written this way?
class Person implements Comparable<Person> {
    public int compareTo(Person other) { ... }
}

// What about:
class Employee extends Person { }
// Employee.compareTo still takes Person — it CANNOT narrow to Employee!
// To fix, use the "recursive type bound" pattern:
class Employee extends Person implements Comparable<Employee> { } // ERROR — already inherits Comparable<Person>
```
**Lesson:** Once a superclass implements `Comparable<SuperClass>`, subclasses are stuck with that parameterization. Design your hierarchy carefully.

---

### Gotcha #9: PECS Gets Confusing with Multiple Bounds
```java
// Producer Extends, Consumer Super
<T extends Comparable<? super T>> void sort(List<T> list) { ... }
// Why not just <T extends Comparable<T>>?
// Because: Employee extends Person, Person implements Comparable<Person>
// With <T extends Comparable<T>>, Employee doesn't match — it's Comparable<Person>, not Comparable<Employee>
// With <T extends Comparable<? super T>>, Employee matches because Person (super of Employee) is Comparable<Person>
```

---

### Gotcha #10: Bridge Methods Create Surprising Stack Traces
```java
class Container<T> {
    void set(T value) { System.out.println(value); }
}
class StringContainer extends Container<String> {
    @Override
    void set(String value) { System.out.println(value.toUpperCase()); }
}
```
**After erasure, the compiler generates:**
```java
// In StringContainer — synthetic bridge method:
void set(Object value) { set((String) value); } // Calls the String version
```
If you pass a non-String via raw type: `((Container) stringContainer).set(42)` → `ClassCastException` inside the **bridge method**, and the stack trace shows a method you never wrote.

---

### Gotcha #11: Type Inference Failures with Chained Generics (Pre-Java 8)
```java
// In Java 7, this doesn't compile:
List<String> list = Collections.emptyList(); // OK
foo(Collections.emptyList());               // ERROR — can't infer <String> in argument position

// Fix (Java 7): use type witness
foo(Collections.<String>emptyList());

// In Java 8+: target-type inference solves this
foo(Collections.emptyList()); // OK — infers from parameter type of foo()
```
