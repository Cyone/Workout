# Mid

### 1. 78. Subsets

Given an integer array `nums` of unique elements, return all possible subsets (the power set). The solution set must not contain duplicate subsets.

**Test Cases:**

1. `nums = [1,2,3]` -> `[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]`
2. `nums = [0]` -> `[[],[0]]`
3. `nums = [1,2]` -> `[[],[1],[2],[1,2]]`
4. `nums = [1]` -> `[[],[1]]`
5. `nums = [4,1,0]` -> `[[],[4],[1],[4,1],[0],[4,0],[1,0],[4,1,0]]`

### 2. 46. Permutations

Given an array `nums` of distinct integers, return all possible permutations.

**Test Cases:**

1. `nums = [1,2,3]` -> `[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]`
2. `nums = [0,1]` -> `[[0,1],[1,0]]`
3. `nums = [1]` -> `[[1]]`
4. `nums = [1,2]` -> `[[1,2],[2,1]]`
5. `nums = [-1,2,-3]` -> (all 6 permutations)

### 3. 39. Combination Sum

Given an array of distinct integers `candidates` and a `target`, return all unique combinations where the chosen numbers sum to `target`. The same number may be chosen unlimited times.

**Test Cases:**

1. `candidates = [2,3,6,7], target = 7` -> `[[2,2,3],[7]]`
2. `candidates = [2,3,5], target = 8` -> `[[2,2,2,2],[2,3,3],[3,5]]`
3. `candidates = [2], target = 1` -> `[]`
4. `candidates = [1], target = 2` -> `[[1,1]]`
5. `candidates = [3,5,7], target = 10` -> `[[3,7],[5,5]]`

### 4. 17. Letter Combinations of a Phone Number

Given a string containing digits 2–9 inclusive, return all possible letter combinations that the number could represent.

**Test Cases:**

1. `digits = "23"` -> `["ad","ae","af","bd","be","bf","cd","ce","cf"]`
2. `digits = ""` -> `[]`
3. `digits = "2"` -> `["a","b","c"]`
4. `digits = "79"` -> (all combinations of p/q/r/s with w/x/y/z)
5. `digits = "234"` -> (all 27 combinations)

### 5. 79. Word Search

Given an `m x n` grid of characters `board` and a string `word`, return `true` if `word` exists in the grid (horizontally/vertically, not reusing cells).

**Test Cases:**

1. `board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"` -> `true`
2. `board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"` -> `true`
3. `board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"` -> `false`
4. `board = [["a"]], word = "a"` -> `true`
5. `board = [["a","b"],["c","d"]], word = "abdc"` -> `true`
