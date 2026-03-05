# Hard

### 1. 135. Candy

There are `n` children standing in a line, each with a rating in `ratings`. Distribute the minimum number of candies such that each child gets at least 1 and a child with a higher rating than its neighbour gets more candies.

**Test Cases:**

1. `ratings = [1,0,2]` -> `5`
2. `ratings = [1,2,2]` -> `4`
3. `ratings = [1]` -> `1`
4. `ratings = [3,2,1]` -> `6`
5. `ratings = [1,2,87,87,87,2,1]` -> `13`

### 2. 502. IPO

You have `k` capital to start and want to start projects to maximize your final capital. `profits[i]` is the profit and `capital[i]` is the minimum capital needed for project `i`. Return your maximized capital after completing at most `k` projects.

**Test Cases:**

1. `k = 2, w = 0, profits = [1,2,3], capital = [0,1,1]` -> `4`
2. `k = 3, w = 0, profits = [1,2,3], capital = [0,1,2]` -> `6`
3. `k = 1, w = 0, profits = [1], capital = [0]` -> `1`
4. `k = 1, w = 10, profits = [3,5], capital = [0,0]` -> `15`
5. `k = 2, w = 0, profits = [1,2,3,4], capital = [0,1,2,3]` -> `7`

### 3. 1029. Two City Scheduling

A company is planning to interview `2n` people. `costs[i] = [aCosti, bCosti]` is the cost to fly person `i` to city A or B. Return the minimum cost to fly exactly `n` people to each city.

**Test Cases:**

1. `costs = [[10,20],[30,200],[400,50],[30,20]]` -> `110`
2. `costs = [[259,770],[448,54],[926,667],[184,139],[840,118],[577,469]]` -> `1859`
3. `costs = [[515,563],[451,713],[537,709],[343,819],[855,779],[457,60],[650,359]]` -> `3086`
4. `costs = [[1,2],[2,1]]` -> `2`
5. `costs = [[20,30],[30,20],[10,40],[40,10]]` -> `80`
