# 20_Records_Sealed_PatternMatching.md

## Modern Java Features (14–21) — Interview Essentials

### 1. Records (Java 16+)
A concise way to declare **immutable data carriers**. The compiler generates `equals()`, `hashCode()`, `toString()`, and accessor methods.

```java
public record Point(int x, int y) { }

Point p = new Point(3, 4);
p.x();              // 3 — accessor, NOT getX()
p.equals(new Point(3, 4)); // true — compares all fields
```

**What's generated:**
*   `private final` fields for all components
*   Public accessor for each (e.g., `x()`, `y()`)
*   `equals()` comparing all components
*   `hashCode()` based on all components
*   `toString()` with class name and values

**Compact Constructor (validation):**
```java
public record Age(int value) {
    public Age {  // No parameter list — compact form
        if (value < 0) throw new IllegalArgumentException("Age cannot be negative");
        // 'this.value = value' is implicit at the end
    }
}
```

**Restrictions:**
*   Cannot `extend` another class (already extends `java.lang.Record`).
*   All fields are `final` — no setters.
*   Cannot declare instance fields beyond the component list.
*   CAN implement interfaces, define static fields/methods, and add instance methods.

**Use Case:** DTOs, value objects, API responses, multi-return-value from methods.

### 2. Sealed Classes & Interfaces (Java 17+)
Restrict which classes can extend/implement a type.

```java
public sealed interface Shape permits Circle, Rectangle, Triangle { }

public record Circle(double radius) implements Shape { }
public record Rectangle(double w, double h) implements Shape { }
public final class Triangle implements Shape { /* ... */ }
```

**Rules for Permitted Subtypes:**
*   Must be in the **same module** (or same package if unnamed module).
*   Must be declared `final`, `sealed`, or `non-sealed`.

| Modifier | Meaning |
|----------|---------|
| `final` | No further subclasses |
| `sealed` | Another level of sealed hierarchy |
| `non-sealed` | Opens up to arbitrary subclassing |

**Why it matters:** Enables **exhaustive** `switch` — the compiler knows all possible subtypes.

### 3. Pattern Matching for `instanceof` (Java 16+)
Eliminates cast-after-check boilerplate.

```java
// Old:
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// New:
if (obj instanceof String s) {
    System.out.println(s.length()); // 's' is already cast and scoped
}

// Works with &&:
if (obj instanceof String s && s.length() > 5) { ... }
// Does NOT work with ||  (s might not be bound)
```

### 4. Pattern Matching for `switch` (Java 21+)
```java
String describe(Shape shape) {
    return switch (shape) {
        case Circle c    -> "Circle with radius " + c.radius();
        case Rectangle r -> "Rectangle " + r.w() + "x" + r.h();
        case Triangle t  -> "Triangle";
        // No default needed — sealed interface, compiler verifies exhaustiveness
    };
}
```

**Guarded Patterns:**
```java
case Circle c when c.radius() > 10 -> "Large circle";
case Circle c                      -> "Small circle";
```

**Null Handling:**
```java
switch (obj) {
    case null       -> System.out.println("null!");
    case String s   -> System.out.println("String: " + s);
    default         -> System.out.println("other");
}
```

### 5. Record Patterns / Deconstruction (Java 21+)
Decompose records directly in pattern matching:

```java
if (obj instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);
}

// Nested deconstruction:
record Line(Point start, Point end) {}
if (obj instanceof Line(Point(var x1, var y1), Point(var x2, var y2))) {
    System.out.println("Length: " + Math.hypot(x2 - x1, y2 - y1));
}
```

### 6. Text Blocks (Java 15+)
Multi-line string literals using `"""`.

```java
String json = """
        {
            "name": "Alice",
            "age": 30
        }
        """;
```

*   Incidental whitespace (leading spaces up to the closing `"""`) is stripped.
*   Supports `\s` (trailing space preservation) and `\` (line continuation, no newline).

### 7. Enhanced `switch` Expressions (Java 14+)
```java
int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY                -> 7;
    case WEDNESDAY, THURSDAY    -> { yield 8; } // yield for block form
    default                     -> throw new IllegalArgumentException();
};
```

*   `switch` as an **expression** — returns a value.
*   Arrow syntax (`->`) — no fall-through.
*   `yield` for multi-statement arms.

### Interview Pro-Tip
**Question:** "What advantage do sealed classes provide over abstract classes?"
**Answer:** "Sealed classes give you a **closed type hierarchy** — the compiler knows every possible subtype at compile time. This enables exhaustive `switch` expressions without a `default` case, makes refactoring safer (add a new subtype and the compiler flags every `switch` that needs updating), and encodes domain constraints into the type system (e.g., a `PaymentMethod` can only be `CreditCard | BankTransfer | Crypto`). Abstract classes without `sealed` are open — anyone can subclass them, removing these guarantees."
