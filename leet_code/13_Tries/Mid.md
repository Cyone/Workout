# Mid

### 1. 208. Implement Trie (Prefix Tree)

Design a Trie data structure with `insert(word)`, `search(word)` (returns true if word is in the Trie), and `startsWith(prefix)` (returns true if any word has this prefix).

**Test Cases:**

1. `insert("apple"), search("apple")` -> `true`
2. `search("app")` -> `false` (after inserting only "apple")
3. `startsWith("app")` -> `true` (after inserting "apple")
4. `insert("app"), search("app")` -> `true`
5. `startsWith("b")` -> `false` (only "apple" and "app" inserted)

### 2. 211. Design Add and Search Words Data Structure

Design a data structure with `addWord(word)` and `search(word)` where `'.'` can match any letter.

**Test Cases:**

1. `addWord("bad"), addWord("dad"), addWord("mad"), search("pad")` -> `false`
2. `search("bad")` -> `true`
3. `search(".ad")` -> `true`
4. `search("b..")` -> `true`
5. `search("...")` -> `true` (matches bad/dad/mad)

### 3. 677. Map Sum Pairs

Design a `MapSum` class with `insert(key, val)` and `sum(prefix)` that returns the sum of all keys' values with the given prefix.

**Test Cases:**

1. `insert("apple", 3), sum("ap")` -> `3`
2. `insert("app", 2), sum("ap")` -> `5`
3. `insert("apple", 5), sum("ap")` -> `7` (apple updated to 5, app=2)
4. `sum("b")` -> `0`
5. `insert("boy", 4), sum("bo")` -> `4`

### 4. 648. Replace Words

Given a sentence and a `dictionary` of root words, replace words in the sentence with their shortest root from the dictionary using a Trie.

**Test Cases:**

1. `dictionary = ["cat","bat","rat"], sentence = "the cattle was rattled by the battery"` -> `"the cat was rat by the bat"`
2. `dictionary = ["a","b","c"], sentence = "aadsfasf absbs bbab cadsfafs"` -> `"a a b c"`
3. `dictionary = ["a","aa","aaa","aaaa"], sentence = "a aa a aaaa aaa aaa aaa aaaaaa bbb baba ababa"` -> `"a a a a a a a a bbb baba a"`
4. `dictionary = ["catt","cat","bat","rat"], sentence = "the cattle was rattled by the battery"` -> `"the cat was rat by the bat"`
5. `dictionary = ["e","k","c","harqr","r","y","lan","fa","d"], sentence = "nlajnlayfuzanynhwng"` -> `"nlajnlayfuzan y nhwng"` (depends on exact words)
