# Hard

### 1. 212. Word Search II

Given an `m x n` board of characters and a list of strings `words`, return all words on the board. Each cell can be used only once. Use a Trie for efficient pruning.

**Test Cases:**

1. `board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]` -> `["eat","oath"]`
2. `board = [["a","b"],["c","d"]], words = ["abcb"]` -> `[]`
3. `board = [["a"]], words = ["a"]` -> `["a"]`
4. `board = [["a","b"],["c","d"]], words = ["ab","cb","ad","bd","ac","ca","da","bc","db","adcb","dabc","abb","acb"]` -> `["ab","ac","bd","ca","db","ad"]`
5. `board = [["o","a","b","n"],["o","t","a","e"],["a","h","k","r"],["a","f","l","v"]], words = ["oa","oaa"]` -> `["oa","oaa"]`

### 2. 745. Prefix and Suffix Search

Design a class `WordFilter` with `f(pref, suff)` that returns the index of the word in `words` which has `pref` as a prefix and `suff` as a suffix. Return -1 if no such word exists.

**Test Cases:**

1. `words = ["apple"], f("a","e")` -> `0`
2. `f("b","")` -> `-1`
3. `f("","e")` -> `0`
4. `words = ["cabaabaaaa","cfababaab","abcaba","baaabaaaaa","bababaaaba"]`, and various prefix/suffix queries
5. `f("apple","apple")` -> `0` (exact match)

### 3. 642. Design Search Autocomplete System

Design an autocomplete system. Given a sentence typed so far, return the top 3 historical hot sentences that have the same prefix. Handle the `'#'` character to indicate sentence completion and storage.

**Test Cases:**

1. `sentences = ["i love you","island","ironman","i love leetcode"], times = [5,3,2,2]`, `input('i')` -> `["i love you","island","i love leetcode"]`
2. `input(' ')` -> `["i love you","i love leetcode"]`
3. `input('a')` -> `[]`
4. `input('#')` -> `[]` (stores "i a")
5. Next inputs use updated frequencies.
