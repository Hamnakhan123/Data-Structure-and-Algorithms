# Module 04 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** A sort is stable if:

- **A.** it never crashes
- **B.** equal keys keep their original relative order
- **C.** it uses O(1) memory
- **D.** it runs in O(n log n)

**Q2.** Selection sort on n = 8 makes how many comparisons?

- **A.** 8
- **B.** 28
- **C.** 64
- **D.** 7

**Q3.** After the first pass of selection sort on `[64, 25, 12, 22, 11]`:

- **A.** [11, 25, 12, 22, 64]
- **B.** [25, 12, 22, 11, 64]
- **C.** [11, 12, 22, 25, 64]
- **D.** [25, 64, 12, 22, 11]

**Q4.** Insertion sort performs best on:

- **A.** reversed data
- **B.** random data
- **C.** nearly sorted data
- **D.** data with many duplicates only

**Q5.** Which sort's number of swaps equals the number of inversions?

- **A.** Selection
- **B.** Bubble
- **C.** Heap
- **D.** Quick

**Q6.** Merge sort's time on already sorted input:

- **A.** O(n)
- **B.** O(n log n)
- **C.** O(n²)
- **D.** O(log n)

**Q7.** The merge step of two sorted arrays of sizes 5 and 7 needs at most how many comparisons?

- **A.** 5
- **B.** 7
- **C.** 11
- **D.** 35

**Q8.** Which pivot strategy makes quick sort's expected time O(n log n) on every input?

- **A.** Always the first element
- **B.** Always the last element
- **C.** A uniformly random element
- **D.** The maximum element

**Q9.** Lomuto partition of `[3, 8, 2, 5, 1, 4]` with pivot 4 places 4 at index:

- **A.** 2
- **B.** 3
- **C.** 4
- **D.** 5

**Q10.** Quick sort's average extra space (recursion stack) is:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q11.** Why does 2-way quick sort degrade when all keys are equal?

- **A.** It swaps too much memory
- **B.** Every partition puts all elements on one side
- **C.** Random pivots stop working
- **D.** It becomes unstable

**Q12.** Quickselect finds the k-th smallest in expected:

- **A.** O(log n)
- **B.** O(n)
- **C.** O(n log n)
- **D.** O(k)

**Q13.** In a max-heap stored in an array, where is the largest element?

- **A.** last index
- **B.** index 0
- **C.** middle index
- **D.** anywhere

**Q14.** Parent of index 9 in a 0-based heap:

- **A.** 4
- **B.** 5
- **C.** 3
- **D.** 8

**Q15.** Heap sort's space complexity:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q16.** The lower bound Ω(n log n) applies to:

- **A.** all sorting algorithms
- **B.** comparison-based sorting
- **C.** counting sort
- **D.** radix sort

**Q17.** Counting sort on n = 1,000,000 values in [0, 100] costs about:

- **A.** n log n
- **B.** n + 100
- **C.** n²
- **D.** 100 log n

**Q18.** Radix sort of 6-digit decimal numbers performs how many counting-sort passes?

- **A.** 1
- **B.** 6
- **C.** 10
- **D.** 60

**Q19.** Java's `Arrays.sort(Object[])` uses:

- **A.** quick sort
- **B.** heap sort
- **C.** TimSort (a stable merge/insertion hybrid)
- **D.** radix sort

**Q20.** To sort 10 million records by a 2-letter country code, the most efficient choice is:

- **A.** bubble sort
- **B.** counting sort on the 26×26 possible codes
- **C.** quick sort with last pivot
- **D.** selection sort

<details>
<summary><b>Show answers</b></summary>

1. **B**: Stability is about ties.
2. **B**: n(n−1)/2 = 28.
3. **A**: The minimum 11 swaps with 64.
4. **C**: Time is O(n + inversions).
5. **B**: Every adjacent swap removes exactly one inversion.
6. **B**: It always splits and merges every level.
7. **C**: At most n + m − 1.
8. **C**: No fixed input can consistently trigger bad splits.
9. **B**: Three values (3, 2, 1) are ≤ 4, so the pivot lands at index 3.
10. **B**: Balanced splits give log n depth.
11. **B**: Splits of size n−1 and 0 give O(n²).
12. **B**: It recurses on one side only.
13. **B**: The root holds the maximum.
14. **A**: (9 − 1) / 2 = 4.
15. **A**: It sorts inside the input array.
16. **B**: Non-comparison sorts use key values as indices.
17. **B**: O(n + k).
18. **B**: One per digit.
19. **C**: Objects need stable sorting.
20. **B**: Only 676 distinct keys: O(n + 676), and stable.

</details>

## Part B · Coding scenarios

### Scenario 1 · Merit list with stable tie-breaking

Students arrive as `(name, marks)` in **registration order**. Produce a merit list by marks (descending) where students with equal marks stay in registration order. Implement it with **merge sort** on an array of records.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Arrays;

public class StableMerit {
    record Student(String name, int marks) {}

    static void mergeSort(Student[] a, Student[] buf, int lo, int hi) {
        if (lo >= hi) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, buf, lo, mid);
        mergeSort(a, buf, mid + 1, hi);
        System.arraycopy(a, lo, buf, lo, hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = buf[j++];
            else if (j > hi) a[k] = buf[i++];
            else if (buf[j].marks() > buf[i].marks()) a[k] = buf[j++];   // strictly better only
            else a[k] = buf[i++];                                        // ties: left first = stable
        }
    }

    public static void main(String[] args) {
        Student[] s = {new Student("Areeba", 78), new Student("Bilal", 91), new Student("Chand", 78),
                       new Student("Dua", 91), new Student("Ehsan", 65)};
        mergeSort(s, new Student[s.length], 0, s.length - 1);
        System.out.println(Arrays.toString(s));
    }
}
```

Bilal precedes Dua and Areeba precedes Chand, exactly as they registered. O(n log n).

</details>

### Scenario 2 · Top-3 fastest delivery times

A courier app stores 1,000,000 delivery times (minutes). Return the 3 smallest in sorted order **without** sorting the whole array.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Arrays;
import java.util.Random;

public class TopThree {
    static int[] smallestThree(int[] a) {
        int x = Integer.MAX_VALUE, y = Integer.MAX_VALUE, z = Integer.MAX_VALUE;   // x <= y <= z
        for (int v : a) {
            if (v < x) { z = y; y = x; x = v; }
            else if (v < y) { z = y; y = v; }
            else if (v < z) { z = v; }
        }
        return new int[]{x, y, z};
    }

    public static void main(String[] args) {
        Random r = new Random(5);
        int[] times = new int[1_000_000];
        for (int i = 0; i < times.length; i++) times[i] = 15 + r.nextInt(120);
        times[777] = 7; times[4242] = 9; times[99] = 11;
        System.out.println(Arrays.toString(smallestThree(times)));
    }
}
```

One pass, O(n) time, O(1) space. For general k use quickselect (O(n) expected) or a size-k max-heap (O(n log k), Module 11).

</details>

### Scenario 3 · Sorting phone numbers

Sort 11-digit mobile numbers stored as `long` (e.g. `3062401862L` style numbers without the leading 0) in linear time.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Arrays;

public class PhoneRadix {
    static void radixSort(long[] a) {
        long[] out = new long[a.length];
        long max = Arrays.stream(a).max().orElse(0);
        for (long exp = 1; max / exp > 0; exp *= 10) {
            int[] count = new int[10];
            for (long v : a) count[(int) (v / exp % 10)]++;
            for (int d = 1; d < 10; d++) count[d] += count[d - 1];
            for (int i = a.length - 1; i >= 0; i--) out[--count[(int) (a[i] / exp % 10)]] = a[i];
            System.arraycopy(out, 0, a, 0, a.length);
        }
    }

    public static void main(String[] args) {
        long[] phones = {3331234567L, 3009876543L, 3215550000L, 3001112222L, 3459998888L};
        radixSort(phones);
        System.out.println(Arrays.toString(phones));
    }
}
```

d = 10 digit passes, each O(n + 10) ⇒ O(n). For n in the millions this beats O(n log n) comparison sorts.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
