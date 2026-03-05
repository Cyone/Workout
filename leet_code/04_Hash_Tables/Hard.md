# Hard

### 1. 30. Substring with Concatenation of All Words
You are given a string `s` and an array of strings `words`. Find all starting indices of substring(s) in `s` that is a concatenation of each word in `words` exactly once and without any intervening characters.

**Test Cases:**
1. `s = "barfoothefoobarman", words = ["foo","bar"]` -> `[0,9]`
2. `s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"]` -> `[]`
3. `s = "barfoofoobarthefoobarman", words = ["bar","foo","the"]` -> `[6,9,12]`
4. `s = "a", words = ["a"]` -> `[0]`
5. `s = "a", words = ["b"]` -> `[]`

### 2. 149. Max Points on a Line
Given an array of `points` where `points[i] = [xi, yi]` represents a point on the X-Y plane, return the maximum number of points that lie on the same straight line.

**Test Cases:**
1. `points = [[1,1],[2,2],[3,3]]` -> `3`
2. `points = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]` -> `4`
3. `points = [[0,0]]` -> `1`
4. `points = [[0,0],[0,0]]` -> `2`
5. `points = [[1,1],[2,2],[1,1]]` -> `3`
