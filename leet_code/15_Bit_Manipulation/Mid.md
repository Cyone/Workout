# Mid

### 1. 137. Single Number II

Every element in `nums` appears three times except for one, which appears exactly once. Find the single one. Linear time, constant space.

**Test Cases:**

1. `nums = [2,2,3,2]` -> `3`
2. `nums = [0,1,0,1,0,1,99]` -> `99`
3. `nums = [1]` -> `1`
4. `nums = [-2,-2,-2,1]` -> `1`
5. `nums = [7,7,7,3]` -> `3`

### 2. 338. Counting Bits

Given an integer `n`, return an array `ans` of length `n + 1` such that `ans[i]` is the number of 1's in the binary representation of `i`.

**Test Cases:**

1. `n = 2` -> `[0,1,1]`
2. `n = 5` -> `[0,1,1,2,1,2]`
3. `n = 0` -> `[0]`
4. `n = 8` -> `[0,1,1,2,1,2,2,3,1]`
5. `n = 4` -> `[0,1,1,2,1]`

### 3. 201. Bitwise AND of Numbers Range

Given two integers `left` and `right`, return the bitwise AND of all numbers in the range `[left, right]`.

**Test Cases:**

1. `left = 5, right = 7` -> `4`
2. `left = 0, right = 0` -> `0`
3. `left = 1, right = 2147483647` -> `0`
4. `left = 6, right = 6` -> `6`
5. `left = 12, right = 15` -> `12`

### 4. 260. Single Number III

In `nums`, every element appears twice except for two elements which appear exactly once. Return those two elements.

**Test Cases:**

1. `nums = [1,2,1,3,2,5]` -> `[3,5]`
2. `nums = [-1,0]` -> `[-1,0]`
3. `nums = [0,1]` -> `[0,1]`
4. `nums = [2,1,2,3,3,5]` -> `[1,5]`
5. `nums = [1,1,2,3,3,4]` -> `[2,4]`
