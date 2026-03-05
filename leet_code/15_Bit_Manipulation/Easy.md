# Easy

### 1. 191. Number of 1 Bits

Write a function that takes an unsigned integer and returns the number of '1' bits (Hamming weight).

**Test Cases:**

1. `n = 00000000000000000000000000001011` -> `3`
2. `n = 00000000000000000000000010000000` -> `1`
3. `n = 11111111111111111111111111111101` -> `31`
4. `n = 0` -> `0`
5. `n = 2147483645` (binary: 0111...01) -> `30`

### 2. 231. Power of Two

Given an integer `n`, return `true` if it is a power of two.

**Test Cases:**

1. `n = 1` -> `true`
2. `n = 16` -> `true`
3. `n = 3` -> `false`
4. `n = 4` -> `true`
5. `n = 0` -> `false`

### 3. 136. Single Number

Given a non-empty array of integers `nums`, every element appears twice except for one. Find that single one. Linear time, constant space.

**Test Cases:**

1. `nums = [2,2,1]` -> `1`
2. `nums = [4,1,2,1,2]` -> `4`
3. `nums = [1]` -> `1`
4. `nums = [0,1,0]` -> `1`
5. `nums = [-1,-1,0]` -> `0`

### 4. 190. Reverse Bits

Reverse bits of a given 32-bit unsigned integer.

**Test Cases:**

1. `n = 00000010100101000001111010011100` -> `964176192` (00111001011110000010100101000000)
2. `n = 11111111111111111111111111111101` -> `3221225471`
3. `n = 0` -> `0`
4. `n = 1` -> `2147483648`
5. `n = 43261596` -> `964176192`
