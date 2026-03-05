# Kotlin vs Java: Under the Hood

Kotlin aims for 100% interoperability with Java. To achieve this, `kotlinc` must ultimately generate `.class` files containing JVM bytecode identical in structure to what `javac` produces. Understanding this mapping is crucial for backend developers optimizing for the JVM.

## 1. Top-Level Functions & Extensions
In Kotlin, you can declare functions outside of any class.
*   **The JVM Reality:** The JVM requires all methods to reside inside a class.
*   **How it works:** If you have `fun doSomething()` in a file named `Utils.kt`, the Kotlin compiler creates a synthetic class named `UtilsKt` and compiles the function to `public static final void doSomething()` inside that class.
*   **Java Interop:** From Java, you call it via `UtilsKt.doSomething()`. Overriding this behavior uses `@JvmName("MyUtils")` on the Kotlin file.

### Extension Functions
*   **Kotlin:** `fun String.lastChar(): Char = this.get(this.length - 1)`
*   **Under the Hood:** Extending final classes like `String` natively is impossible. Kotlin implements extensions as `public static final` methods where the receiver object (`this`) becomes the first parameter of the method.
*   **Java Interop:** `char c = StringUtilKt.lastChar("Hello");`

## 2. Null Safety (The Billion Dollar Mistake)
Kotlin's defining feature is distinguishing nullable (`String?`) from non-nullable (`String`) types.
*   **The Problem:** The JVM has no concept of this. A reference is a reference (which can be null).
*   **How it works:** The non-nullability is enforced entirely at compile time. At runtime, the nullable and non-nullable types are erased to the same Java type (e.g., `java.lang.String`).
*   **Intrinsics:** For every non-null parameter passed to a Kotlin function, the compiler forcibly injects a bytecode check: `Intrinsics.checkParameterIsNotNull(param, "paramName")`. If a Java caller passes `null`, this check throws an `IllegalArgumentException` instantly, preventing downstream `NullPointerExceptions`.

## 3. Data Classes vs Records
`data class User(val name: String)` generates massive boilerplate:
*   Getters for properties (and Setters if `var`).
*   `equals()`, `hashCode()`, `toString()`.
*   `copy()` method.
*   `componentN()` methods for destructuring declarations: `val (name, age) = user`.

*   **Compared to Java 14+ Records:** Java Records are immutable by default and also generate `equals`, `hashCode`, and `toString`. However, `data class` supports `var` properties (mutability) and the highly useful `copy` block, lacking natively in Records.

## 4. `inline` Functions & Reified Types
One of Kotlin's most powerful performance optimizations, specifically for Higher-Order Functions.

*   **The Problem with Lambdas:** In Java/Kotlin, creating a lambda creates an anonymous class instance. If you call a function taking a lambda repeatedly in a loop, you allocate memory constantly, triggering the Garbage Collector.
*   **The Solution (`inline`):** When you declare a function `inline fun foo(block: () -> Unit)`, the compiler copies the *actual bytecode* of both `foo` and the `block` directly into the call site. No anonymous object is allocated.
*   *Note:* Only use `inline` for small functions taking lambda arguments. Inlining a massive function everywhere bloats the binary size.

### Reified Generics
Because of Type Erasure, you can't check the type of a generic parameter at runtime in Java (`if (x is T)` is illegal).
*   **Kotlin's Trick:** If a function is `inline`, you can mark a type parameter as `reified`: `inline fun <reified T> isType(value: Any) = value is T`.
*   **How it works:** Because `inline` pastes the function body at the call site, the compiler *knows* the exact concrete type used at that specific call site. It hardcodes the type check into the bytecode, bypassing Type Erasure completely.
