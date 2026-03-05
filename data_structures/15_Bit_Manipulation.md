# Bit Manipulation Deep Dive

Bit manipulation operates directly on the binary representations of integers. These operations run in **O(1)** time and are used for compact state encoding, fast arithmetic, and solving problems that seem complex but resolve elegantly via XOR or masks.

## 1. The Essential Operators

| Operator | Java Symbol | Example (a=5=101, b=3=011) | Result |
|:---|:---|:---|:---|
| AND | `&` | `5 & 3` → `001` | 1 |
| OR | `\|` | `5 \| 3` → `111` | 7 |
| XOR | `^` | `5 ^ 3` → `110` | 6 |
| NOT | `~` | `~5` → `...11111010` | -6 |
| Left Shift | `<<` | `5 << 1` → `1010` | 10 (×2) |
| Right Shift | `>>` | `5 >> 1` → `10` | 2 (÷2) |
| Unsigned Right Shift | `>>>` | fills with 0 regardless of sign | |

---

## 2. Critical Bit Tricks

### XOR Properties
XOR is the most useful operator for interview problems. Key properties:
- `a ^ a = 0` (XOR of a number with itself is 0)
- `a ^ 0 = a` (XOR with 0 is the identity)
- XOR is commutative and associative

**Use case:** `nums[0] ^ nums[1] ^ ... ^ nums[n-1]` cancels all pairs, leaving only the single unique element. **(Single Number — LeetCode 136)**

### Remove the Lowest Set Bit
```java
n = n & (n - 1);
// Example: n=12 (1100) → n & 1011 → 1000
```
This is how you count 1-bits efficiently (Brian Kernighan's algorithm):
```java
int count = 0;
while (n != 0) { n &= (n - 1); count++; }
```

### Isolate the Lowest Set Bit
```java
int lowestBit = n & (-n); // -n is the two's complement
```

### Check / Set / Clear a Specific Bit
```java
boolean isSet  = (n & (1 << i)) != 0;  // Check bit i
int setted     = n | (1 << i);          // Set bit i
int cleared    = n & ~(1 << i);         // Clear bit i
int toggled    = n ^ (1 << i);          // Toggle bit i
```

### Power of Two Check
```java
boolean isPowerOfTwo = n > 0 && (n & (n - 1)) == 0;
// A power of two has exactly one set bit, so n-1 clears it → result is 0.
```

---

## 3. Bit Masking for Subset Enumeration

When N is small (≤ 20), represent subsets as integers. Each bit `i` being 1 means element `i` is included.

```java
int n = nums.length;
for (int mask = 0; mask < (1 << n); mask++) {
    List<Integer> subset = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0)
            subset.add(nums[i]);
    }
    // process subset
}
```
This generates all 2^N subsets in O(N × 2^N) — identical to backtracking but often cleaner for small N.

---

## 4. Java-specific Notes

- **`int`:** 32-bit signed. Bit 31 is the sign bit.
- **`long`:** 64-bit signed. Use `1L << i` to avoid overflow when shifting into high bits.
- **`Integer.bitCount(n)`:** Returns the number of 1-bits (Hamming weight). O(1) jdk intrinsic.
- **`Integer.reverse(n)`:** Reverses all 32 bits of an int.
- **Unsigned operations:** Java has no `uint`. For unsigned behavior, use `>>> ` (unsigned right shift) and mask with `0xFFFFFFFFL` when widening to `long`.

---

## 5. Time Complexity Cheat Sheet

| Operation | Time | Notes |
|:---|:---|:---|
| **Any bitwise op (AND/OR/XOR/NOT/shift)** | **O(1)** | Handled by CPU in single instruction |
| **Count 1-bits (Hamming weight)** | O(number of 1-bits) / O(1) with `Integer.bitCount` | |
| **Check power of two** | **O(1)** | `n > 0 && (n & (n-1)) == 0` |
| **Enumerate all subsets** | **O(N × 2^N)** | Practical only for N ≤ ~20 |
| **XOR in array to find single element** | **O(N)** | Single linear pass |
| **Maximum XOR (via Trie)** | **O(N × 32)** = O(N) | Build Trie of binary representations |
