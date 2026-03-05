# Intervals Deep Dive

Interval problems involve ranges `[start, end]` and almost always require either **merging** overlapping ranges or **scheduling** them optimally. They appear constantly in real-world systems: calendar scheduling, resource allocation, network packet merging, and database range scans.

## 1. The Foundation: Sorting

Almost every interval problem begins with **sorting by start time** (or sometimes by end time for greedy scheduling). Without sorting, you'd need an O(N²) comparison of all pairs — sorting makes the problem tractable in O(N log N).

---

## 2. Core Patterns

### A. Merge Overlapping Intervals
**Condition for overlap:** `currentInterval.start <= lastMerged.end`
```java
Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // sort by start
List<int[]> merged = new ArrayList<>();
for (int[] interval : intervals) {
    if (merged.isEmpty() || interval[0] > merged.get(merged.size()-1)[1]) {
        merged.add(interval);                          // no overlap: add
    } else {
        merged.get(merged.size()-1)[1] =
            Math.max(merged.get(merged.size()-1)[1], interval[1]); // overlap: extend
    }
}
```
**Time:** O(N log N) sort + O(N) merge = **O(N log N)**

### B. Insert Interval into Sorted Non-overlapping List
Three phases:
1. Collect all intervals that end **before** the new interval starts (no overlap).
2. Merge all intervals that overlap with the new one (extend boundaries).
3. Collect remaining intervals that start **after** the new interval ends.

```java
// Phase 2 merge condition: interval[0] <= newEnd (still overlaps)
newStart = Math.min(newStart, interval[0]);
newEnd   = Math.max(newEnd,   interval[1]);
```

### C. Non-overlapping Intervals (Minimum Removals)
Sort by **end time** (greedy: always keep the interval that ends soonest — it leaves the most room for future intervals).

```java
Arrays.sort(intervals, (a, b) -> a[1] - b[1]); // sort by END
int removals = 0, lastEnd = Integer.MIN_VALUE;
for (int[] interval : intervals) {
    if (interval[0] >= lastEnd) lastEnd = interval[1]; // compatible, keep it
    else removals++;                                    // conflict, remove current
}
```
- Same logic applies to **Minimum Arrows to Burst Balloons** (each "arrow" bursts all overlapping balloons at the same x-coordinate).

### D. Meeting Rooms II (Minimum Resources Needed)
Sort meetings by **start time**. Use a **min-heap of end times** to track when the earliest-ending meeting finishes:
- If a new meeting starts ≥ the earliest end → reuse that room (replace end time).
- Otherwise → allocate a new room (add new end time).

```java
Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
PriorityQueue<Integer> ends = new PriorityQueue<>();
for (int[] interval : intervals) {
    if (!ends.isEmpty() && ends.peek() <= interval[0]) ends.poll(); // room free
    ends.offer(interval[1]); // assign this meeting to a room
}
return ends.size(); // rooms in use = answer
```

---

## 3. Two-Pointer Approach for Merge/Find Overlap

When given **two sorted lists** of intervals, a two-pointer approach finds intersections in O(M + N):
```java
// Advance the pointer with the smaller end time
if (list1[i][1] < list2[j][1]) i++;
else j++;
```

---

## 4. Common Interview Traps

| Trap | Solution |
|:---|:---|
| Touching intervals (e.g. `[1,2],[2,3]`) — should they merge? | Read problem carefully. LeetCode #56 merges, #452 (arrows) does not. |
| Integer overflow in comparator `(a,b) -> a[0] - b[0]` | Use `Integer.compare(a[0], b[0])` to be safe |
| Forgetting to sort before processing | Always sort first unless explicitly told it's pre-sorted |

---

## 5. Time Complexity Cheat Sheet

| Problem | Time | Space |
|:---|:---|:---|
| **Merge Intervals** | O(N log N) | O(N) |
| **Insert Interval** | O(N) (pre-sorted input) | O(N) |
| **Non-overlapping / Min Arrows** | O(N log N) | O(1) |
| **Meeting Rooms I** | O(N log N) | O(1) |
| **Meeting Rooms II** | O(N log N) | O(N) heap |
| **Interval List Intersections** | O(M + N) | O(1) |
