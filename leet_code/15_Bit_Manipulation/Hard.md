# Hard

### 1. 421. Maximum XOR of Two Numbers in an Array

Given an integer array `nums`, return the maximum result of `nums[i] XOR nums[j]` where `0 <= i <= j < n`. Must be O(N) using a Trie or bit-by-bit approach.

**Test Cases:**

1. `nums = [3,10,5,25,2,8]` -> `28`
2. `nums = [14,70,53,83,49,91,36,80,92,51,66,70]` -> `127`
3. `nums = [0]` -> `0`
4. `nums = [4,6,7]` -> `3`
5. `nums = [8,10,2]` -> `10`

### 2. 1239. Maximum Length of a Concatenated String with Unique Characters

Given an array of strings `arr`, return the maximum length of a concatenation of a subsequence of strings where all characters are unique. Use bit masking to represent character sets.

**Test Cases:**

1. `arr = ["un","iq","ue"]` -> `4`
2. `arr = ["cha","r","act","ers"]` -> `6`
3. `arr = ["abcdefghijklmnopqrstuvwxyz"]` -> `26`
4. `arr = ["aa","bb"]` -> `0`
5. `arr = ["a","b","c","d"]` -> `4`

### 3. 41. First Missing Positive (Bit-indexed approach)

Given an unsorted integer array `nums`, return the smallest missing positive integer. Must be O(N) time and O(1) space.

**Test Cases:**

1. `nums = [1,2,0]` -> `3`
2. `nums = [3,4,-1,1]` -> `2`
3. `nums = [7,8,9,11,12]` -> `1`
4. `nums = [1]` -> `2`
5. `nums = [1,2,3,4,5]` -> `6`
