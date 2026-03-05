# Easy

### 1. 21. Merge Two Sorted Lists
Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.

**Test Cases:**
1. `list1 = [1,2,4], list2 = [1,3,4]` -> `[1,1,2,3,4,4]`
2. `list1 = [], list2 = []` -> `[]`
3. `list1 = [], list2 = [0]` -> `[0]`
4. `list1 = [2], list2 = [1]` -> `[1,2]`
5. `list1 = [1,1], list2 = [2,2]` -> `[1,1,2,2]`

### 2. 83. Remove Duplicates from Sorted List
Given the head of a sorted linked list, delete all duplicates such that each element appears only once.

**Test Cases:**
1. `head = [1,1,2]` -> `[1,2]`
2. `head = [1,1,2,3,3]` -> `[1,2,3]`
3. `head = []` -> `[]`
4. `head = [1,1,1]` -> `[1]`
5. `head = [1,2,3]` -> `[1,2,3]`

### 3. 141. Linked List Cycle
Given `head`, the head of a linked list, determine if the linked list has a cycle in it.

**Test Cases:**
1. `head = [3,2,0,-4], pos = 1` -> `true`
2. `head = [1,2], pos = 0` -> `true`
3. `head = [1], pos = -1` -> `false`
4. `head = [], pos = -1` -> `false`
5. `head = [1,2,3,4,5], pos = 2` -> `true`

### 4. 160. Intersection of Two Linked Lists
Given the heads of two singly linked-lists `headA` and `headB`, return the node at which the two lists intersect.

**Test Cases:**
1. `intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5], skipA = 2, skipB = 3` -> `Reference of the node with value = 8`
2. `intersectVal = 2, listA = [1,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1` -> `Reference of the node with value = 2`
3. `intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2` -> `null`
4. `intersectVal = 1, listA = [1], listB = [1], skipA = 0, skipB = 0` -> `Reference of the node with value = 1`
5. `intersectVal = 0, listA = [], listB = [], skipA = 0, skipB = 0` -> `null`

### 5. 203. Remove Linked List Elements
Given the `head` of a linked list and an integer `val`, remove all the nodes of the linked list that has `Node.val == val`.

**Test Cases:**
1. `head = [1,2,6,3,4,5,6], val = 6` -> `[1,2,3,4,5]`
2. `head = [], val = 1` -> `[]`
3. `head = [7,7,7,7], val = 7` -> `[]`
4. `head = [1,2,3], val = 4` -> `[1,2,3]`
5. `head = [1], val = 1` -> `[]`

### 6. 206. Reverse Linked List
Given the `head` of a singly linked list, reverse the list, and return the reversed list.

**Test Cases:**
1. `head = [1,2,3,4,5]` -> `[5,4,3,2,1]`
2. `head = [1,2]` -> `[2,1]`
3. `head = []` -> `[]`
4. `head = [1]` -> `[1]`
5. `head = [2,3,4]` -> `[4,3,2]`

### 7. 234. Palindrome Linked List
Given the `head` of a singly linked list, return `true` if it is a palindrome.

**Test Cases:**
1. `head = [1,2,2,1]` -> `true`
2. `head = [1,2]` -> `false`
3. `head = []` -> `true`
4. `head = [1]` -> `true`
5. `head = [1,2,3,2,1]` -> `true`
