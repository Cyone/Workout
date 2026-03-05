# Problem Solving Patterns: Bit Manipulation

## Common Patterns
1. **XOR Tricks**
2. **Bit Masking / Subsets**
3. **Count Bits / Hamming Weight**
4. **Power of Two / Alignment Checks**

## How to Detect These Patterns

### 1. XOR Tricks
- **When to use**: A number appears an odd number of times while others appear an even number of times. XOR of a number with itself is 0; XOR with 0 returns the number.
- **Keywords**: "find the single number", "numbers appearing odd/even times", "two numbers that differ".
- **Approach**: XOR all numbers; pairs cancel out leaving the unique element.
- **Example**: Single Number, Single Number III, Missing Number.

### 2. Bit Masking / Subsets
- **When to use**: Enumerate all subsets of a set of N elements (when N is small, e.g., ≤ 20). Each integer 0 to 2^N − 1 represents a subset via its binary bits.
- **Keywords**: "all subsets", "bitmask DP", "enumerate states", "maximum XOR of subset".
- **Approach**: Iterate `mask` from 0 to `(1 << N) - 1`; bit i is set means element i is included.
- **Example**: Subsets, Maximum XOR of Two Numbers in an Array, Count of Subsets with Given XOR.

### 3. Count Bits / Hamming Weight
- **When to use**: Count the number of 1-bits in an integer, or count 1-bits for a range of integers.
- **Keywords**: "number of 1 bits", "Hamming weight", "count bits", "population count".
- **Approach**: `n & (n-1)` removes the lowest set bit. Repeat until n is 0. Or use `Integer.bitCount()`.
- **Example**: Number of 1 Bits, Counting Bits, Reverse Bits.

### 4. Power of Two / Alignment Checks
- **When to use**: Check if a number is a power of two, power of four, etc. Or align to next power.
- **Keywords**: "power of two", "power of four", "is divisible by 2^k", "next power of two".
- **Approach**: Power of two: `n > 0 && (n & (n-1)) == 0`. Power of four: also check that the single bit is in an even position.
- **Example**: Power of Two, Power of Four, Bitwise AND of Numbers Range.
