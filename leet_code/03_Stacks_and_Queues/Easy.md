# Easy

### 1. 20. Valid Parentheses
Given a string `s` containing just the characters `(`, `)`, `{`, `}`, `[` and `]`, determine if the input string is valid.

**Test Cases:**
1. `s = "()"` -> `true`
2. `s = "()[]{}"` -> `true`
3. `s = "(]"` -> `false`
4. `s = "([)]"` -> `false`
5. `s = "{[]}"` -> `true`

### 2. 225. Implement Stack using Queues
Implement a last-in-first-out (LIFO) stack using only two queues.

**Test Cases:**
1. `["MyStack", "push", "push", "top", "pop", "empty"]`, `[[], [1], [2], [], [], []]` -> `[null, null, null, 2, 2, false]`
2. `["MyStack", "push", "empty"]`, `[[], [1], []]` -> `[null, null, false]`
3. `["MyStack", "push", "pop", "empty"]`, `[[], [1], [], []]` -> `[null, null, 1, true]`
4. `["MyStack", "push", "push", "pop"]`, `[[], [1], [2], []]` -> `[null, null, null, 2]`
5. `["MyStack", "empty"]`, `[[]]` -> `[null, true]`

### 3. 232. Implement Queue using Stacks
Implement a first in first out (FIFO) queue using only two stacks.

**Test Cases:**
1. `["MyQueue", "push", "push", "peek", "pop", "empty"]`, `[[], [1], [2], [], [], []]` -> `[null, null, null, 1, 1, false]`
2. `["MyQueue", "empty"]`, `[[]]` -> `[null, true]`
3. `["MyQueue", "push", "empty"]`, `[[], [1], []]` -> `[null, null, false]`
4. `["MyQueue", "push", "pop", "empty"]`, `[[], [1], [], []]` -> `[null, null, 1, true]`
5. `["MyQueue", "push", "push", "pop"]`, `[[], [1], [2], []]` -> `[null, null, null, 1]`

### 4. 496. Next Greater Element I
Find the next greater element for specific queries in an array.

**Test Cases:**
1. `nums1 = [4,1,2], nums2 = [1,3,4,2]` -> `[-1,3,-1]`
2. `nums1 = [2,4], nums2 = [1,2,3,4]` -> `[3,-1]`
3. `nums1 = [1], nums2 = [1,2]` -> `[2]`
4. `nums1 = [2], nums2 = [2,1]` -> `[-1]`
5. `nums1 = [1,3], nums2 = [1,2,3,4]` -> `[2,4]`

### 5. 682. Baseball Game
Keep score for a baseball game with specific rules for recording, adding, doubling, and invalidating past scores via a stack.

**Test Cases:**
1. `ops = ["5","2","C","D","+"]` -> `30`
2. `ops = ["5","-2","4","C","D","9","+","+"]` -> `27`
3. `ops = ["1","C"]` -> `0`
4. `ops = ["10","20","+"]` -> `60`
5. `ops = ["-1","-2","-3","+"]` -> `-11`

### 6. 844. Backspace String Compare
Given two strings `s` and `t`, return `true` if they are equal when both are typed into empty text editors. `#` means a backspace.

**Test Cases:**
1. `s = "ab#c", t = "ad#c"` -> `true`
2. `s = "ab##", t = "c#d#"` -> `true`
3. `s = "a#c", t = "b"` -> `false`
4. `s = "a##c", t = "#a#c"` -> `true`
5. `s = "bxj##tw", t = "bxj###tw"` -> `false`

### 7. 1047. Remove All Adjacent Duplicates In String
Remove adjacent duplicate letters in a string until no adjacent duplicates remain.

**Test Cases:**
1. `s = "abbaca"` -> `"ca"`
2. `s = "azxxzy"` -> `"ay"`
3. `s = "a"` -> `"a"`
4. `s = "aa"` -> `""`
5. `s = "abc"` -> `"abc"`
