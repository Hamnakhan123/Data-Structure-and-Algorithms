# Module 13 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 90 minutes

## Part A · Multiple-choice questions

**Q1.** A trie stores the words "tea" and "ten". How many nodes do they share below the root?

- **A.** 0
- **B.** 1
- **C.** 2
- **D.** 3

**Q2.** Searching for a word of length 8 in a trie of one million words takes about:

- **A.** 8 steps
- **B.** 20 steps
- **C.** 1,000,000 steps
- **D.** 8,000,000 steps

**Q3.** Which operation does a trie support efficiently but a HashSet does not?

- **A.** exact lookup
- **B.** insertion
- **C.** listing all words with a given prefix
- **D.** deletion

**Q4.** The `pass` counter in a trie node counts:

- **A.** the node's depth
- **B.** the words that go through that node
- **C.** the node's children
- **D.** the deleted words

**Q5.** A binary trie is used to maximise XOR by choosing at each bit:

- **A.** the same bit
- **B.** the opposite bit when possible
- **C.** always 0
- **D.** always 1

**Q6.** With a prefix-sum array, a point update costs:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q7.** A segment tree over n elements is safely stored in an array of size:

- **A.** n
- **B.** 2n
- **C.** 4n
- **D.** n²

**Q8.** In a segment-tree query, a node whose segment lies completely inside [l, r]:

- **A.** is split further
- **B.** returns its stored value
- **C.** returns the identity
- **D.** is ignored

**Q9.** The identity element for a range-maximum segment tree is:

- **A.** 0
- **B.** +∞
- **C.** −∞
- **D.** 1

**Q10.** Building a segment tree bottom-up costs:

- **A.** O(log n)
- **B.** O(n)
- **C.** O(n log n)
- **D.** O(n²)

**Q11.** Lazy propagation stores pending updates:

- **A.** in the leaves only
- **B.** at nodes fully covered by the update range
- **C.** in a separate queue
- **D.** nowhere; it recomputes everything

**Q12.** Range sum after "add v to [l, r]" at a covering node of length len increases the node's sum by:

- **A.** v
- **B.** v × len
- **C.** len
- **D.** v + len

**Q13.** Fenwick trees use which indexing?

- **A.** 0-based
- **B.** 1-based
- **C.** either
- **D.** negative

**Q14.** lowbit(10) equals:

- **A.** 1
- **B.** 2
- **C.** 8
- **D.** 10

**Q15.** prefix(13) in a Fenwick tree visits indices:

- **A.** 13, 12, 8
- **B.** 13, 14, 16
- **C.** 13, 6, 3, 1
- **D.** 1, 2, 4, 8

**Q16.** add(5, d) in a Fenwick tree of size 16 visits:

- **A.** 5, 4, 0
- **B.** 5, 6, 8, 16
- **C.** 5, 10, 20
- **D.** 1..5

**Q17.** Range-update / point-query with a Fenwick tree stores:

- **A.** the original array
- **B.** a difference array
- **C.** sorted values
- **D.** a lazy array

**Q18.** Which problem is NOT suited to a plain Fenwick tree?

- **A.** prefix sums with updates
- **B.** counting inversions
- **C.** range minimum with updates
- **D.** k-th smallest among inserted values

**Q19.** To count inversions of values up to 10⁹, first apply:

- **A.** hashing
- **B.** coordinate compression
- **C.** sorting the original array in place
- **D.** a trie

**Q20.** Compared with a segment tree, a Fenwick tree typically has:

- **A.** more memory and longer code
- **B.** less memory and shorter code
- **C.** support for any associative operation
- **D.** O(1) queries

<details>
<summary><b>Show answers</b></summary>

1. **C**: The prefix "te".
2. **A**: O(L), independent of n.
3. **C**: A HashSet must scan everything.
4. **B**: It gives prefix counts and safe deletion.
5. **B**: Opposite bits produce a 1 in the XOR.
6. **C**: Every later prefix changes.
7. **C**: Enough for non-power-of-two sizes.
8. **B**: Total overlap.
9. **C**: max(x, −∞) = x.
10. **B**: Each of < 4n nodes computed once.
11. **B**: They are pushed down only when needed.
12. **B**: Every element in the segment grows by v.
13. **B**: i & -i is 0 for i = 0.
14. **B**: 10 = 1010₂.
15. **A**: 1101 → 1100 → 1000 → 0.
16. **B**: 0101 → 0110 → 1000 → 10000.
17. **B**: a[i] is the prefix sum of differences.
18. **C**: Min has no inverse.
19. **B**: Map values to ranks 1..m.
20. **B**: n + 1 cells and two loops.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Phone contact search

A phone stores contact names (lowercase). Write a class with `add(String name)` and `count(String prefix)` that returns how many contacts start with the prefix, each in O(L).

<details>
<summary>✅ Model solution</summary>

```java
public class ContactSearch {
    static final class Node {
        Node[] next = new Node[26];
        int pass;
    }

    private final Node root = new Node();

    void add(String name) {
        Node n = root;
        for (char c : name.toCharArray()) {
            if (n.next[c - 'a'] == null) n.next[c - 'a'] = new Node();
            n = n.next[c - 'a'];
            n.pass++;
        }
    }

    int count(String prefix) {
        Node n = root;
        for (char c : prefix.toCharArray()) {
            n = n.next[c - 'a'];
            if (n == null) return 0;
        }
        return n.pass;
    }

    public static void main(String[] args) {
        ContactSearch cs = new ContactSearch();
        for (String s : new String[]{"hamza", "hania", "haris", "huma", "zara"}) cs.add(s);
        System.out.println(cs.count("ha") + " " + cs.count("h") + " " + cs.count("har") + " " + cs.count("x"));
    }
}
```

Output: `3 4 1 0`.

</details>

### Scenario 2 · Stock price range queries

An exchange records closing prices for n days and must answer "lowest price between day l and r" while allowing corrections of single days. Implement an iterative (bottom-up) segment tree for range minimum.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Arrays;

public class PriceMin {
    final int n;
    final int[] t;

    PriceMin(int[] a) {
        n = a.length;
        t = new int[2 * n];
        System.arraycopy(a, 0, t, n, n);                       // leaves at n..2n-1
        for (int i = n - 1; i > 0; i--) t[i] = Math.min(t[2 * i], t[2 * i + 1]);
    }

    void set(int i, int v) {
        for (t[i += n] = v; i > 1; i >>= 1) t[i >> 1] = Math.min(t[i], t[i ^ 1]);
    }

    int min(int l, int r) {                                    // inclusive l..r
        int res = Integer.MAX_VALUE;
        for (l += n, r += n + 1; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) res = Math.min(res, t[l++]);
            if ((r & 1) == 1) res = Math.min(res, t[--r]);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] prices = {120, 115, 130, 110, 125, 140, 105};
        PriceMin pm = new PriceMin(prices);
        System.out.print(pm.min(0, 2) + " " + pm.min(2, 5) + " " + pm.min(0, 6));
        pm.set(3, 135);
        System.out.println(" | after correction: " + pm.min(2, 5) + " " + Arrays.toString(prices));
    }
}
```

Output: `115 110 105 | after correction: 125 [120, 115, 130, 110, 125, 140, 105]`. The iterative tree uses 2n cells and no recursion.

</details>

### Scenario 3 · How unsorted is the merit list?

Two examiners ranked the same 8 candidates. Given the second examiner's ranking expressed as the first examiner's positions, count disagreements (inversions) with a Fenwick tree in O(n log n).

<details>
<summary>✅ Model solution</summary>

```java
public class RankDisagreement {
    static long inversions(int[] a) {
        int n = a.length;
        long[] bit = new long[n + 1];
        long inv = 0;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = a[i] - 1; j > 0; j -= j & -j) inv += bit[j];   // smaller values to the right
            for (int j = a[i]; j <= n; j += j & -j) bit[j]++;
        }
        return inv;
    }

    public static void main(String[] args) {
        System.out.println(inversions(new int[]{2, 1, 3, 5, 4, 8, 6, 7}));
        System.out.println(inversions(new int[]{8, 7, 6, 5, 4, 3, 2, 1}));
    }
}
```

Output: `4` then `28`. Four pairs are ordered differently ((2,1), (5,4), (8,6), (8,7)); a fully reversed list has 8·7/2 = 28.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
