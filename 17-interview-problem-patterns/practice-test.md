# Module 17 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 90 minutes

## Part A · Multiple-choice questions

**Q1.** "Find two numbers in a sorted array that add up to T" is best solved with:

- **A.** nested loops
- **B.** opposite-end two pointers
- **C.** a stack
- **D.** DP

**Q2.** In the pair-sum two-pointer method, if a[i] + a[j] < T you should:

- **A.** decrease j
- **B.** increase i
- **C.** stop
- **D.** swap a[i] and a[j]

**Q3.** 3-sum with sorting and two pointers runs in:

- **A.** O(n)
- **B.** O(n log n)
- **C.** O(n²)
- **D.** O(n³)

**Q4.** Removing duplicates from a sorted array in place uses:

- **A.** a hash map
- **B.** reader/writer pointers
- **C.** binary search
- **D.** recursion

**Q5.** The best sum of any 5 consecutive values in an array of n is computed in:

- **A.** O(5n)
- **B.** O(n) with a fixed window
- **C.** O(n²)
- **D.** O(n log n)

**Q6.** For "longest substring without repeating characters", the window is invalid when:

- **A.** it is longer than 26
- **B.** some character appears twice
- **C.** it contains a vowel
- **D.** it ends with a repeat of the first character only

**Q7.** A variable sliding window for sums works when values are:

- **A.** any integers
- **B.** non-negative
- **C.** sorted
- **D.** distinct

**Q8.** For a "shortest valid window" problem, the answer is recorded:

- **A.** after the shrink loop
- **B.** inside the shrink loop, while the window is still valid
- **C.** only at the end
- **D.** before expanding

**Q9.** Floyd's algorithm detects:

- **A.** shortest paths
- **B.** a cycle using two pointers at different speeds
- **C.** the minimum spanning tree
- **D.** balanced parentheses

**Q10.** Binary search on the answer is appropriate for:

- **A.** finding a value in an unsorted array
- **B.** the minimum capacity that makes a task feasible
- **C.** sorting strings
- **D.** counting inversions

**Q11.** Shipping weights [1..10] within 5 days needs minimum capacity:

- **A.** 10
- **B.** 11
- **C.** 15
- **D.** 55

**Q12.** Which overflow-safe midpoint should you use?

- **A.** (lo + hi) / 2
- **B.** lo + (hi − lo) / 2
- **C.** hi / 2
- **D.** (lo * hi) / 2

**Q13.** The number of subarrays with sum k (negatives allowed) is found with:

- **A.** sliding window
- **B.** prefix sums + hash map
- **C.** two pointers on sorted data
- **D.** a heap

**Q14.** Days until a warmer temperature uses:

- **A.** a min-heap
- **B.** a monotonic stack
- **C.** BFS
- **D.** union-find

**Q15.** Cyclic sort works when values are:

- **A.** arbitrary integers
- **B.** in the range 1..n (or 0..n−1)
- **C.** strings
- **D.** sorted already

**Q16.** XOR of all values when every value appears twice except one gives:

- **A.** 0
- **B.** the unpaired value
- **C.** the largest value
- **D.** the sum

**Q17.** Boyer-Moore voting finds the majority element in:

- **A.** O(n) time, O(1) space
- **B.** O(n log n) time
- **C.** O(n) space only
- **D.** O(n²) time

**Q18.** If n ≤ 20, which approach may be acceptable?

- **A.** O(2ⁿ) backtracking or bitmask DP
- **B.** only O(n)
- **C.** only O(log n)
- **D.** none

**Q19.** If n = 10⁶, you should aim for:

- **A.** O(n²)
- **B.** O(n log n) or O(n)
- **C.** O(n³)
- **D.** O(2ⁿ)

**Q20.** The first step of a good interview answer is to:

- **A.** start coding immediately
- **B.** clarify the problem, inputs and edge cases
- **C.** write tests only
- **D.** optimise memory

<details>
<summary><b>Show answers</b></summary>

1. **B**: O(n), O(1) space.
2. **B**: The sum must grow.
3. **C**: n anchors × O(n) scan.
4. **B**: O(1) extra space.
5. **B**: Add one, drop one.
6. **B**: Shrink until unique.
7. **B**: Monotone growth of the sum.
8. **B**: Each shrink step is a valid, shorter candidate.
9. **B**: Tortoise and hare.
10. **B**: Monotone feasibility.
11. **C**: From the program's output.
12. **B**: Avoids int overflow.
13. **B**: Count earlier equal prefixes.
14. **B**: Pop cooler days when a warmer one arrives.
15. **B**: Each value has a home index.
16. **B**: Pairs cancel.
17. **A**: Cancelling votes.
18. **A**: 2²⁰ ≈ 10⁶.
19. **B**: 10¹² operations is far too slow.
20. **B**: Then brute force, then optimise.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Best study streak

A student logs daily study minutes. Find the longest run of consecutive days whose total is **at most** 300 minutes (all values non-negative), using a sliding window.

<details>
<summary>✅ Model solution</summary>

```java
public class StudyStreak {
    static int longestAtMost(int[] a, int limit) {
        int left = 0, sum = 0, best = 0;
        for (int right = 0; right < a.length; right++) {
            sum += a[right];
            while (sum > limit) sum -= a[left++];      // shrink until valid
            best = Math.max(best, right - left + 1);   // longest: record after shrinking
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(longestAtMost(new int[]{90, 30, 120, 60, 200, 20, 40, 50, 10}, 300));
    }
}
```

Output: `4`. Both days 1-4 (90 + 30 + 120 + 60 = 300) and days 6-9 (20 + 40 + 50 + 10 = 120) are 4 days long; any 5-day run exceeds 300. O(n).

</details>

### Scenario 2 · Smallest exam-session capacity

A department must seat batches of students in a fixed order into **at most k** exam sessions, each session holding consecutive batches. Find the smallest session capacity that works. Batches: [40, 25, 35, 30, 50, 20, 45], k = 3.

<details>
<summary>✅ Model solution</summary>

```java
public class HallCapacity {
    static int sessions(int[] b, int cap) {
        int count = 1, load = 0;
        for (int x : b) { if (load + x > cap) { count++; load = 0; } load += x; }
        return count;
    }

    static int minCapacity(int[] b, int k) {
        int lo = 0, hi = 0;
        for (int x : b) { lo = Math.max(lo, x); hi += x; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (sessions(b, mid) <= k) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] b = {40, 25, 35, 30, 50, 20, 45};
        int c = minCapacity(b, 3);
        System.out.println(c + " seats (" + sessions(b, c) + " sessions; " + (c - 1) + " seats would need " + sessions(b, c - 1) + ")");
    }
}
```

Output: `100 seats (3 sessions; 99 seats would need 4)`. At 100 the greedy split is [40, 25, 35], [30, 50, 20], [45]; at 99 it becomes [40, 25], [35, 30], [50, 20], [45]. Feasibility is monotone in capacity, so binary search on the answer is valid: O(n log(total)).

</details>

### Scenario 3 · Missing roll number

Roll numbers 1..n were handed out, but the attendance sheet has n − 1 entries with one missing. Find it in O(n) time and O(1) space with XOR (no sorting, no sum overflow risk).

<details>
<summary>✅ Model solution</summary>

```java
public class MissingRoll {
    static int missing(int[] sheet, int n) {
        int x = 0;
        for (int i = 1; i <= n; i++) x ^= i;          // XOR of all roll numbers
        for (int r : sheet) x ^= r;                   // present ones cancel out
        return x;
    }

    public static void main(String[] args) {
        System.out.println(missing(new int[]{3, 7, 1, 2, 8, 4, 5}, 8));
    }
}
```

Output: `6`. Every present roll number appears twice in the XOR and cancels.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
