# Problem Solving Patterns: Greedy Algorithms

## Common Patterns
1. **Greedy Choice / Activity Selection**
2. **Jump Game (Reach-based Greedy)**
3. **Gas Station / Circular Greedy**
4. **Task Scheduling**

## How to Detect These Patterns

### 1. Greedy Choice / Activity Selection
- **When to use**: A locally optimal choice at each step leads to a globally optimal solution. No overlapping subproblems (unlike DP).
- **Keywords**: "maximum activities", "interval scheduling", "minimum cost", "assign greedily".
- **How to verify greedy applies**: Ask yourself — does taking the best local option ever cost you globally? If not, greedy works.
- **Approach**: Sort by some criterion (end time, value/weight ratio) then greedily pick.
- **Example**: Non-overlapping Intervals (sort by end), Assign Cookies, Lemonade Change.

### 2. Jump Game (Reach-based Greedy)
- **When to use**: From each position you can jump up to a certain number of steps; determine if you can reach the end, or the minimum jumps needed.
- **Keywords**: "jump game", "reach the end", "minimum jumps", "can you reach".
- **Approach**: Track the current maximum reachable index. If current position exceeds max-reach, you're stuck. For minimum jumps, track the current "level" boundary.
- **Example**: Jump Game, Jump Game II.

### 3. Gas Station / Circular Greedy
- **When to use**: Circular array where total supply >= total cost guarantees a solution, and you need the starting point.
- **Keywords**: "gas station", "circular route", "net gain/loss".
- **Approach**: Track running sum. If it goes negative, reset start to the next station (the previous range can't be a valid start).
- **Example**: Gas Station.

### 4. Task Scheduling
- **When to use**: Schedule tasks with cooldown periods to minimise total time, or allocate resources to maximise throughput.
- **Keywords**: "task scheduler", "cooldown", "most frequent task", "CPU scheduling".
- **Approach**: Always process the most frequent remaining task first. Fill idle slots with less frequent tasks.
- **Example**: Task Scheduler, Reorganize String.
