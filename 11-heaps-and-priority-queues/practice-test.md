# Module 11 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** A priority queue always removes:

- **A.** the oldest element
- **B.** the newest element
- **C.** the highest-priority element
- **D.** a random element

**Q2.** Which two properties define a binary min-heap?

- **A.** sorted array and no duplicates
- **B.** complete shape and parent ≤ children
- **C.** BST order and balance
- **D.** full tree and leaves at one level

**Q3.** In 0-based array storage, the children of index 4 are at:

- **A.** 8 and 9
- **B.** 9 and 10
- **C.** 5 and 6
- **D.** 7 and 8

**Q4.** The height of a heap with 1,000 elements is:

- **A.** 9
- **B.** 10
- **C.** 500
- **D.** 999

**Q5.** Inserting into a binary heap costs in the worst case:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q6.** During sift-down, a node is swapped with:

- **A.** its left child always
- **B.** its larger child (in a min-heap)
- **C.** its smaller child (in a min-heap)
- **D.** its parent

**Q7.** Extract-min first moves which element to the root?

- **A.** the smallest leaf
- **B.** the last element of the array
- **C.** the left child of the root
- **D.** a random leaf

**Q8.** Bottom-up heapify starts at index:

- **A.** 0
- **B.** n − 1
- **C.** n/2 − 1 (the last internal node)
- **D.** n/2 + 1

**Q9.** Building a heap from n elements with bottom-up heapify costs:

- **A.** O(n)
- **B.** O(n log n)
- **C.** O(log n)
- **D.** O(n²)

**Q10.** Which statement about a min-heap array is true?

- **A.** it is sorted ascending
- **B.** the first element is the minimum
- **C.** the last element is the maximum
- **D.** siblings are ordered

**Q11.** `PriorityQueue<Integer>` in Java is by default a:

- **A.** max-heap
- **B.** min-heap
- **C.** stack
- **D.** balanced BST

**Q12.** Which constructor creates a max-heap?

- **A.** new PriorityQueue<>(Comparator.reverseOrder())
- **B.** new PriorityQueue<>(100)
- **C.** new PriorityQueue<>(true)
- **D.** new PriorityQueue<>().reverse()

**Q13.** The k largest values of a stream of n values are best kept in:

- **A.** a max-heap of all values
- **B.** a min-heap of size k
- **C.** an unsorted list
- **D.** a queue

**Q14.** Merging 8 sorted files with 1,000,000 records in total costs about:

- **A.** 1,000,000 × 8 comparisons
- **B.** 1,000,000 × log₂ 8 = 3,000,000 comparisons
- **C.** 8 × log₂ 1,000,000
- **D.** 1,000,000²

**Q15.** In the two-heap running median, the median when both heaps are the same size is:

- **A.** the top of the max-heap
- **B.** the top of the min-heap
- **C.** the average of both tops
- **D.** the size of the heaps

**Q16.** `pq.contains(x)` on a PriorityQueue is:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** not available

**Q17.** Why add an arrival counter to a scheduler's comparator?

- **A.** to speed up poll
- **B.** to break ties fairly and deterministically (FIFO among equals)
- **C.** Java requires two keys
- **D.** to reduce memory

**Q18.** Which algorithm does NOT typically use a priority queue?

- **A.** Dijkstra
- **B.** Prim
- **C.** Huffman coding
- **D.** binary search

**Q19.** The comparator `(a, b) -> a - b` is risky because:

- **A.** it sorts descending
- **B.** the subtraction can overflow for large or negative values
- **C.** it is not allowed for Integer
- **D.** it is too slow

**Q20.** Heap sort uses O(1) extra space because:

- **A.** it uses recursion
- **B.** the heap lives inside the input array
- **C.** it copies to a new array
- **D.** it uses a linked list

<details>
<summary><b>Show answers</b></summary>

1. **C**: That is the ADT's definition.
2. **B**: Shape + heap order.
3. **B**: 2·4 + 1 = 9 and 2·4 + 2 = 10.
4. **A**: ⌊log₂ 1000⌋ = 9.
5. **B**: Sift-up follows one root-to-leaf path.
6. **C**: Otherwise heap order would break between the children.
7. **B**: Keeps the tree complete.
8. **C**: Leaves are already heaps.
9. **A**: Σ k · n/2^(k+1) = O(n).
10. **B**: Only the root is guaranteed.
11. **B**: Natural ordering, smallest first.
12. **A**: Reverse the comparator.
13. **B**: O(n log k), O(k) space.
14. **B**: O(N log k).
15. **C**: Even count ⇒ average of the middle two.
16. **C**: Linear scan of the array.
17. **B**: Heaps are not stable.
18. **D**: Binary search uses a sorted array.
19. **B**: Use Integer.compare.
20. **B**: Max-heap in place, sorted suffix grows from the end.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Nearest delivery riders

A food-delivery app knows the (x, y) positions of riders relative to a restaurant at (0, 0). Write `kClosest(int[][] pts, int k)` that returns the k riders closest to the restaurant in O(n log k).

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class NearestRiders {
    static long d2(int[] p) { return (long) p[0] * p[0] + (long) p[1] * p[1]; }

    static List<String> kClosest(int[][] pts, int k) {
        // max-heap on distance: the root is the FARTHEST of the current k
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingLong((int[] p) -> d2(p)).reversed());
        for (int[] p : pts) {
            pq.offer(p);
            if (pq.size() > k) pq.poll();
        }
        List<int[]> res = new ArrayList<>(pq);
        res.sort(Comparator.comparingLong(NearestRiders::d2));
        List<String> out = new ArrayList<>();
        for (int[] p : res) out.add("(" + p[0] + "," + p[1] + ")");
        return out;
    }

    public static void main(String[] args) {
        System.out.println(kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}, {1, 1}, {0, -2}}, 3));
    }
}
```

Output: `[(1,1), (0,-2), (3,3)]` (squared distances 2, 4, 18). Squared distances avoid square roots; `long` avoids overflow.

</details>

### Scenario 2 · Office-hours queue with priorities

Students join an online office-hours queue as `(name, priority, arrival)` where priority 1 means "assignment due today". Write a program that prints the order in which students are served (lowest priority number first, then earliest arrival).

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Comparator;
import java.util.PriorityQueue;

public class OfficeHours {
    record Student(String name, int priority, int arrival) { }

    public static void main(String[] args) {
        PriorityQueue<Student> q = new PriorityQueue<>(
                Comparator.comparingInt(Student::priority).thenComparingInt(Student::arrival));
        q.offer(new Student("Ayesha", 2, 1));
        q.offer(new Student("Bilal", 1, 2));
        q.offer(new Student("Kiran", 3, 3));
        q.offer(new Student("Daniyal", 1, 4));
        q.offer(new Student("Esha", 2, 5));
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) sb.append(q.poll().name()).append(' ');
        System.out.println(sb.toString().trim());
    }
}
```

Output: `Bilal Daniyal Ayesha Esha Kiran`.

</details>

### Scenario 3 · Smallest range covering three sensor feeds

Three sensors report sorted readings. Find the smallest range [a, b] that includes at least one reading from **each** sensor. Use a min-heap holding one cursor per list and track the current maximum.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Comparator;
import java.util.PriorityQueue;

public class SmallestRange {
    static int[] smallestRange(int[][] lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt((int[] c) -> lists[c[0]][c[1]]));
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lists.length; i++) { pq.offer(new int[]{i, 0}); max = Math.max(max, lists[i][0]); }
        int bestLo = 0, bestHi = Integer.MAX_VALUE;
        while (true) {
            int[] c = pq.poll();
            int lo = lists[c[0]][c[1]];
            if ((long) max - lo < (long) bestHi - bestLo) { bestLo = lo; bestHi = max; }
            if (c[1] + 1 == lists[c[0]].length) break;          // one list exhausted: no more full windows
            int next = lists[c[0]][c[1] + 1];
            max = Math.max(max, next);
            pq.offer(new int[]{c[0], c[1] + 1});
        }
        return new int[]{bestLo, bestHi};
    }

    public static void main(String[] args) {
        int[] r = smallestRange(new int[][]{{4, 10, 15, 24, 26}, {0, 9, 12, 20}, {5, 18, 22, 30}});
        System.out.println("[" + r[0] + ", " + r[1] + "]");
    }
}
```

Output: `[20, 24]`. The window always contains one cursor per list; moving the minimum forward is the only way to shrink it. O(N log k).

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
