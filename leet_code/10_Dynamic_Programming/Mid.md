# Mid

### 1. 322. Coin Change

You are given an integer array `coins` representing coins of different denominations and an integer `amount`. Return the fewest number of coins needed to make up the amount; return -1 if impossible.

**Test Cases:**

1. `coins = [1,5,11], amount = 15` -> `3` (5+5+5? No: 11+1+… wait: 11+4×1=5, that's 5. Actually coins=[1,5,11], 15=11+4? No 11+5-1. Let's recalc: 5+5+5=3) -> `3`
2. `coins = [1,2,5], amount = 11` -> `3`
3. `coins = [2], amount = 3` -> `-1`
4. `coins = [1], amount = 0` -> `0`
5. `coins = [186,419,83,408], amount = 6249` -> `20`

### 2. 300. Longest Increasing Subsequence

Given an integer array `nums`, return the length of the longest strictly increasing subsequence.

**Test Cases:**

1. `nums = [10,9,2,5,3,7,101,18]` -> `4`
2. `nums = [0,1,0,3,2,3]` -> `4`
3. `nums = [7,7,7,7,7,7,7]` -> `1`
4. `nums = [1,3,6,7,9,4,10,5,6]` -> `6`
5. `nums = [4,3,2,1]` -> `1`

### 3. 62. Unique Paths

A robot is located at the top-left corner of an `m x n` grid. It can only move right or down. Return the number of unique paths to reach the bottom-right corner.

**Test Cases:**

1. `m = 3, n = 7` -> `28`
2. `m = 3, n = 2` -> `3`
3. `m = 1, n = 1` -> `1`
4. `m = 7, n = 3` -> `28`
5. `m = 3, n = 3` -> `6`

### 4. 139. Word Break

Given a string `s` and a dictionary `wordDict`, return `true` if `s` can be segmented into a space-separated sequence of dictionary words.

**Test Cases:**

1. `s = "leetcode", wordDict = ["leet","code"]` -> `true`
2. `s = "applepenapple", wordDict = ["apple","pen"]` -> `true`
3. `s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]` -> `false`
4. `s = "a", wordDict = ["b"]` -> `false`
5. `s = "cars", wordDict = ["car","ca","rs"]` -> `true`

### 5. 416. Partition Equal Subset Sum

Given an integer array `nums`, return `true` if you can partition it into two subsets with equal sum.

**Test Cases:**

1. `nums = [1,5,11,5]` -> `true`
2. `nums = [1,2,3,5]` -> `false`
3. `nums = [2,2,1,1]` -> `true`
4. `nums = [1,2,5]` -> `false`
5. `nums = [3,3,3,4,5]` -> `true`

### 6. 1143. Longest Common Subsequence

Given two strings `text1` and `text2`, return the length of their longest common subsequence.

**Test Cases:**

1. `text1 = "abcde", text2 = "ace"` -> `3`
2. `text1 = "abc", text2 = "abc"` -> `3`
3. `text1 = "abc", text2 = "def"` -> `0`
4. `text1 = "ABCBDAB", text2 = "BDCAB"` -> `4`
5. `text1 = "aggtab", text2 = "gxtxayb"` -> `4`
