# Binary Search Deep Dive

Binary Search is an algorithm that finds the position of a target value in a **sorted** collection by repeatedly halving the search space. It is deceptively simple to describe but surprisingly easy to implement incorrectly (off-by-one errors, infinite loops).

## 1. The Core Algorithm

```java
int binarySearch(int[] nums, int target) {
    int lo = 0, hi = nums.length - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2; // Avoids integer overflow vs. (lo+hi)/2
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) lo = mid + 1;
        else hi = mid - 1;
    }
    return -1; // not found
}
```

> ⚠️ **The two invariant forms:**
> - `while (lo <= hi)` with `hi = mid - 1` / `lo = mid + 1` — for finding an exact match.
> - `while (lo < hi)` with `lo = mid + 1` / `hi = mid` — for finding a boundary (first/last position).

**Time:** O(log N) | **Space:** O(1)

---

## 2. Finding Boundaries: First and Last Occurrence

When the array has duplicates and you need the first or last position of a target:

```java
// Find FIRST occurrence
int findFirst(int[] nums, int target) {
    int lo = 0, hi = nums.length - 1, result = -1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (nums[mid] == target) { result = mid; hi = mid - 1; } // keep looking LEFT
        else if (nums[mid] < target) lo = mid + 1;
        else hi = mid - 1;
    }
    return result;
}

// Find LAST occurrence: when match found, save mid and move lo = mid + 1
```

---

## 3. Binary Search on Rotated Arrays

A rotated sorted array like `[4,5,6,7,0,1,2]` is still partially sorted. Identify which half is sorted, then decide which half to search:

```java
int search(int[] nums, int target) {
    int lo = 0, hi = nums.length - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (nums[mid] == target) return mid;
        // Left half is sorted
        if (nums[lo] <= nums[mid]) {
            if (nums[lo] <= target && target < nums[mid]) hi = mid - 1;
            else lo = mid + 1;
        } else { // Right half is sorted
            if (nums[mid] < target && target <= nums[hi]) lo = mid + 1;
            else hi = mid - 1;
        }
    }
    return -1;
}
```

---

## 4. Binary Search on the Answer (Search Space Reduction)

This is the most powerful and unintuitive application. Instead of searching in an array, you binary search over the **answer range itself**.

**Pattern recognition:** The problem asks you to **minimize the maximum** or **maximize the minimum** of something. You can check a given answer in O(N).

```java
// Template: Binary search on answer
int lo = minPossibleAnswer, hi = maxPossibleAnswer;
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (canAchieve(mid)) hi = mid;   // try to go lower (minimise)
    else lo = mid + 1;
}
return lo;
```

**Examples:**
- *Koko Eating Bananas:* Binary search on eating speed (1 to max pile). `canAchieve(k)` checks if she finishes in ≤ h hours.
- *Capacity to Ship Packages:* Binary search on max daily load. `canAchieve(cap)` checks if all packages ship in ≤ days.
- *Split Array Largest Sum:* Binary search on max subarray sum.

---

## 5. Time Complexity Cheat Sheet

| Variant | Time | Space |
|:---|:---|:---|
| **Classic Binary Search** | O(log N) | O(1) |
| **First/Last Occurrence** | O(log N) | O(1) |
| **Rotated Sorted Array** | O(log N) | O(1) |
| **Search in 2D Matrix** | O(log(M×N)) | O(1) |
| **Binary Search on Answer** | O(log(Range) × N) | O(1) |

> **Golden rule for off-by-one errors:** Write down the invariant (`lo ≤ answer ≤ hi`) and verify it's maintained after every update before coding.
