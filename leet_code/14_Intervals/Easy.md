# Easy

### 1. 252. Meeting Rooms

Given an array of meeting time intervals `intervals` where `intervals[i] = [start_i, end_i]`, determine if a person could attend all meetings.

**Test Cases:**

1. `intervals = [[0,30],[5,10],[15,20]]` -> `false`
2. `intervals = [[7,10],[2,4]]` -> `true`
3. `intervals = []` -> `true`
4. `intervals = [[1,5]]` -> `true`
5. `intervals = [[0,10],[10,20]]` -> `true` (touching but not overlapping)

### 2. 228. Summary Ranges

You are given a sorted unique integer array `nums`. Return the smallest sorted list of ranges that cover all numbers exactly. `[a,b]` represents "a->b", single values represent "a".

**Test Cases:**

1. `nums = [0,1,2,4,5,7]` -> `["0->2","4->5","7"]`
2. `nums = [0,2,3,4,6,8,9]` -> `["0","2->4","6","8->9"]`
3. `nums = []` -> `[]`
4. `nums = [-1]` -> `["-1"]`
5. `nums = [0]` -> `["0"]`
