# Easy

### 1. 217. Contains Duplicate
Given an integer array `nums`, return `true` if any value appears at least twice in the array.

**Test Cases:**
1. `nums = [1,2,3,1]` -> `true`
2. `nums = [1,2,3,4]` -> `false`
3. `nums = [1,1,1,3,3,4,3,2,4,2]` -> `true`
4. `nums = [0]` -> `false`
5. `nums = [2,2]` -> `true`

### 2. 242. Valid Anagram
Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise.

**Test Cases:**
1. `s = "anagram", t = "nagaram"` -> `true`
2. `s = "rat", t = "car"` -> `false`
3. `s = "a", t = "a"` -> `true`
4. `s = "ab", t = "a"` -> `false`
5. `s = "aacc", t = "ccac"` -> `false`

### 3. 349. Intersection of Two Arrays
Given two integer arrays `nums1` and `nums2`, return an array of their intersection.

**Test Cases:**
1. `nums1 = [1,2,2,1], nums2 = [2,2]` -> `[2]`
2. `nums1 = [4,9,5], nums2 = [9,4,9,8,4]` -> `[9,4]`
3. `nums1 = [1], nums2 = [2]` -> `[]`
4. `nums1 = [], nums2 = [1]` -> `[]`
5. `nums1 = [1,2], nums2 = [1,2]` -> `[1,2]`

### 4. 350. Intersection of Two Arrays II
Given two integer arrays `nums1` and `nums2`, return an array of their intersection where each element bounds the minimum appearance frequency.

**Test Cases:**
1. `nums1 = [1,2,2,1], nums2 = [2,2]` -> `[2,2]`
2. `nums1 = [4,9,5], nums2 = [9,4,9,8,4]` -> `[4,9]`
3. `nums1 = [1], nums2 = [2]` -> `[]`
4. `nums1 = [1,1], nums2 = [1,1]` -> `[1,1]`
5. `nums1 = [3,1,2], nums2 = [1,1]` -> `[1]`

### 5. 387. First Unique Character in a String
Given a string `s`, find the first non-repeating character in it and return its index.

**Test Cases:**
1. `s = "leetcode"` -> `0`
2. `s = "loveleetcode"` -> `2`
3. `s = "aabb"` -> `-1`
4. `s = "z"` -> `0`
5. `s = "dddccdbba"` -> `8`

### 6. 705. Design HashSet
Design a HashSet without using any built-in hash table libraries.

**Test Cases:**
1. `["MyHashSet", "add", "add", "contains", "contains"]`, `[[], [1], [2], [1], [3]]` -> `[null, null, null, true, false]`
2. `["MyHashSet", "add", "contains", "remove", "contains"]`, `[[], [2], [2], [2], [2]]` -> `[null, null, true, null, false]`
3. `["MyHashSet", "remove"]`, `[[], [1]]` -> `[null]`
4. `["MyHashSet", "contains"]`, `[[], [1]]` -> `[false]`
5. `["MyHashSet", "add", "add", "remove"]`, `[[], [1], [1], [1]]` -> `[null, null, null]`

### 7. 706. Design HashMap
Design a HashMap without using any built-in hash table libraries.

**Test Cases:**
1. `["MyHashMap", "put", "put", "get", "get"]`, `[[], [1, 1], [2, 2], [1], [3]]` -> `[null, null, null, 1, -1]`
2. `["MyHashMap", "put", "get", "remove", "get"]`, `[[], [2, 2], [2], [2], [2]]` -> `[null, null, 2, null, -1]`
3. `["MyHashMap", "put", "put", "get"]`, `[[], [1, 1], [1, 2], [1]]` -> `[null, null, null, 2]`
4. `["MyHashMap", "remove"]`, `[[], [1]]` -> `[null]`
5. `["MyHashMap", "get"]`, `[[], [1]]` -> `[-1]`
