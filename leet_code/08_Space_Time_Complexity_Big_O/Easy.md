# Easy

### 1. 136. Single Number
Given a non-empty array of integers `nums`, every element appears twice except for one. Find that single one. Must be O(N) time and O(1) space.

**Test Cases:**
1. `nums = [2,2,1]` -> `1`
2. `nums = [4,1,2,1,2]` -> `4`
3. `nums = [1]` -> `1`
4. `nums = [-1,-1,-2]` -> `-2`
5. `nums = [0,1,0]` -> `1`

### 2. 169. Majority Element
Return the majority element. Must be O(N) time and O(1) space.

**Test Cases:**
1. `nums = [3,2,3]` -> `3`
2. `nums = [2,2,1,1,1,2,2]` -> `2`
3. `nums = [1]` -> `1`
4. `nums = [6,5,5]` -> `5`
5. `nums = [10,9,9,9,10]` -> `9`

### 3. 217. Contains Duplicate
Time vs Space tradeoff: Solve it in O(N log N) time and O(1) space, or O(N) time and O(N) space.

**Test Cases:**
1. `nums = [1,2,3,1]` -> `true`
2. `nums = [1,2,3,4]` -> `false`
3. `nums = [1,1,1,3,3,4,3,2,4,2]` -> `true`
4. `nums = [0]` -> `false`
5. `nums = [2,2]` -> `true`

### 4. 268. Missing Number
Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the missing number. Must be O(N) time and O(1) space.

**Test Cases:**
1. `nums = [3,0,1]` -> `2`
2. `nums = [0,1]` -> `2`
3. `nums = [9,6,4,2,3,5,7,0,1]` -> `8`
4. `nums = [0]` -> `1`
5. `nums = [1]` -> `0`

### 5. 283. Move Zeroes
Move all `0`s to the end of the array while maintaining relative order. Must be done in-place without copying the array.

**Test Cases:**
1. `nums = [0,1,0,3,12]` -> `[1,3,12,0,0]`
2. `nums = [0]` -> `[0]`
3. `nums = [1,2,3]` -> `[1,2,3]`
4. `nums = [0,0,1]` -> `[1,0,0]`
5. `nums = [4,2,4,0,0,3,0,5,1,0]` -> `[4,2,4,3,5,1,0,0,0,0]`

### 6. 448. Find All Numbers Disappeared in an Array
Return an array of all integers in the range `[1, n]` that do not appear in `nums`. Must be O(N) time and O(1) extra space.

**Test Cases:**
1. `nums = [4,3,2,7,8,2,3,1]` -> `[5,6]`
2. `nums = [1,1]` -> `[2]`
3. `nums = [1]` -> `[]`
4. `nums = [2,2]` -> `[1]`
5. `nums = [1,2,3]` -> `[]`

### 7. 876. Middle of the Linked List
Given the head of a singly linked list, return the middle node of the linked list. Must be done in a single pass O(N) time and O(1) space.

**Test Cases:**
1. `head = [1,2,3,4,5]` -> `[3,4,5]`
2. `head = [1,2,3,4,5,6]` -> `[4,5,6]`
3. `head = [1]` -> `[1]`
4. `head = [1,2]` -> `[2]`
5. `head = [1,2,3]` -> `[2,3]`
