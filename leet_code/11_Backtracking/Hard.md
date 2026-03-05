# Hard

### 1. 51. N-Queens

Place `n` queens on an `n x n` chessboard such that no two queens attack each other. Return all distinct solutions.

**Test Cases:**

1. `n = 4` -> `[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]`
2. `n = 1` -> `[["Q"]]`
3. `n = 2` -> `[]`
4. `n = 3` -> `[]`
5. `n = 5` -> (10 distinct solutions)

### 2. 37. Sudoku Solver

Write a program to solve a Sudoku puzzle by filling the empty cells (marked as `'.'`). Each row, column, and 3x3 box must contain digits 1–9.

**Test Cases:**

1. Standard 9×9 board with a classic puzzle -> single valid solution.
2. Board with many pre-filled cells -> fast solve.
3. Board with minimum clues (17) -> valid unique solution.

### 3. 131. Palindrome Partitioning

Given a string `s`, partition `s` such that every substring is a palindrome. Return all possible palindrome partitionings.

**Test Cases:**

1. `s = "aab"` -> `[["a","a","b"],["aa","b"]]`
2. `s = "a"` -> `[["a"]]`
3. `s = "aaaa"` -> `[["a","a","a","a"],["a","a","aa"],["a","aa","a"],["aa","a","a"],["aa","aa"],["aaa","a"],["a","aaa"],["aaaa"]]`
4. `s = "ab"` -> `[["a","b"]]`
5. `s = "aba"` -> `[["a","b","a"],["aba"]]`

### 4. 140. Word Break II

Given a string `s` and a dictionary `wordDict`, add spaces to `s` to construct sentences where each word is in the dictionary. Return all such sentences.

**Test Cases:**

1. `s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]` -> `["cats and dog","cat sand dog"]`
2. `s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]` -> `["pine apple pen apple","pineapple pen apple","pine applepen apple"]`
3. `s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]` -> `[]`
4. `s = "a", wordDict = ["a"]` -> `["a"]`
5. `s = "aaaaa", wordDict = ["a","aa","aaa"]` -> (all valid space combinations)
