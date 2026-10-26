# Module 16 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 90 minutes

## Part A · Multiple-choice questions

**Q1.** Which pair of properties indicates that dynamic programming applies?

- **A.** greedy choice and sorting
- **B.** overlapping subproblems and optimal substructure
- **C.** recursion and iteration
- **D.** hashing and sorting

**Q2.** Merge sort is not usually called DP because its subproblems:

- **A.** are too large
- **B.** do not overlap
- **C.** need sorting
- **D.** use recursion

**Q3.** Memoised Fibonacci(n) makes about how many calls?

- **A.** n
- **B.** 2n
- **C.** n²
- **D.** 2ⁿ

**Q4.** Tabulation fills the table:

- **A.** from the answer down to the base cases
- **B.** from base cases upwards, in dependency order
- **C.** randomly
- **D.** only along the diagonal

**Q5.** Climbing 5 stairs taking 1 or 2 steps at a time can be done in how many ways?

- **A.** 5
- **B.** 8
- **C.** 10
- **D.** 13

**Q6.** Paths in a 3 × 3 grid (right/down only, no walls) from corner to corner:

- **A.** 4
- **B.** 6
- **C.** 9
- **D.** 12

**Q7.** The running time of a DP is usually:

- **A.** number of states × work per state
- **B.** number of base cases
- **C.** recursion depth
- **D.** always O(n²)

**Q8.** In the 0/1 knapsack table, dp[i][c] ≠ dp[i−1][c] means:

- **A.** item i was not taken
- **B.** item i was taken
- **C.** capacity c is invalid
- **D.** the table is wrong

**Q9.** 0/1 knapsack in O(nW) is called pseudo-polynomial because:

- **A.** it is exponential in n
- **B.** it depends on the numeric value of W, not its size in bits
- **C.** it uses recursion
- **D.** it is approximate

**Q10.** In LCS, when a[i−1] == b[j−1], dp[i][j] equals:

- **A.** max(dp[i−1][j], dp[i][j−1])
- **B.** dp[i−1][j−1] + 1
- **C.** 0
- **D.** dp[i][j−1] + 1

**Q11.** LCS("AGGTAB", "GXTXAYB") has length:

- **A.** 3
- **B.** 4
- **C.** 5
- **D.** 6

**Q12.** Edit distance from "cat" to "cut" is:

- **A.** 0
- **B.** 1
- **C.** 2
- **D.** 3

**Q13.** To count coin combinations (order ignored), the outer loop iterates over:

- **A.** amounts
- **B.** coins
- **C.** either
- **D.** digits

**Q14.** Minimum coins for 11 with {1, 5, 6, 9} is:

- **A.** 2
- **B.** 3
- **C.** 4
- **D.** 11

**Q15.** The O(n log n) LIS algorithm uses:

- **A.** a stack
- **B.** binary search over a sorted tails array
- **C.** a hash map
- **D.** a heap

**Q16.** Interval DP (e.g. matrix chain) fills the table by:

- **A.** row index only
- **B.** increasing interval length
- **C.** decreasing values
- **D.** random order

**Q17.** Matrices 10×20, 20×30, 30×5: the cheaper order is:

- **A.** (A × B) × C
- **B.** A × (B × C)
- **C.** both equal
- **D.** impossible to tell

**Q18.** Bitmask DP for TSP has how many states for n cities?

- **A.** n!
- **B.** 2ⁿ · n
- **C.** n²
- **D.** n · log n

**Q19.** In tree DP for "no parent and child both chosen", each node returns:

- **A.** one value
- **B.** two values: best with the node and best without it
- **C.** the height
- **D.** the subtree size

**Q20.** Weighted interval scheduling is solved by:

- **A.** earliest-finish greedy
- **B.** DP with dp[i] = max(dp[i−1], pay + dp[p(i)])
- **C.** Huffman coding
- **D.** topological sort

<details>
<summary><b>Show answers</b></summary>

1. **B**: Repeated subproblems whose answers combine.
2. **B**: The two halves are disjoint.
3. **B**: Each value is computed once; each call makes two lookups.
4. **B**: Bottom-up.
5. **B**: 1, 1, 2, 3, 5, 8.
6. **B**: C(4, 2) = 6.
7. **A**: Each state is computed once.
8. **B**: Used in reconstruction.
9. **B**: W can be huge.
10. **B**: Extend the diagonal.
11. **B**: "GTAB".
12. **B**: One substitution.
13. **B**: Avoids counting permutations.
14. **A**: 5 + 6.
15. **B**: Replace the first tail ≥ x.
16. **B**: Short ranges first.
17. **B**: A(BC) = 20·30·5 + 10·20·5 = 4000 vs (AB)C = 6000 + 1500 = 7500.
18. **B**: Visited set × current city.
19. **B**: Include / exclude.
20. **B**: Greedy ignores the weights.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Minimum cost to climb the stairs of the library

Each stair i has a cost (e.g. effort). You can start on stair 0 or 1 and climb 1 or 2 stairs at a time. Paying a stair's cost lets you step from it. Find the minimum total cost to reach the top (beyond the last stair).

<details>
<summary>✅ Model solution</summary>

```java
public class StairCost {
    static int minCost(int[] cost) {
        int a = 0, b = 0;                         // best cost to stand on stair i-2 and i-1 (before paying)
        for (int i = 2; i <= cost.length; i++) {
            int c = Math.min(b + cost[i - 1], a + cost[i - 2]);
            a = b;
            b = c;
        }
        return b;
    }

    public static void main(String[] args) {
        System.out.println(minCost(new int[]{10, 15, 20}));
        System.out.println(minCost(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
    }
}
```

Output: `15` then `6`. State: best cost to reach stair i; O(n) time, O(1) space.

</details>

### Scenario 2 · Exam revision plan (0/1 knapsack)

A student has 10 hours before the exam. Topics have (hours, expected marks): Trees (4, 12), Graphs (5, 15), DP (6, 18), Hashing (2, 7), Sorting (3, 8). Find the maximum expected marks and the topics to revise.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.List;

public class RevisionPlan {
    public static void main(String[] args) {
        String[] topic = {"Trees", "Graphs", "DP", "Hashing", "Sorting"};
        int[] h = {4, 5, 6, 2, 3}, m = {12, 15, 18, 7, 8};
        int H = 10, n = topic.length;
        int[][] dp = new int[n + 1][H + 1];
        for (int i = 1; i <= n; i++)
            for (int c = 0; c <= H; c++) {
                dp[i][c] = dp[i - 1][c];
                if (h[i - 1] <= c) dp[i][c] = Math.max(dp[i][c], dp[i - 1][c - h[i - 1]] + m[i - 1]);
            }
        List<String> chosen = new ArrayList<>();
        for (int i = n, c = H; i > 0; i--)
            if (dp[i][c] != dp[i - 1][c]) { chosen.add(0, topic[i - 1]); c -= h[i - 1]; }
        System.out.println(dp[n][H] + " marks from " + chosen);
    }
}
```

Output: `30 marks from [Trees, DP]` (10 hours). Graphs + Hashing + Sorting also gives 30 in 10 hours: optimal plans need not be unique, and the reconstruction returns one of them.

</details>

### Scenario 3 · Similarity of two DNA-like answer keys

Two answer keys are strings over {A, B, C, D}. Print the LCS length and the edit distance of `"ABCDDCBA"` and `"ABDCCBDA"`.

<details>
<summary>✅ Model solution</summary>

```java
public class AnswerKeys {
    static int lcs(String a, String b) {
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                d[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? d[i - 1][j - 1] + 1 : Math.max(d[i - 1][j], d[i][j - 1]);
        return d[a.length()][b.length()];
    }

    static int edit(String a, String b) {
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) d[i][0] = i;
        for (int j = 0; j <= b.length(); j++) d[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                d[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? d[i - 1][j - 1]
                        : 1 + Math.min(d[i - 1][j - 1], Math.min(d[i - 1][j], d[i][j - 1]));
        return d[a.length()][b.length()];
    }

    public static void main(String[] args) {
        String a = "ABCDDCBA", b = "ABDCCBDA";
        System.out.println("LCS = " + lcs(a, b) + ", edit distance = " + edit(a, b));
    }
}
```

Output: `LCS = 6, edit distance = 3`. Six answers agree in order (e.g. "ABDCBA"), and three single-character edits turn one key into the other.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
