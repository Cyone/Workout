# 05_API_And_Library_Gotchas.md

## Java API & Library Gotchas — Trick Interview Questions

---

### Gotcha #1: `BigDecimal` — The `0.1` Trap
```java
BigDecimal bad  = new BigDecimal(0.1);  // 0.1000000000000000055511151231257827021181583404541015625
BigDecimal good = new BigDecimal("0.1"); // 0.1 — exact!
BigDecimal also = BigDecimal.valueOf(0.1); // 0.1 — uses Double.toString() internally
```
**Why?** `new BigDecimal(double)` captures the **exact** IEEE 754 representation of `0.1`, which is not `0.1`. The `String` constructor parses the decimal representation literally.

**Rule:** Never use `new BigDecimal(double)` for financial calculations. Use the `String` constructor or `valueOf()`.

---

### Gotcha #2: `BigDecimal.equals()` Considers Scale
```java
BigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("1.00");
System.out.println(a.equals(b));    // false!  (1.0 has scale 1, 1.00 has scale 2)
System.out.println(a.compareTo(b)); // 0       (numerically equal)
```
**Rule:** Use `compareTo() == 0` for numeric equality, not `equals()`. If using `BigDecimal` as `HashMap` keys, use `stripTrailingZeros()` first or switch to `TreeMap` (which uses `compareTo`).

---

### Gotcha #3: `LocalDateTime` Has No Time Zone
```java
LocalDateTime ldt = LocalDateTime.now();          // Local clock, NO zone info
ZonedDateTime zdt = ZonedDateTime.now();           // Includes time zone
Instant instant = Instant.now();                   // UTC epoch point

// WRONG: converting LocalDateTime to Instant without specifying a zone
Instant wrong = ldt.toInstant(ZoneOffset.UTC); // Assumes UTC — may not be your intention!
```
**Rule:**
*   Use `Instant` for timestamps (store in DB, API communication).
*   Use `ZonedDateTime` when the user's local time matters.
*   Use `LocalDateTime` only when zone is irrelevant (e.g., "the meeting is at 3pm" without specifying where).

---

### Gotcha #4: `DateTimeFormatter` Thread Safety

| Formatter | Thread Safe? |
|-----------|-------------|
| `SimpleDateFormat` | ❌ **Not** thread-safe! Shared instances corrupt output. |
| `DateTimeFormatter` | ✅ Thread-safe and immutable. |

```java
// BUG — shared SimpleDateFormat across threads:
static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
// Thread 1: SDF.format(date1);
// Thread 2: SDF.format(date2);  // Corrupted output or ArrayIndexOutOfBoundsException!

// FIX:
static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Safe!
```

---

### Gotcha #5: `Optional.get()` Without Check
```java
Optional<User> opt = findUser(id);
User user = opt.get(); // NoSuchElementException if empty!
```
**Anti-pattern hierarchy:**
```java
opt.get()                                   // Worst — defeats the purpose
opt.isPresent() ? opt.get() : null          // Bad — just use nullable return
opt.orElse(defaultUser)                     // OK
opt.orElseGet(() -> createDefault())        // Good — lazy default
opt.orElseThrow(() -> new NotFoundException()) // Best for mandatory values
opt.map(User::getName).orElse("Unknown")    // Best for transformations
```

---

### Gotcha #6: `orElse()` vs `orElseGet()` — Eagerness
```java
Optional<String> opt = Optional.of("value");

opt.orElse(expensiveCall());      // expensiveCall() is ALWAYS evaluated (even when value is present!)
opt.orElseGet(() -> expensiveCall()); // expensiveCall() is evaluated ONLY when opt is empty
```
**Rule:** Use `orElseGet()` when the alternative involves computation, I/O, or object creation.

---

### Gotcha #7: `Stream.toList()` vs `Collectors.toList()`
```java
// Java 16+
List<String> list1 = stream.toList();                     // Unmodifiable!
List<String> list2 = stream.collect(Collectors.toList()); // Mutable ArrayList

list1.add("x"); // UnsupportedOperationException!
list2.add("x"); // Works fine
```
**Also:** `Collectors.toList()` doesn't guarantee the specific list implementation (currently `ArrayList`, but not contractual). `toList()` explicitly returns an unmodifiable list.

---

### Gotcha #8: `Comparator.naturalOrder()` vs Nulls
```java
List<String> list = Arrays.asList("b", null, "a");
list.sort(Comparator.naturalOrder()); // NullPointerException!

// Fix:
list.sort(Comparator.nullsFirst(Comparator.naturalOrder())); // nulls first, then sorted
list.sort(Comparator.nullsLast(Comparator.naturalOrder()));  // sorted, then nulls
```

---

### Gotcha #9: `String.format()` vs `formatted()` (Java 15+)
```java
// Old way:
String old = String.format("Hello %s, you are %d", name, age);

// New way (Java 15+):
String newWay = "Hello %s, you are %d".formatted(name, age);
```
Both use the same `Formatter` under the hood. `formatted()` is just syntactic sugar.

---

### Gotcha #10: `Arrays.sort()` vs `Collections.sort()` — Algorithm Difference

| Method | Algorithm | Stable? | Array Type |
|--------|-----------|---------|------------|
| `Arrays.sort(int[])` | Dual-Pivot Quicksort | No | Primitives |
| `Arrays.sort(Object[])` | TimSort | Yes | Objects |
| `Collections.sort(List)` | TimSort (converts to array, sorts, copies back) | Yes | Objects |

**Why different?** Stability doesn't matter for primitives (two equal `int`s are indistinguishable). Quicksort is faster in practice for primitives due to cache behavior.

---

### Gotcha #11: `Pattern.compile()` Is Expensive
```java
// BUG — recompiling regex on every call:
boolean matches(String input) {
    return input.matches("\\d{3}-\\d{4}"); // Compiles Pattern every time!
}

// FIX — compile once, reuse:
private static final Pattern PHONE = Pattern.compile("\\d{3}-\\d{4}");
boolean matches(String input) {
    return PHONE.matcher(input).matches();
}
```
`String.matches()`, `String.split()`, and `String.replaceAll()` all compile a `Pattern` internally on every call.
