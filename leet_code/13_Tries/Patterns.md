# Problem Solving Patterns: Tries (Prefix Trees)

## Common Patterns
1. **Insert / Search / StartsWith**
2. **Wildcard / Prefix Matching**
3. **Word Search with Trie Pruning**

## How to Detect These Patterns

### 1. Insert / Search / StartsWith
- **When to use**: You need to efficiently store and look up strings, especially for prefix queries. Operations should be O(L) where L = word length.
- **Keywords**: "autocomplete", "prefix search", "dictionary", "starts with", "word exists".
- **Data structure**: Each TrieNode has an array or map of children (one per character), and a boolean `isEnd`.
- **Example**: Implement Trie (Prefix Tree), Map Sum Pairs.

### 2. Wildcard / Prefix Matching
- **When to use**: You need to search for words using wildcards (`.` matches any letter). Requires DFS through the Trie.
- **Keywords**: "add and search word", "design dictionary", "pattern matching".
- **Approach**: When encountering `.`, recurse into all children at that level.
- **Example**: Add and Search Word — Data structure design (LeetCode 211).

### 3. Word Search with Trie Pruning
- **When to use**: You're given a board and a list of words; find all words present in the board. Brute force is too slow — use Trie to prune dead branches.
- **Keywords**: "find all words in board", "word search II", "dictionary + grid".
- **Approach**: Build a Trie from all target words; DFS on board checking Trie simultaneously to prune.
- **Example**: Word Search II.
