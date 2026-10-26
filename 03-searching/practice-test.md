# Module 03 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** Linear search on 500 unsorted items: average comparisons when the key is present?

- **A.** 250.5
- **B.** 500
- **C.** 9
- **D.** 1

**Q2.** Which data structure makes binary search impractical?

- **A.** Array
- **B.** ArrayList
- **C.** Singly linked list
- **D.** Sorted int[]

**Q3.** Binary search on a sorted array of 64 elements needs at most how many comparisons?

- **A.** 6
- **B.** 7
- **C.** 8
- **D.** 32

**Q4.** In iterative binary search, the loop condition for the closed range [lo, hi] is:

- **A.** `lo < hi`
- **B.** `lo <= hi`
- **C.** `lo != hi`
- **D.** `hi > 0`

**Q5.** Space complexity of recursive binary search:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q6.** Jump search uses a block size of:

- **A.** log n
- **B.** √n
- **C.** n/2
- **D.** 2

**Q7.** Interpolation search performs worst on:

- **A.** uniformly spaced values
- **B.** exponentially growing values like 1, 2, 4, 8, …
- **C.** small arrays
- **D.** arrays with no duplicates

**Q8.** Exponential search first checks indices:

- **A.** 0, n/2, n
- **B.** 1, 2, 4, 8, …
- **C.** 1, 3, 5, 7, …
- **D.** √n, 2√n, …

**Q9.** `lowerBound` returns:

- **A.** the last index with a[i] < key
- **B.** the first index with a[i] ≥ key
- **C.** the first index with a[i] > key
- **D.** −1 if key is absent

**Q10.** For `a = [2, 4, 4, 4, 9]`, `upperBound(4)` is:

- **A.** 3
- **B.** 4
- **C.** 1
- **D.** 5

**Q11.** The number of elements strictly less than 5 in sorted `a` is:

- **A.** `upperBound(5)`
- **B.** `lowerBound(5)`
- **C.** `n − lowerBound(5)`
- **D.** `upperBound(5) − 1`

**Q12.** In a rotated sorted array (distinct values), at every mid:

- **A.** both halves are sorted
- **B.** at least one half is sorted
- **C.** neither half is sorted
- **D.** the left half is always sorted

**Q13.** Index of the minimum of `[30, 40, 50, 10, 20]`:

- **A.** 0
- **B.** 2
- **C.** 3
- **D.** 4

**Q14.** Which problem is NOT naturally solved by binary search on the answer?

- **A.** Minimum ship capacity in D days
- **B.** Integer square root
- **C.** Minimum eating speed
- **D.** Finding the mode (most frequent value) of an unsorted array

**Q15.** For the ship-capacity problem, the smallest sensible capacity to try is:

- **A.** 1
- **B.** the average weight
- **C.** the maximum single parcel weight
- **D.** the total weight

**Q16.** Time of the ship-capacity solution for n parcels with total weight S:

- **A.** O(n)
- **B.** O(log S)
- **C.** O(n log S)
- **D.** O(n²)

**Q17.** What does this loop do if the array is `[5]` and key is 5? `lo=0, hi=0; while(lo<hi){...} return lo;`

- **A.** Infinite loop
- **B.** Returns 0
- **C.** Returns −1
- **D.** Throws an exception

**Q18.** Which mid computation is overflow-safe?

- **A.** `(lo + hi) / 2`
- **B.** `lo + (hi - lo) / 2`
- **C.** `(lo * hi) / 2`
- **D.** `hi / 2 + lo`

**Q19.** `Collections.binarySearch(list, key)` requires the list to be:

- **A.** a LinkedList
- **B.** sorted in the same order as the comparator used
- **C.** unique
- **D.** of size power of two

**Q20.** Peak finding moves right when:

- **A.** a[mid] > a[mid+1]
- **B.** a[mid] < a[mid+1]
- **C.** a[mid] is even
- **D.** mid < n/2

<details>
<summary><b>Show answers</b></summary>

1. **A**: (n + 1) / 2 = 250.5.
2. **C**: No O(1) access to the middle element.
3. **B**: ⌊log₂ 64⌋ + 1 = 7.
4. **B**: A range with lo == hi still has one candidate.
5. **B**: One stack frame per halving.
6. **B**: Balances the number of jumps with the length of the final scan.
7. **B**: Position guesses become badly skewed, degrading towards O(n).
8. **B**: It doubles the bound until it passes the key.
9. **B**: It is the insertion point of key.
10. **B**: The first value greater than 4 (9) is at index 4.
11. **B**: Everything before the first value ≥ 5 is < 5.
12. **B**: The rotation break can be in at most one half.
13. **C**: 10 sits at index 3.
14. **D**: There is no monotone yes/no predicate over a numeric answer range.
15. **C**: A smaller truck could never load the heaviest parcel.
16. **C**: log S feasibility checks, each O(n).
17. **B**: The loop body never runs; lo = 0.
18. **B**: The difference never exceeds the array length.
19. **B**: Otherwise results are undefined.
20. **B**: On a rising slope a peak exists to the right.

</details>

## Part B · Coding scenarios

### Scenario 1 · Exam rank lookup

Given a sorted array of exam scores (ascending, duplicates allowed), answer: "how many students scored strictly more than X?" for many queries in O(log n) each.

<details>
<summary>✅ Model solution</summary>

```java
public class RankLookup {
    static int upperBound(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= key) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    static int countAbove(int[] scores, int x) {
        return scores.length - upperBound(scores, x);
    }

    public static void main(String[] args) {
        int[] scores = {45, 52, 52, 60, 67, 73, 73, 73, 88, 95};
        for (int x : new int[]{50, 73, 100})
            System.out.println("scored more than " + x + ": " + countAbove(scores, x));
    }
}
```

Output: 9, 2, 0. Each query O(log n).

</details>

### Scenario 2 · Version bisect

A software release history has versions 1…n. Some version introduced a bug, and every later version is also buggy. `isBad(v)` is expensive (runs a full test suite). Find the first bad version with as few calls as possible.

<details>
<summary>✅ Model solution</summary>

```java
public class FirstBadVersion {
    static int firstBad = 1_234_567;   // hidden from the algorithm
    static int calls = 0;

    static boolean isBad(int v) { calls++; return v >= firstBad; }

    static int findFirstBad(int n) {
        int lo = 1, hi = n;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (isBad(mid)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println("first bad = " + findFirstBad(2_000_000_000) + " using " + calls + " test runs");
    }
}
```

The predicate "is bad" is monotone, so the first-true template needs ⌈log₂ n⌉ ≈ 31 test runs for two billion versions (this is exactly how `git bisect` works).

</details>

### Scenario 3 · Splitting lecture notes among TAs

A course has `pages[]` for each chapter (in order). Split the chapters into k contiguous groups, one per teaching assistant, so that the **maximum** pages any TA must review is as small as possible. Return that minimum maximum.

<details>
<summary>✅ Model solution</summary>

```java
public class SplitChapters {
    static boolean fits(int[] pages, int k, int limit) {
        int groups = 1, load = 0;
        for (int p : pages) {
            if (p > limit) return false;
            if (load + p > limit) { groups++; load = 0; }
            load += p;
        }
        return groups <= k;
    }

    static int minMaxLoad(int[] pages, int k) {
        int lo = 0, hi = 0;
        for (int p : pages) { lo = Math.max(lo, p); hi += p; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (fits(pages, k, mid)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] pages = {12, 34, 67, 90, 25, 41, 18};
        System.out.println("2 TAs: " + minMaxLoad(pages, 2));
        System.out.println("3 TAs: " + minMaxLoad(pages, 3));
    }
}
```

Monotone predicate (a bigger limit never needs more groups) ⇒ binary search on the answer, O(n log(sum)).

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
