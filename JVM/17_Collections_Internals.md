# 17_Collections_Internals.md

## Java Collections Framework — Internals & Interview Deep Dive

### 1. `ArrayList`
*   **Backing Structure:** Resizable `Object[]` array.
*   **Default Capacity:** 10 (created on first `add()`, not at construction with `new ArrayList<>()`).
*   **Growth:** When full, grows to `oldCapacity + (oldCapacity >> 1)` = **1.5×**. Uses `Arrays.copyOf()` (native `System.arraycopy`).
*   **Access:** `get(i)` is **O(1)**. `add(index, e)` is **O(n)** (shifts right). `remove(index)` is **O(n)** (shifts left).
*   **Iteration:** Cache-friendly — elements are contiguous in memory.

### 2. `LinkedList`
*   **Backing Structure:** Doubly-linked list of `Node` objects (each holds `prev`, `item`, `next`).
*   **Access:** `get(i)` is **O(n)** — must traverse from head or tail. Never use indexed access in a loop!
*   **Add/Remove at Head/Tail:** **O(1)**. This is the *only* scenario where `LinkedList` beats `ArrayList`.
*   **Implements:** Both `List` and `Deque` interfaces.
*   **Memory:** Each `Node` has 3 references + object header ≈ **24 extra bytes per element** vs. `ArrayList`'s 4 bytes (reference only).

**Rule:** `ArrayList` wins almost always. Use `LinkedList` only for true deque/queue semantics at the ends.

### 3. `HashMap`
*   **Backing Structure:** Array of **Buckets** (`Node<K,V>[]`). Default capacity = 16, load factor = 0.75.
*   **Hashing:** `hash(key) = key.hashCode() ^ (key.hashCode() >>> 16)` — spreads high bits into low bits to reduce collisions.
*   **Collision Handling:**
    *   **< 8 entries in a bucket:** Singly-linked list (`Node`). Lookup is **O(n)** within bucket.
    *   **≥ 8 entries (treeify threshold):** Converts to a **Red-Black Tree** (`TreeNode`). Lookup becomes **O(log n)**.
    *   **≤ 6 entries:** Converts **back** to linked list (untreeify).
*   **Resize:** When `size > capacity × loadFactor`, doubles capacity and rehashes all entries.
*   **Null Key:** Allowed (stored at bucket index 0). Only one null key.

```java
Map<String, Integer> map = new HashMap<>(32, 0.5f); // custom capacity + load factor
```

### 4. `LinkedHashMap`

#### How Order Is Preserved — The Internal Mechanism

`LinkedHashMap` extends `HashMap` but overrides its `Node` class to add two extra pointers:

```java
// Inside LinkedHashMap (simplified)
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before;   // previous entry in insertion/access order
    Entry<K,V> after;    // next     entry in insertion/access order
}
```

It also maintains a **sentinel doubly-linked list** with `head` (oldest) and `tail` (newest) pointers. Every `Entry` lives in **two data structures simultaneously**:

1. **The HashMap bucket array** — for O(1) hash-based lookup (same as `HashMap`).
2. **A doubly-linked list** threaded through the `before`/`after` pointers — for ordered iteration.

```
Bucket array:       [0] → null
                    [1] → Entry("A") → Entry("C") → null   (same bucket, different list)
                    [2] → Entry("B") → null

Doubly-linked list: head ←→ Entry("A") ←→ Entry("B") ←→ Entry("C") ←→ tail
                    (insertion order preserved regardless of bucket placement)
```

#### Insertion Order (Default)

```java
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
map.put("A", 1);  // appended to tail of doubly-linked list
map.put("B", 2);
map.put("C", 3);
map.keySet().forEach(System.out::println); // A, B, C — always in put() order
```

When `put()` is called, `LinkedHashMap` overrides `afterNodeInsertion()` to append the new `Entry` to the tail. When `remove()` is called, `afterNodeRemoval()` unlinks the `Entry` from the doubly-linked list in O(1) by rewiring `before` and `after` pointers.

#### Access Order (LRU mode)

```java
LinkedHashMap<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);
//                                                                     ^^^^ accessOrder = true
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);
map.get("A");   // "A" is moved from head to tail
// Iteration order is now: B, C, A  (least → most recently accessed)
```

When `accessOrder = true`, `get()` and `getOrDefault()` call `afterNodeAccess()` which **moves the accessed entry to the tail** of the doubly-linked list in O(1):

```
Before get("A"):   head ←→ A ←→ B ←→ C ←→ tail
After  get("A"):   head ←→ B ←→ C ←→ A ←→ tail
```

This makes `head` always point to the **Least Recently Used** entry — perfect for LRU eviction.

#### Key Takeaways

| Aspect | Detail |
|--------|--------|
| Extra memory | 2 additional references (`before`, `after`) per entry ≈ 16 extra bytes |
| Iteration | O(n) via the doubly-linked list, skips all empty buckets |
| `put` / `get` | O(1) — same as `HashMap`, list pointer updates are O(1) |
| `remove` | O(1) — two pointer rewires in the linked list |

#### LRU Cache Pattern

```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;
    LRUCache(int maxSize) { super(maxSize, 0.75f, true); this.maxSize = maxSize; }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize; // head entry (LRU) is auto-removed when over capacity
    }
}
```

### 5. `TreeMap` / `TreeSet`
*   **Backing Structure:** **Red-Black Tree** (self-balancing BST).
*   **All Operations:** `get`, `put`, `remove`, `containsKey` — **O(log n)**.
*   **Ordering:** Keys must be `Comparable` **or** a `Comparator` must be provided at construction.
*   **Implements `NavigableMap`:** Rich API:

| Method | Description |
|--------|-------------|
| `firstKey()` / `lastKey()` | Min / Max key |
| `lowerKey(k)` / `higherKey(k)` | Strictly less / greater |
| `floorKey(k)` / `ceilingKey(k)` | ≤ / ≥ |
| `subMap(from, to)` | Range view [from, to) |
| `descendingMap()` | Reversed view |
| `headMap(k)` / `tailMap(k)` | < k / ≥ k |

*   **`TreeSet`** is backed by a `TreeMap` (values are a dummy `PRESENT` object, same pattern as `HashSet`).

### 6. `HashSet` / `LinkedHashSet`
*   `HashSet` = `HashMap<E, PRESENT>` where `PRESENT` is a static dummy `Object`.
*   `LinkedHashSet` = `LinkedHashMap<E, PRESENT>` → preserves insertion order.
*   **Critical:** `add()`, `remove()`, `contains()` are **O(1)** amortized, but depend entirely on `hashCode()` and `equals()` quality.

### 7. `EnumSet` / `EnumMap`
*   **`EnumSet`:** Stored as a **bit vector** (`long` or `long[]`). Operations (`add`, `contains`, `remove`) are bit-mask operations → **O(1)** and incredibly fast.

```java
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
EnumSet<Day> weekdays = EnumSet.complementOf(weekend);
```

*   **`EnumMap`:** Backed by a plain `Object[]` indexed by `enum.ordinal()`. No hashing, no collisions → fastest `Map` for enum keys.

### 8. `WeakHashMap`
*   Keys are wrapped in `WeakReference`. When a key has **no strong references** outside the map, it becomes eligible for GC.
*   On next map operation, the entry is silently removed (via a `ReferenceQueue`).
*   **Use Case:** Caches where entries should auto-expire when the key is no longer used elsewhere (e.g., `ClassLoader` metadata caches).
*   **Gotcha:** String literals are interned and never GC'd → `WeakHashMap` with `String` keys computed at compile time will never evict.

### 9. `IdentityHashMap`
*   Uses **reference equality** (`==`) instead of `equals()` for key comparison.
*   Uses **`System.identityHashCode()`** instead of `hashCode()`.
*   **Use Case:** Object graph traversal (tracking which objects have already been visited), serialization frameworks.

### 10. `CopyOnWriteArrayList` / `CopyOnWriteArraySet`
*   Every mutation (`add`, `set`, `remove`) creates a **new copy** of the internal array.
*   Reads are **lock-free** and extremely fast — no synchronization needed.
*   Iterators are **snapshot-based** — they see the array as it was when the iterator was created. Never throws `ConcurrentModificationException`.
*   **Use Case:** Read-heavy, write-rare scenarios (e.g., listener lists, observer patterns).
*   **Anti-pattern:** Do NOT use for write-heavy workloads — each mutation is **O(n)** copy.

### 11. `PriorityQueue`
*   **Backing Structure:** Binary **min-heap** (stored in an array).
*   `offer()` / `poll()` — **O(log n)**. `peek()` — **O(1)**.
*   **Not sorted** — only guarantees the head is the minimum. Iteration order is undefined.
*   Elements must be `Comparable` or a `Comparator` must be supplied.

```java
// Max-heap via reversed comparator
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
```

### 12. `ArrayDeque`
*   **Backing Structure:** Circular array (head/tail pointers wrap around).
*   Faster than `LinkedList` for stack (`push`/`pop`) and queue (`offer`/`poll`) operations due to cache locality.
*   **No null elements** allowed (nulls are used internally as empty-slot markers).
*   **Rule:** Prefer `ArrayDeque` over `Stack` (which is legacy, synchronized, and extends `Vector`).

### 13. Immutable Collections

| Factory | Nulls? | Mutability |
|---------|--------|------------|
| `Arrays.asList(a, b)` | Yes | Fixed-size (no add/remove, but `set()` works) |
| `Collections.unmodifiableList(list)` | Yes | Unmodifiable **view** — changes to backing list are visible |
| `List.of(a, b)` (Java 9) | **No** | Truly immutable. Throws `NullPointerException` on null elements |
| `List.copyOf(collection)` (Java 10) | **No** | Immutable copy. Changes to source have no effect |

### Interview Pro-Tip
**Question:** "When should you use `TreeMap` instead of `HashMap`?"
**Answer:** "Use `TreeMap` when you need **sorted key order** or **range queries** (`subMap`, `headMap`, `tailMap`). `HashMap` is O(1) amortized, `TreeMap` is O(log n) — but `TreeMap` gives you `NavigableMap` operations like `floorKey()`, `ceilingKey()`, and `descendingMap()` that `HashMap` simply cannot provide. Also, `TreeMap` guarantees consistent iteration order (sorted) while `HashMap` iteration order is undefined."
