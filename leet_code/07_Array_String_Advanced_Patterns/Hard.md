# Hard

### 1. 76. Minimum Window Substring
Given two strings `s` and `t`, return the minimum window substring of `s` such that every character in `t` (including duplicates) is included in the window.

**Test Cases:**
1. `s = "ADOBECODEBANC", t = "ABC"` -> `"BANC"`
2. `s = "a", t = "a"` -> `"a"`
3. `s = "a", t = "aa"` -> `""`
4. `s = "aaaaaaaaaaaabbbbbcdd", t = "abcdd"` -> `"abbbbbcdd"`
5. `s = "ab", t = "b"` -> `"b"`

### 2. 239. Sliding Window Maximum
You are given an array of integers `nums`, there is a sliding window of size `k`. Return the max sliding window.

**Test Cases:**
1. `nums = [1,3,-1,-3,5,3,6,7], k = 3` -> `[3,3,5,5,6,7]`
2. `nums = [1], k = 1` -> `[1]`
3. `nums = [1,-1], k = 1` -> `[1,-1]`
4. `nums = [9,11], k = 2` -> `[11]`
5. `nums = [4,-2], k = 2` -> `[4]`
