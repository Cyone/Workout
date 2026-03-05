# Mid

### 1. 49. Group Anagrams
Given an array of strings `strs`, group the anagrams together.

**Test Cases:**
1. `strs = ["eat","tea","tan","ate","nat","bat"]` -> `[["bat"],["nat","tan"],["ate","eat","tea"]]`
2. `strs = [""]` -> `[[""]]`
3. `strs = ["a"]` -> `[["a"]]`
4. `strs = ["ab", "ba"]` -> `[["ab", "ba"]]`
5. `strs = ["a", "b", "c"]` -> `[["a"], ["b"], ["c"]]`

### 2. 347. Top K Frequent Elements
Given an integer array `nums` and an integer `k`, return the `k` most frequent elements.

**Test Cases:**
1. `nums = [1,1,1,2,2,3], k = 2` -> `[1,2]`
2. `nums = [1], k = 1` -> `[1]`
3. `nums = [-1,-1], k = 1` -> `[-1]`
4. `nums = [1,2,2,3,3,3], k = 2` -> `[3,2]`
5. `nums = [4,4,4,4,5,5,6], k = 3` -> `[4,5,6]`

### 3. 560. Subarray Sum Equals K
Given an array of integers `nums` and an integer `k`, return the total number of continuous subarrays whose sum equals to `k`.

**Test Cases:**
1. `nums = [1,1,1], k = 2` -> `2`
2. `nums = [1,2,3], k = 3` -> `2`
3. `nums = [-1,-1,1], k = 0` -> `1`
4. `nums = [1], k = 0` -> `0`
5. `nums = [1], k = 1` -> `1`


### 4. 451. Sort Characters By Frequency
Given a string s, sort it in decreasing order based on the frequency of the characters. The frequency of a character is the number of times it appears in the string.

**Test Cases:**
1. `s = "tree"` -> `"eert"`
2. `s = "cccaaa"` -> `"aaaccc"`

### 5. 128. Longest Consecutive Sequence
Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence. You must write an algorithm that runs in O(n) time.

**Test Cases:**
1. `nums = [100,4,200,1,3,2]` -> `4`
2. `nums = [0,3,7,2,5,8,4,6,0,1]` -> `9`

### 6. 525. Contiguous Array
Given a binary array nums, return the maximum length of a contiguous subarray with an equal number of 0 and 1.

**Test Cases:**
1. `nums = [0,1]` -> `2`
2. `nums = [0,1,0]` -> `2`

### 7. 36. Valid Sudoku
Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the specific rules.

**Test Cases:**
1. `board = [["5","3",".",...]]` -> `true`

### 8. 3. Longest Substring Without Repeating Characters
Given a string s, find the length of the longest substring without repeating characters.

**Test Cases:**
1. `s = "abcabcbb"` -> `3`
2. `s = "bbbbb"` -> `1`

### 9. 567. Permutation in String
Given two strings s1 and s2, return true if s2 contains a permutation of s1, or false otherwise.

**Test Cases:**
1. `s1 = "ab", s2 = "eidbaooo"` -> `true`
2. `s1 = "ab", s2 = "eidboaoo"` -> `false`
