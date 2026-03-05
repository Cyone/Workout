# Mid

### 1. 150. Evaluate Reverse Polish Notation
Evaluate the value of an arithmetic expression in Reverse Polish Notation.

**Test Cases:**
1. `tokens = ["2","1","+","3","*"]` -> `9`
2. `tokens = ["4","13","5","/","+"]` -> `6`
3. `tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]` -> `22`
4. `tokens = ["18"]` -> `18`
5. `tokens = ["4","2","-"]` -> `2`

### 2. 155. Min Stack
Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

**Test Cases:**
1. `["MinStack","push","push","push","getMin","pop","top","getMin"]`, `[[],[-2],[0],[-3],[],[],[],[]]` -> `[null,null,null,null,-3,null,0,-2]`
2. `["MinStack","push","getMin"]`, `[[],[1],[]]` -> `[null,null,1]`
3. `["MinStack","push","push","getMin"]`, `[[],[2],[1],[]]` -> `[null,null,null,1]`
4. `["MinStack","push","top"]`, `[[],[-1],[]]` -> `[null,null,-1]`
5. `["MinStack","push","push","pop","getMin"]`, `[[],[1],[2],[],[]]` -> `[null,null,null,null,1]`

### 3. 739. Daily Temperatures
Given an array of integers `temperatures`, return an array such that the `ith` element is the number of days you have to wait to get a warmer temperature.

**Test Cases:**
1. `temperatures = [73,74,75,71,69,72,76,73]` -> `[1,1,4,2,1,1,0,0]`
2. `temperatures = [30,40,50,60]` -> `[1,1,1,0]`
3. `temperatures = [30,60,90]` -> `[1,1,0]`
4. `temperatures = [90,80,70]` -> `[0,0,0]`
5. `temperatures = [30]` -> `[0]`


### 4. 901. Online Stock Span
Design a class that collects daily price quotes for some stock and returns the span of that stock's price for the current day.

**Test Cases:**
1. `["StockSpanner","next","next","next","next","next","next","next"]`, `[[],[100],[80],[60],[70],[60],[75],[85]]` -> `[null,1,1,1,2,1,4,6]`

### 5. 394. Decode String
Given an encoded string, return its decoded string. The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times.

**Test Cases:**
1. `s = "3[a]2[bc]"` -> `"aaabcbc"`
2. `s = "3[a2[c]]"` -> `"accaccacc"`

### 6. 622. Design Circular Queue
Design your implementation of the circular queue. The circular queue is a linear data structure in which the operations are performed based on FIFO (First In First Out) principle and the last position is connected back to the first position to make a circle.

**Test Cases:**
1. `["MyCircularQueue","enQueue","enQueue","enQueue","enQueue","Rear","isFull","deQueue","enQueue","Rear"]`, `[[3],[1],[2],[3],[4],[],[],[],[4],[]]` -> `[null,true,true,true,false,3,true,true,true,4]`

### 7. 641. Design Circular Deque
Design your implementation of the circular double-ended queue (deque).

**Test Cases:**
1. `["MyCircularDeque","insertLast","insertLast","insertFront","insertFront","getRear","isFull","deleteLast","insertFront","getFront"]`, `[[3],[1],[2],[3],[4],[],[],[],[4],[]]` -> `[null,true,true,true,false,2,true,true,true,4]`

### 8. 71. Simplify Path
Given a string path, which is an absolute path (starting with a slash '/') to a file or directory in a Unix-style file system, convert it to the simplified canonical path.

**Test Cases:**
1. `path = "/home/"` -> `"/home"`
2. `path = "/../"` -> `"/"`
