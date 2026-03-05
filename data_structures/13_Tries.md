# Tries (Prefix Trees) Deep Dive

A Trie (pronounced "try", from re**trie**val) is a tree-like data structure used to store strings, where each path from the root to a node spells out a prefix. Unlike a hash map, a Trie naturally supports **prefix queries** and **lexicographic ordering** without hashing.

## 1. Structure

Each `TrieNode` contains:
- An array or map of children: one slot per possible character.
- A boolean flag `isEnd` that marks whether this node completes a valid word.

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26]; // for lowercase a-z
    boolean isEnd = false;
}

class Trie {
    TrieNode root = new TrieNode();
}
```

Using an array of size 26 gives O(1) child access using `c - 'a'` as the index. For a larger alphabet (Unicode, etc.), use `HashMap<Character, TrieNode>` instead.

---

## 2. Core Operations

### Insert — O(L)
```java
void insert(String word) {
    TrieNode cur = root;
    for (char c : word.toCharArray()) {
        int idx = c - 'a';
        if (cur.children[idx] == null)
            cur.children[idx] = new TrieNode();
        cur = cur.children[idx];
    }
    cur.isEnd = true;
}
```

### Search — O(L)
```java
boolean search(String word) {
    TrieNode cur = root;
    for (char c : word.toCharArray()) {
        int idx = c - 'a';
        if (cur.children[idx] == null) return false;
        cur = cur.children[idx];
    }
    return cur.isEnd; // must be a complete word
}
```

### StartsWith (Prefix Check) — O(L)
Same as search, but return `true` regardless of `isEnd`. This is the defining advantage of a Trie over a HashSet.

---

## 3. Wildcard Search

For patterns with `.` (matches any character), use DFS through the Trie:

```java
boolean searchWithWildcard(String word, int idx, TrieNode node) {
    if (idx == word.length()) return node.isEnd;
    char c = word.charAt(idx);
    if (c == '.') {
        for (TrieNode child : node.children)
            if (child != null && searchWithWildcard(word, idx + 1, child))
                return true;
        return false;
    } else {
        TrieNode next = node.children[c - 'a'];
        return next != null && searchWithWildcard(word, idx + 1, next);
    }
}
```

---

## 4. Word Search II: Trie + DFS on Grid

The most advanced Trie interview problem. Instead of searching for one word (which would be O(N × 4^L) brute force per word), build a Trie from all target words, then run a single DFS on the grid using the Trie to prune dead paths.

- When the current grid path doesn't match any Trie prefix → backtrack immediately.
- When a Trie node's `isEnd = true` → record the word found.

**Key optimization:** After finding a word, set `node.isEnd = false` to avoid adding duplicates.

---

## 5. Trie vs. HashMap/HashSet Trade-offs

| Feature | Trie | HashMap / HashSet |
|:---|:---|:---|
| **Exact word lookup** | O(L) | O(L) avg |
| **Prefix search** | ✅ O(L) | ❌ Not directly supported |
| **Autocomplete** | ✅ Natural | ❌ Expensive |
| **Memory** | ❌ Higher (node per char) | ✅ Lower |
| **Ordered iteration** | ✅ Lexicographic | ❌ No guaranteed order |

**Use a Trie when:** you need prefix queries, autocomplete, or must search many words simultaneously (Word Search II).
**Use a HashSet when:** you only need exact lookups.

---

## 6. Time & Space Complexity

| Operation | Time | Space |
|:---|:---|:---|
| **Insert** | O(L) | O(L) per word, O(N×L) total |
| **Search** | O(L) | O(1) extra |
| **StartsWith** | O(L) | O(1) extra |
| **Wildcard Search** | O(L × 26^L) worst | O(L) stack |
| **Word Search II** | O(M×N×4^L) worst | O(W×L) for Trie |

*L = word length, N = number of words, M×N = grid size, W = number of words*
