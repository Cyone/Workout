# Easy

### 1. 401. Binary Watch

A binary watch has 4 LEDs for hours (0–11) and 6 LEDs for minutes (0–59). Given `turnedOn` (number of LEDs lit), return all possible times in "h:mm" format.

**Test Cases:**

1. `turnedOn = 1` -> `["0:01","0:02","0:04","0:08","0:16","0:32","1:00","2:00","4:00","8:00"]`
2. `turnedOn = 9` -> `[]`
3. `turnedOn = 0` -> `["0:00"]`
4. `turnedOn = 2` -> (all times with exactly 2 bits set)
5. `turnedOn = 10` -> `[]`

### 2. 784. Letter Case Permutation

Given a string `s`, transform every letter individually to be lowercase or uppercase to create another string. Return a list of all possible strings we could create.

**Test Cases:**

1. `s = "a1b2"` -> `["a1b2","a1B2","A1b2","A1B2"]`
2. `s = "3z4"` -> `["3z4","3Z4"]`
3. `s = "12345"` -> `["12345"]`
4. `s = "C"` -> `["c","C"]`
5. `s = "aB"` -> `["ab","aB","Ab","AB"]`
