# 01_Language_Gotchas.md

## Java Language Gotchas — Trick Interview Questions

---

### Gotcha #1: `==` vs `.equals()` on `Integer`
```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);   // true ✅

Integer c = 128;
Integer d = 128;
System.out.println(c == d);   // false ❌
```
**Why?** The JVM caches `Integer` objects for values **-128 to 127** (`IntegerCache`). Within that range, autoboxing returns the same object. Outside that range, each autoboxing creates a new `Integer` object, so `==` compares references and fails.

**Rule:** Always use `.equals()` to compare wrapper objects.

---

### Gotcha #2: Autoboxing Null → `NullPointerException`
```java
Integer value = null;
int x = value;  // NPE! Unboxing null → NullPointerException
```
```java
Map<String, Integer> map = new HashMap<>();
int count = map.get("missing"); // NPE! get() returns null Integer, unboxed to int
```
**Rule:** Never unbox without a null check. Use `map.getOrDefault("missing", 0)`.

---

### Gotcha #3: `finally` Always Executes (Even with `return`)
```java
int getValue() {
    try {
        return 1;
    } finally {
        return 2;  // WARNING: this overrides the return in try!
    }
}
// Returns 2, NOT 1.
```
**Why?** `finally` executes after the `try` block but *before* the return value is actually delivered to the caller. A `return` in `finally` replaces the pending return.

**Rule:** Never put `return` or `throw` statements in `finally`.

---

### Gotcha #4: `final` vs `finally` vs `finalize()`

| Keyword | What It Does |
|---------|-------------|
| `final` | Variable: can't reassign. Method: can't override. Class: can't subclass. |
| `finally` | Block that always executes after `try`/`catch` (for cleanup). |
| `finalize()` | Deprecated method on `Object`. Called by GC before reclaiming. **Never use it.** Unpredictable timing, can revive objects, causes GC delays. Use `try-with-resources` or `Cleaner` instead. |

---

### Gotcha #5: `String.intern()` and the String Pool
```java
String s1 = new String("hello");  // Creates 2 objects: one in pool, one on heap
String s2 = "hello";              // Points to pool
String s3 = s1.intern();          // Returns the pool reference

System.out.println(s1 == s2);     // false — different objects
System.out.println(s2 == s3);     // true  — same pool reference
```
**Rule:** String literals and compile-time constants are automatically interned. `new String()` always creates a new heap object.

---

### Gotcha #6: Covariant Return Types
```java
class Animal { Animal create() { return new Animal(); } }
class Dog extends Animal {
    @Override
    Dog create() { return new Dog(); } // Legal! Return type is more specific
}
```
**Why it works:** Java allows an overriding method to have a **more specific return type** (a subclass of the original return type). This is called covariant return.

---

### Gotcha #7: Array Covariance is Dangerous
```java
Number[] arr = new Integer[5]; // Compiles! Arrays are covariant
arr[0] = 3.14;                 // ArrayStoreException at RUNTIME!
```
**Why?** Java arrays are covariant (`Integer[]` is a subtype of `Number[]`), but the runtime checks element types. This is a design mistake from Java 1.0 that generics (which are invariant) fixed.

---

### Gotcha #8: `try-with-resources` Close Order
```java
try (
    InputStream in = new FileInputStream("file.txt");   // Opened FIRST
    OutputStream out = new FileOutputStream("copy.txt") // Opened SECOND
) {
    // ...
} // 'out' is closed FIRST, 'in' is closed SECOND (reverse order)
```
**Rule:** Resources are closed in **reverse declaration order** (LIFO), same as a stack.

If a resource's constructor throws, only the previously opened resources are closed.

---

### Gotcha #9: `static` Initializer Block Execution Order
```java
class Foo {
    static int x = compute();           // (1) runs first
    static { System.out.println("A"); } // (2) runs second
    static int y = 5;                   // (3) runs third
    static { System.out.println("B"); } // (4) runs fourth
}
```
Static initializers and field assignments run **in textual order** when the class is first loaded. Instance initializers follow the same rule but run before the constructor body.

---

### Gotcha #10: `switch` Without `break` Falls Through
```java
int x = 1;
switch (x) {
    case 1: System.out.println("one");    // Prints!
    case 2: System.out.println("two");    // ALSO prints! Fall-through!
    case 3: System.out.println("three");  // ALSO prints!
}
```
**Fix:** Use `break` after each case, or use enhanced `switch` with arrow syntax (`->`) which has no fall-through.

---

### Gotcha #11: `equals()` Symmetry with Inheritance
```java
class Point { int x, y; /* equals compares x and y */ }
class ColorPoint extends Point { Color color; /* equals compares x, y, and color */ }

Point p = new Point(1, 2);
ColorPoint cp = new ColorPoint(1, 2, RED);
p.equals(cp);  // true (Point ignores color)
cp.equals(p);  // false (ColorPoint checks color, p has none)
// Violates symmetry contract of equals()!
```
**Rule:** Prefer composition over inheritance when `equals()` semantics differ. Or use `getClass()` instead of `instanceof` in `equals()`.

---

### Gotcha #12: Varargs + Overloading Ambiguity
```java
void foo(int... args)    { System.out.println("varargs"); }
void foo(int a, int b)  { System.out.println("two args"); }

foo(1, 2); // "two args" — exact match wins
foo(1);    // "varargs"
foo();     // "varargs"
```
The compiler prefers the most specific method. But two varargs overloads (e.g., `foo(int...)` and `foo(long...)`) can cause ambiguous compile errors.
