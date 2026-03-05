# Greedy Algorithms Deep Dive

A Greedy algorithm builds a solution incrementally by always making the **locally optimal choice** at each step, with the hope — and mathematical guarantee for applicable problems — that a sequence of local optima leads to a global optimum.

## 1. When Does Greedy Work?

Greedy is NOT always correct. It applies when the problem has:

1. **Greedy Choice Property:** A globally optimal solution can be reached by making locally optimal choices. The local choice never invalidates future optimal choices.
2. **Optimal Substructure:** An optimal solution to the problem contains optimal solutions to subproblems (shared with DP, but greedy is simpler when it applies).

> **Greedy vs. DP:** Both require optimal substructure. But greedy makes one irrevocable choice per step without exploring alternatives, while DP explores all possibilities. If a greedy proof exists, greedy is always preferred — it's O(N log N) vs. O(N²) or worse for DP.

**How to verify greedy correctness:** Use an **exchange argument** — prove that swapping any two choices in the greedy solution cannot improve the result. If no swap helps, the greedy order is optimal.

---

## 2. Core Greedy Patterns

### A. Activity Selection / Interval Scheduling
**Problem:** Given intervals, select the maximum number of non-overlapping ones.
**Greedy choice:** Always pick the interval with the **earliest end time**. This leaves the most "room" for future intervals.

```java
Arrays.sort(intervals, (a, b) -> a[1] - b[1]); // sort by END
int count = 1, lastEnd = intervals[0][1];
for (int i = 1; i < intervals.length; i++) {
    if (intervals[i][0] >= lastEnd) {
        count++;
        lastEnd = intervals[i][1];
    }
}
```
This greedy is provably optimal via exchange argument.

### B. Jump Game (Reach-based Greedy)
**Problem:** Can you reach the last index? What's the minimum number of jumps?

```java
// Can reach? Track maximum reachable index
int maxReach = 0;
for (int i = 0; i < nums.length; i++) {
    if (i > maxReach) return false;     // stuck
    maxReach = Math.max(maxReach, i + nums[i]);
}
return true;

// Min jumps: track current "level" boundary and next level boundary
int jumps = 0, curEnd = 0, farthest = 0;
for (int i = 0; i < nums.length - 1; i++) {
    farthest = Math.max(farthest, i + nums[i]);
    if (i == curEnd) { jumps++; curEnd = farthest; }
}
```

### C. Gas Station (Circular Greedy)
**Key insight:** If total `sum(gas) >= sum(cost)`, a solution always exists. The valid starting station is where the running sum last "reset" to 0.

```java
int total = 0, tank = 0, start = 0;
for (int i = 0; i < gas.length; i++) {
    int net = gas[i] - cost[i];
    total += net;
    tank  += net;
    if (tank < 0) { start = i + 1; tank = 0; } // reset
}
return total >= 0 ? start : -1;
```

### D. Task Scheduler
**Insight:** Minimize idle slots driven by the **most frequent task**. Arrange tasks in "rounds" of length `n+1`, always scheduling the most frequent task first.

```java
int[] freq = new int[26];
for (char c : tasks) freq[c - 'A']++;
Arrays.sort(freq);
int maxFreq = freq[25];
int idleSlots = (maxFreq - 1) * (n);
// Fill idle slots with other tasks
for (int i = 24; i >= 0 && idleSlots > 0; i--)
    idleSlots -= Math.min(maxFreq - 1, freq[i]);
idleSlots = Math.max(0, idleSlots);
return tasks.length + idleSlots;
```

---

## 3. Greedy vs. DP Decision Guide

| Situation | Use Greedy | Use DP |
|:---|:---|:---|
| Provably locally optimal = globally optimal | ✅ | |
| Need to explore all possible choices | | ✅ |
| "Make one pass, no recomputation" | ✅ | |
| Overlapping subproblems | | ✅ |

---

## 4. Time Complexity Cheat Sheet

| Problem | Time | Space |
|:---|:---|:---|
| **Activity Selection / Non-overlapping Intervals** | O(N log N) | O(1) |
| **Jump Game I** | O(N) | O(1) |
| **Jump Game II** | O(N) | O(1) |
| **Gas Station** | O(N) | O(1) |
| **Task Scheduler** | O(N + 26 log 26) ≈ O(N) | O(26) = O(1) |
| **Assign Cookies** | O(N log N + M log M) | O(1) |
| **Two City Scheduling** | O(N log N) | O(1) |
