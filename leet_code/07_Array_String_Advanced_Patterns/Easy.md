# Easy

### 1. 125. Valid Palindrome
A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.

**Test Cases:**
1. `s = "A man, a plan, a canal: Panama"` -> `true`
2. `s = "race a car"` -> `false`
3. `s = " "` -> `true`
4. `s = "a."` -> `true`
5. `s = "ab"` -> `false`

### 2. 345. Reverse Vowels of a String
Given a string `s`, reverse only all the vowels in the string and return it.

**Test Cases:**
1. `s = "hello"` -> `"holle"`
2. `s = "leetcode"` -> `"leotcede"`
3. `s = "aA"` -> `"Aa"`
4. `s = "a"` -> `"a"`
5. `s = "bcd"` -> `"bcd"`

### 3. 392. Is Subsequence
Given two strings `s` and `t`, return `true` if `s` is a subsequence of `t`, or `false` otherwise.

**Test Cases:**
1. `s = "abc", t = "ahbgdc"` -> `true`
2. `s = "axc", t = "ahbgdc"` -> `false`
3. `s = "", t = "ahbgdc"` -> `true`
4. `s = "a", t = "b"` -> `false`
5. `s = "abc", t = ""` -> `false`

### 4. 643. Maximum Average Subarray I
You are given an integer array `nums` consisting of `n` elements, and an integer `k`. Find a contiguous subarray whose length is equal to `k` that has the maximum average value.

**Test Cases:**
1. `nums = [1,12,-5,-6,50,3], k = 4` -> `12.75000`
2. `nums = [5], k = 1` -> `5.00000`
3. `nums = [0,4,0,3,2], k = 1` -> `4.00000`
4. `nums = [-1], k = 1` -> `-1.00000`
5. `nums = [3,3,4,3,3], k = 3` -> `3.33333`

### 5. 977. Squares of a Sorted Array
Given an integer array `nums` sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.

**Test Cases:**
1. `nums = [-4,-1,0,3,10]` -> `[0,1,9,16,100]`
2. `nums = [-7,-3,2,3,11]` -> `[4,9,9,49,121]`
3. `nums = [0,0,0]` -> `[0,0,0]`
4. `nums = [1]` -> `[1]`
5. `nums = [-5,-4,-3]` -> `[9,16,25]`

### 6. 1768. Merge Strings Alternately
You are given two strings `word1` and `word2`. Merge the strings by adding letters in alternating order, starting with `word1`.

**Test Cases:**
1. `word1 = "abc", word2 = "pqr"` -> `"apbqcr"`
2. `word1 = "ab", word2 = "pqrs"` -> `"apbqrs"`
3. `word1 = "abcd", word2 = "pq"` -> `"apbqcd"`
4. `word1 = "", word2 = "a"` -> `"a"`
5. `word1 = "a", word2 = ""` -> `"a"`

### 7. 2824. Count Pairs Whose Sum is Less than Target
Given a 0-indexed integer array `nums` of length `n` and an integer `target`, return the number of pairs `(i, j)` where `0 <= i < j < n` and `nums[i] + nums[j] < target`.

**Test Cases:**
1. `nums = [-1,1,2,3,1], target = 2` -> `3`
2. `nums = [-6,2,5,-2,-7,-1,3], target = -2` -> `10`
3. `nums = [1,1,1,1], target = 2` -> `0`
4. `nums = [-1,0,1], target = 0` -> `1`
5. `nums = [0,0], target = 1` -> `1`
