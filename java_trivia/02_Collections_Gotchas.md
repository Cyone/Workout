# 02_Collections_Gotchas.md

## Java Collections Gotchas — Trick Interview Questions

---

### Gotcha #1: `ConcurrentModificationException` — The Classic
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s); // ConcurrentModificationException!
    }
}
```
**Why?** The enhanced `for` loop uses an `Iterator` internally. Direct modification of the list during iteration violates the iterator's fail-fast contract.

**Fixes:**
```java
// Fix 1: Use Iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) { if (it.next().equals("b")) it.remove(); }

// Fix 2: removeIf (Java 8+)
list.removeIf(s -> s.equals("b"));

// Fix 3: Use CopyOnWriteArrayList (if concurrent access needed)
```

---

### Gotcha #2: `HashMap` with Mutable Keys
```java
List<String> key = new ArrayList<>(List.of("a"));
Map<List<String>, String> map = new HashMap<>();
map.put(key, "value");
System.out.println(map.get(key)); // "value" ✅

key.add("b"); // Mutating the key changes its hashCode!
System.out.println(map.get(key)); // null ❌ — looks in wrong bucket!
map.containsKey(key);             // false
```
**Why?** `HashMap` stores elements based on `hashCode()` at insertion time. Mutating the key changes its hash — the map can no longer find it. The entry is forever orphaned (memory leak).

**Rule:** Use only **immutable** objects as `Map` keys.

---

### Gotcha #3: `equals()` / `hashCode()` Contract Violation
```java
class Employee {
    String name; int id;
    @Override public boolean equals(Object o) {
        return o instanceof Employee e && e.name.equals(name); // Only checks name!
    }
    // No hashCode() override!
}

Set<Employee> set = new HashSet<>();
set.add(new Employee("Alice", 1));
set.contains(new Employee("Alice", 2)); // May return false!
```
**Contract:** If `a.equals(b)` is `true`, then `a.hashCode() == b.hashCode()` **must** be `true`.

If you override `equals()` but not `hashCode()`, `HashMap`/`HashSet` lookups fail because they check the bucket (determined by `hashCode()`) first.

---

### Gotcha #4: `TreeMap` with Inconsistent `Comparable`
```java
class Employee implements Comparable<Employee> {
    String name; int id;
    public int compareTo(Employee o) { return name.compareTo(o.name); }
    public boolean equals(Object o) {
        return o instanceof Employee e && e.id == id; // Compares id!
    }
}
```
**Problem:** `TreeMap` uses `compareTo()` for equality (when `compareTo` returns 0, it considers them equal). But `equals()` uses a different field (`id`). Now:
*   Two employees with the same name but different IDs → `TreeMap` sees them as **one** entry.
*   `containsKey` behavior doesn't match `equals()`.

**Rule:** `compareTo()` and `equals()` should be **consistent**: `a.compareTo(b) == 0` should imply `a.equals(b)`.

---

### Gotcha #5: `List.of()` vs `Arrays.asList()` vs `new ArrayList<>()`

| Factory | Null Elements | Mutability | Backed by Array? |
|---------|---------------|------------|------------------|
| `List.of(a, b)` | ❌ Throws NPE | Fully immutable | No |
| `Arrays.asList(arr)` | ✅ Allowed | Fixed-size (`set()` works, `add()`/`remove()` throw) | Yes — changes to array reflect in list |
| `new ArrayList<>(List.of(...))` | ✅ Allowed | Fully mutable | No |

```java
String[] arr = {"a", "b"};
List<String> view = Arrays.asList(arr);
arr[0] = "z";
System.out.println(view.get(0)); // "z" — it's a view, not a copy!

List<String> immutable = List.of("a", "b");
immutable.add("c"); // UnsupportedOperationException
```

---

### Gotcha #6: `Collections.unmodifiableList()` Isn't Truly Immutable
```java
List<String> original = new ArrayList<>(List.of("a", "b"));
List<String> unmod = Collections.unmodifiableList(original);
System.out.println(unmod); // [a, b]

original.add("c"); // Mutating the BACKING list!
System.out.println(unmod); // [a, b, c] — the "unmodifiable" list changed!
```
**Why?** `unmodifiableList` is an **unmodifiable view**, not a copy. It delegates to the original list. Changes to the original are visible through the view.

**Fix:** `List.copyOf(original)` (Java 10+) — creates a true immutable copy.

---

### Gotcha #7: `Map.of()` Has a Size Limit
```java
Map.of("a", 1, "b", 2, ..., "k", 11); // Only up to 10 key-value pairs!
// For more, use:
Map.ofEntries(
    Map.entry("a", 1),
    Map.entry("b", 2),
    // ... any number of entries
);
```

---

### Gotcha #8: `subList()` Returns a View, Not a Copy
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c", "d"));
List<String> sub = list.subList(1, 3); // ["b", "c"]

list.add("e"); // Structural modification to the parent!
sub.get(0);    // ConcurrentModificationException!
```
**Why?** `subList()` is a **view** into the original. Any structural modification (add/remove) to the original list invalidates the sublist.

---

### Gotcha #9: `remove(int)` vs `remove(Object)` on `List<Integer>`
```java
List<Integer> list = new ArrayList<>(List.of(10, 20, 30));
list.remove(1);         // Removes element at INDEX 1 → removes 20
list.remove(Integer.valueOf(10)); // Removes the OBJECT 10
```
**Why?** `List` has two `remove` methods: `remove(int index)` and `remove(Object o)`. With `List<Integer>`, `remove(1)` is ambiguous — the compiler picks `remove(int index)` because primitive overloads take priority.

---

### Gotcha #10: `EnumSet.of()` Order Doesn't Matter
```java
EnumSet<Day> set1 = EnumSet.of(Day.MONDAY, Day.FRIDAY);
EnumSet<Day> set2 = EnumSet.of(Day.FRIDAY, Day.MONDAY);
System.out.println(set1.equals(set2)); // true
// Iteration order follows the ENUM DECLARATION ORDER, not insertion order
for (Day d : set1) { ... } // Iterates in declaration order of the Day enum
```
