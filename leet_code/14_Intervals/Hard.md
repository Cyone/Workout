# Hard

### 1. 759. Employee Free Time

We are given a list of schedules of employees. Each schedule is a list of non-overlapping intervals the employee is busy. Return the list of finite intervals representing common free time for all employees.

**Test Cases:**

1. `schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]` -> `[[5,6],[7,9]]`
2. `schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]` -> `[[5,6],[7,9]]`
3. `schedule = [[[1,2],[2,3]],[[3,4]]]` -> `[]`
4. `schedule = [[[1,3]],[[3,6]]]` -> `[]`
5. `schedule = [[[1,3],[9,12]],[[2,4]],[[6,8]]]` -> `[[4,6],[8,9]]`

### 2. 452. Minimum Number of Arrows to Burst Balloons

Each balloon is represented by `[xstart, xend]`. An arrow shot at `x` bursts all balloons with `xstart <= x <= xend`. Find the minimum arrows needed to burst all balloons.

**Test Cases:**

1. `points = [[10,16],[2,8],[1,6],[7,12]]` -> `2`
2. `points = [[1,2],[3,4],[5,6],[7,8]]` -> `4`
3. `points = [[1,2],[2,3],[3,4],[4,5]]` -> `2`
4. `points = [[1,2]]` -> `1`
5. `points = [[-2147483646,-2147483645],[2147483646,2147483647]]` -> `2`

### 3. 715. Range Module

A Range Module tracks ranges of numbers. Implement `addRange(left, right)`, `queryRange(left, right)`, `removeRange(left, right)` efficiently.

**Test Cases:**

1. `addRange(10,20), queryRange(10,14)` -> `true`
2. `removeRange(14,16), queryRange(14,16)` -> `false`
3. `queryRange(10,14)` -> `true`
4. `addRange(5,25), queryRange(10,25)` -> `true`
5. `removeRange(5,30), queryRange(5,10)` -> `false`
