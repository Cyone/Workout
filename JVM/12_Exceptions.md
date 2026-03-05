# 12_Exceptions.md

## The Exception Hierarchy

### 1. The Class Tree
```
Throwable
├── Error               ← JVM-level, don't catch
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── VirtualMachineError
└── Exception
    ├── RuntimeException (Unchecked)
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   ├── ClassCastException
    │   └── ArrayIndexOutOfBoundsException
    └── Checked Exceptions (must declare or handle)
        ├── IOException
        ├── SQLException
        └── ClassNotFoundException
```

### 2. Checked vs. Unchecked
| Type | Must handle? | Root Class | When to use |
|------|-------------|------------|-------------|
| **Checked** | Yes (`try/catch` or `throws`) | `Exception` | Recoverable external conditions (file not found, network error) |
| **Unchecked** | No | `RuntimeException` | Programming errors (null pointer, bad argument) |
| **Error** | Never | `Error` | JVM integrity failure — not for application code |

**Controversial:** Checked exceptions force caller awareness but pollute APIs. `RuntimeException` gives cleaner code but can be silently swallowed. Modern APIs (Spring, Kotlin) prefer unchecked.

### 3. `finally` Guarantee
The `finally` block always executes — even if an exception is thrown or a `return` is hit.

**Exception:** `System.exit()` or JVM crash bypasses `finally`.

**Tricky Question:**
```java
int method() {
    try { return 1; }
    finally { return 2; } // finally overrides the try return!
}
// Returns 2. The value "1" is saved, then "2" clobbers it.
```
**Rule:** Never use `return`, `break`, or `continue` inside `finally` — it swallows exceptions.

### 4. Try-With-Resources (`AutoCloseable`)
```java
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    // use resources
} // conn and ps are automatically closed, even on exception
```

*   Resources are closed in **reverse order** of declaration.
*   If both the `try` body and `close()` throw, the `close()` exception is **suppressed** (accessible via `Throwable.getSuppressed()`).

### 5. Exception Chaining (Root Cause Preservation)
```java
try {
    dbOperation();
} catch (SQLException e) {
    // WRONG: throw new ServiceException("DB failed"); // Loses original stack trace
    // RIGHT:
    throw new ServiceException("DB failed", e); // Wraps and preserves root cause
}
```
Always wrap, never lose the original exception. `Throwable.getCause()` retrieves the wrapped exception.

### 6. Custom Exceptions — Best Practices
```java
// Prefer RuntimeException unless callers MUST handle it
public class OrderNotFoundException extends RuntimeException {
    private final long orderId;
    public OrderNotFoundException(long orderId) {
        super("Order not found: " + orderId);
        this.orderId = orderId;
    }
}
```
*   Add a meaningful message and any relevant data fields.
*   Define one per distinct failure mode, not one generic `AppException`.

### Interview Pro-Tip
**Question:** "What happens to an exception thrown inside a `finally` block?"
**Answer:** "It is thrown normally, but any exception from the `try` block is **lost** (suppressed silently). This is why you should almost never throw from `finally`. Try-with-resources solved this for `close()` by using `Throwable.addSuppressed()` so the original exception survives."
