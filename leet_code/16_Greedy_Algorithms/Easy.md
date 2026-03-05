# Easy

### 1. 455. Assign Cookies

Assign cookies to children to maximize the number of content children. Each child has a greed factor; a cookie satisfies a child only if `cookie_size >= greed_factor`.

**Test Cases:**

1. `g = [1,2,3], s = [1,1]` -> `1`
2. `g = [1,2], s = [1,2,3]` -> `2`
3. `g = [10,9,8], s = [5,6,7]` -> `0`
4. `g = [1,2], s = [1,2]` -> `2`
5. `g = [1], s = []` -> `0`

### 2. 860. Lemonade Change

At a lemonade stand, each lemonade costs $5. Customers pay with $5, $10, or $20 bills. Return `true` if you can give correct change to every customer.

**Test Cases:**

1. `bills = [5,5,5,10,20]` -> `true`
2. `bills = [5,5,10,10,20]` -> `false`
3. `bills = [5,5,10]` -> `true`
4. `bills = [10,10]` -> `false`
5. `bills = [5,5,5,5,20,20,5,5,5,5]` -> `false`

### 3. 1710. Maximum Units on a Truck

You are assigned to put some boxes onto a truck. `boxTypes[i] = [numberOfBoxes_i, numberOfUnitsPerBox_i]`. The truck can carry at most `truckSize` boxes. Find the maximum total number of units on the truck.

**Test Cases:**

1. `boxTypes = [[1,3],[2,2],[3,1]], truckSize = 4` -> `8`
2. `boxTypes = [[5,10],[2,5],[4,7],[3,9]], truckSize = 10` -> `91`
3. `boxTypes = [[1,1]], truckSize = 2` -> `1`
4. `boxTypes = [[2,1],[1,2]], truckSize = 3` -> `4`
5. `boxTypes = [[3,2],[2,3]], truckSize = 5` -> `13`
