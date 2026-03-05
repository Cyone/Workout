# Hard

### 1. 72. Edit Distance

Given two strings `word1` and `word2`, return the minimum number of operations (insert, delete, replace) to convert `word1` to `word2`.

**Test Cases:**

1. `word1 = "horse", word2 = "ros"` -> `3`
2. `word1 = "intention", word2 = "execution"` -> `5`
3. `word1 = "", word2 = "a"` -> `1`
4. `word1 = "abc", word2 = "abc"` -> `0`
5. `word1 = "sunday", word2 = "saturday"` -> `3`

### 2. 10. Regular Expression Matching

Given an input string `s` and a pattern `p` (with `.` and `*`), implement regular expression matching.

**Test Cases:**

1. `s = "aa", p = "a"` -> `false`
2. `s = "aa", p = "a*"` -> `true`
3. `s = "ab", p = ".*"` -> `true`
4. `s = "aab", p = "c*a*b"` -> `true`
5. `s = "mississippi", p = "mis*is*p*."` -> `false`

### 3. 312. Burst Balloons

You are given `n` balloons, indexed from 0 to n-1. Each balloon is painted with a number on it represented by an array `nums`. Burst all balloons to maximize coins collected.

**Test Cases:**

1. `nums = [3,1,5,8]` -> `167`
2. `nums = [1,5]` -> `10`
3. `nums = [1]` -> `1`
4. `nums = [1,2,3,4]` -> `24` -> actually `60`
5. `nums = [7,9,8,0,7,1,3,5,5,2,3]` -> `1654`

### 4. 115. Distinct Subsequences

Given strings `s` and `t`, return the number of distinct subsequences of `s` which equals `t`.

**Test Cases:**

1. `s = "rabbbit", t = "rabbit"` -> `3`
2. `s = "babgbag", t = "bag"` -> `5`
3. `s = "abc", t = "abc"` -> `1`
4. `s = "abc", t = "d"` -> `0`
5. `s = "aaa", t = "aa"` -> `3`

### 5. 132. Palindrome Partitioning II

Given a string `s`, partition it such that every substring of the partition is a palindrome. Return the minimum cuts needed.

**Test Cases:**

1. `s = "aab"` -> `1`
2. `s = "a"` -> `0`
3. `s = "ab"` -> `1`
4. `s = "aaabaa"` -> `1`
5. `s = "abcba"` -> `0`
