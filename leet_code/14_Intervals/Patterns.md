# Problem Solving Patterns: Intervals

## Common Patterns
1. **Merge Overlapping Intervals**
2. **Insert Interval**
3. **Non-overlapping / Minimum Removals**
4. **Meeting Rooms (Scheduling)**

## How to Detect These Patterns

### 1. Merge Overlapping Intervals
- **When to use**: Given a list of intervals, combine all overlapping ones into a unified set.
- **Keywords**: "merge intervals", "overlapping ranges", "combine intervals", "union of intervals".
- **Approach**: Sort by start time. Iterate and compare each interval's start against the last merged interval's end; extend if overlapping.
- **Example**: Merge Intervals, Employee Free Time.

### 2. Insert Interval
- **When to use**: A sorted, non-overlapping list of intervals; insert a new interval and merge as needed.
- **Keywords**: "insert interval", "add a range", "sorted intervals plus new entry".
- **Approach**: Collect all intervals ending before the new one, merge overlapping ones, append remaining.
- **Example**: Insert Interval.

### 3. Non-overlapping / Minimum Removals
- **When to use**: Find the minimum number of intervals to remove to make the remaining intervals non-overlapping.
- **Keywords**: "non-overlapping", "minimum removal", "minimum number of arrows", "maximum non-overlapping".
- **Approach**: Sort by end time (greedy). Always keep the interval ending earliest; skip intervals that conflict.
- **Example**: Non-overlapping Intervals, Minimum Number of Arrows to Burst Balloons.

### 4. Meeting Rooms (Scheduling)
- **When to use**: Determine if a person can attend all meetings, or find the minimum number of conference rooms needed.
- **Keywords**: "meeting rooms", "can attend all", "minimum resources", "overlapping meetings".
- **Approach**: For I: Sort and check adjacent overlaps. For II: Use a min-heap of end times to track rooms in use.
- **Example**: Meeting Rooms I (sort), Meeting Rooms II (heap), Employee Free Time.
