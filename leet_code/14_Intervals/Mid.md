# Mid

### 1. 56. Merge Intervals

Given an array of `intervals` where `intervals[i] = [start_i, end_i]`, merge all overlapping intervals and return the non-overlapping result.

**Test Cases:**

1. `intervals = [[1,3],[2,6],[8,10],[15,18]]` -> `[[1,6],[8,10],[15,18]]`
2. `intervals = [[1,4],[4,5]]` -> `[[1,5]]`
3. `intervals = [[1,4],[2,3]]` -> `[[1,4]]`
4. `intervals = [[1,4],[0,4]]` -> `[[0,4]]`
5. `intervals = [[1,4],[0,0]]` -> `[[0,0],[1,4]]`

### 2. 57. Insert Interval

You are given an array of non-overlapping `intervals` sorted by start time. Insert `newInterval` and merge if necessary.

**Test Cases:**

1. `intervals = [[1,3],[6,9]], newInterval = [2,5]` -> `[[1,5],[6,9]]`
2. `intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]` -> `[[1,2],[3,10],[12,16]]`
3. `intervals = [], newInterval = [5,7]` -> `[[5,7]]`
4. `intervals = [[1,5]], newInterval = [2,3]` -> `[[1,5]]`
5. `intervals = [[1,5]], newInterval = [6,8]` -> `[[1,5],[6,8]]`

### 3. 435. Non-overlapping Intervals

Given an array of `intervals`, return the minimum number of intervals you need to remove to make the rest non-overlapping.

**Test Cases:**

1. `intervals = [[1,2],[2,3],[3,4],[1,3]]` -> `1`
2. `intervals = [[1,2],[1,2],[1,2]]` -> `2`
3. `intervals = [[1,2],[2,3]]` -> `0`
4. `intervals = [[0,2],[1,3],[1,3],[2,4],[3,5],[3,6]]` -> `4` (actually -> 3)
5. `intervals = [[-100,-87],[-99,-44]]` -> `0`

### 4. 253. Meeting Rooms II

Given an array of meeting time `intervals`, return the minimum number of conference rooms required.

**Test Cases:**

1. `intervals = [[0,30],[5,10],[15,20]]` -> `2`
2. `intervals = [[7,10],[2,4]]` -> `1`
3. `intervals = [[1,5],[2,6],[3,7]]` -> `3`
4. `intervals = [[0,10],[5,15],[10,20]]` -> `2`
5. `intervals = [[1,2],[2,3],[3,4]]` -> `1`
