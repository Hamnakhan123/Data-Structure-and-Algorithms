# Module 14 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** Backtracking is best described as:

- **A.** breadth-first search over states
- **B.** depth-first search over a decision tree that undoes choices
- **C.** sorting followed by binary search
- **D.** a greedy algorithm

**Q2.** The "un-choose" step is needed because:

- **A.** Java requires it
- **B.** the partial solution is shared and must be restored for the next option
- **C.** it frees memory
- **D.** it sorts the result

**Q3.** Permutations of 6 distinct items number:

- **A.** 36
- **B.** 64
- **C.** 720
- **D.** 6

**Q4.** The number of 3-element combinations of 6 items is:

- **A.** 18
- **B.** 20
- **C.** 120
- **D.** 216

**Q5.** Which recursive call allows an element to be reused in combination sum?

- **A.** recurse(i + 1)
- **B.** recurse(i)
- **C.** recurse(0)
- **D.** recurse(start + 1)

**Q6.** To avoid duplicate subsets from input [1, 2, 2] you should:

- **A.** use a HashSet of lists only
- **B.** sort and skip equal siblings at the same level
- **C.** remove duplicates from the input
- **D.** generate permutations instead

**Q7.** Pruning means:

- **A.** removing duplicates from the output
- **B.** abandoning a branch that cannot lead to a valid solution
- **C.** sorting the input
- **D.** limiting recursion depth to 10

**Q8.** Balanced parentheses for n = 3 number:

- **A.** 3
- **B.** 5
- **C.** 6
- **D.** 8

**Q9.** Which input size is realistic for a full n! search?

- **A.** n = 10
- **B.** n = 20
- **C.** n = 50
- **D.** n = 100

**Q10.** The maximum recursion depth when generating subsets of n elements is about:

- **A.** 1
- **B.** n
- **C.** 2ⁿ
- **D.** n!

**Q11.** In N-Queens, the anti-diagonal ("/") of square (r, c) is identified by:

- **A.** r − c
- **B.** r + c
- **C.** r · c
- **D.** max(r, c)

**Q12.** Why index the "\" diagonal array as diag[r − c + n]?

- **A.** to make the index non-negative
- **B.** to sort diagonals
- **C.** to save memory
- **D.** to count solutions

**Q13.** The number of solutions to 8-Queens is:

- **A.** 8
- **B.** 12
- **C.** 64
- **D.** 92

**Q14.** In a Sudoku solver, a digit d may go in (r, c) if:

- **A.** d is not in row r
- **B.** d is not in row r, column c or the 3×3 box of (r, c)
- **C.** d is greater than its neighbours
- **D.** d is odd

**Q15.** The MRV heuristic speeds up Sudoku because it:

- **A.** tries digits in random order
- **B.** picks the cell with the fewest legal digits, failing fast and branching least
- **C.** fills the first row first
- **D.** avoids recursion

**Q16.** Word search marks a cell before exploring neighbours to prevent:

- **A.** stack overflow
- **B.** using the same cell twice in one word
- **C.** finding the word
- **D.** leaving the grid

**Q17.** Graph colouring with 2 colours fails exactly when the graph has:

- **A.** a vertex of degree 3
- **B.** an odd cycle
- **C.** more than 10 vertices
- **D.** no edges

**Q18.** Palindrome partitioning prunes a branch when:

- **A.** the remaining string is empty
- **B.** the current piece is not a palindrome
- **C.** the piece has length 1
- **D.** the output list is full

**Q19.** For "find any one solution", the recursive function should usually return:

- **A.** void
- **B.** a boolean that stops the search once true
- **C.** a count
- **D.** a list of all solutions

**Q20.** When backtracking is still too slow because the same subproblems repeat, you should consider:

- **A.** a bigger stack
- **B.** dynamic programming (memoisation)
- **C.** printing less output
- **D.** using a queue

<details>
<summary><b>Show answers</b></summary>

1. **B**: Choose, explore, un-choose.
2. **B**: Otherwise later branches see stale choices.
3. **C**: 6! = 720.
4. **B**: 6! / (3! 3!) = 20.
5. **B**: Staying at i lets the same candidate be picked again.
6. **B**: Equal values at the same depth create identical branches.
7. **B**: It shrinks the search tree.
8. **B**: The Catalan number C₃ = 5.
9. **A**: 10! ≈ 3.6 million; 20! ≈ 2.4 × 10¹⁸.
10. **B**: One level per element.
11. **B**: Constant along "/".
12. **A**: r − c ranges from −(n−1) to n−1.
13. **D**: 12 of them are fundamentally different.
14. **B**: All three constraints.
15. **B**: Most constrained variable first.
16. **B**: It is restored afterwards.
17. **B**: 2-colourable = bipartite.
18. **B**: No completion can fix a non-palindromic piece.
19. **B**: Propagate success up the call chain.
20. **B**: Module 16.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Snack combinations within a budget

A student has Rs. 160 for snacks priced `[50, 70, 30, 90, 40]`. List every combination of **different** snacks (each at most once) whose total is **exactly** the budget.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnackBudget {
    static void pick(int[] p, int start, int left, List<Integer> cur, List<List<Integer>> out) {
        if (left == 0) { out.add(new ArrayList<>(cur)); return; }
        for (int i = start; i < p.length; i++) {
            if (p[i] > left) break;                         // sorted: nothing later fits
            cur.add(p[i]);
            pick(p, i + 1, left - p[i], cur, out);          // i + 1: each snack once
            cur.remove(cur.size() - 1);
        }
    }

    public static void main(String[] args) {
        int[] prices = {50, 70, 30, 90, 40};
        Arrays.sort(prices);
        List<List<Integer>> out = new ArrayList<>();
        pick(prices, 0, 160, new ArrayList<>(), out);
        System.out.println(out);
    }
}
```

Output: `[[30, 40, 90], [40, 50, 70], [70, 90]]`. Sorting enables the `break`; `i + 1` prevents buying the same snack twice.

</details>

### Scenario 2 · Seating with rivals

Five students A-E sit in a row of five chairs. Rival pairs (A, B) and (C, D) must not sit next to each other. Count the valid seatings with backtracking (permutations + pruning at each placement).

<details>
<summary>✅ Model solution</summary>

```java
public class RivalSeating {
    static boolean[][] rival = new boolean[5][5];
    static int count = 0;

    static void seat(int[] row, boolean[] used, int pos) {
        if (pos == 5) { count++; return; }
        for (int s = 0; s < 5; s++) {
            if (used[s]) continue;
            if (pos > 0 && rival[row[pos - 1]][s]) continue;     // prune: rival on the left
            used[s] = true; row[pos] = s;
            seat(row, used, pos + 1);
            used[s] = false;
        }
    }

    public static void main(String[] args) {
        rival[0][1] = rival[1][0] = true;     // A-B
        rival[2][3] = rival[3][2] = true;     // C-D
        seat(new int[5], new boolean[5], 0);
        System.out.println(count);
    }
}
```

Output: `48`. Of the 120 orders, inclusion-exclusion gives 120 − 48 − 48 + 24 = 48 valid ones (48 with A-B adjacent, 48 with C-D adjacent, 24 with both).

</details>

### Scenario 3 · Rat in a maze: all paths

A rat starts at the top-left of an n × n grid (1 = open, 0 = wall) and must reach the bottom-right moving D, L, R or U without revisiting cells. Print all paths in lexicographic order of moves.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.List;

public class RatMaze {
    static final int[] DR = {1, 0, 0, -1};
    static final int[] DC = {0, -1, 1, 0};
    static final char[] MOVE = {'D', 'L', 'R', 'U'};        // lexicographic order

    static void go(int[][] m, int r, int c, StringBuilder path, List<String> out) {
        int n = m.length;
        if (r == n - 1 && c == n - 1) { out.add(path.toString()); return; }
        m[r][c] = 0;                                          // mark visited
        for (int k = 0; k < 4; k++) {
            int nr = r + DR[k], nc = c + DC[k];
            if (nr >= 0 && nc >= 0 && nr < n && nc < n && m[nr][nc] == 1) {
                path.append(MOVE[k]);
                go(m, nr, nc, path, out);
                path.deleteCharAt(path.length() - 1);
            }
        }
        m[r][c] = 1;                                          // restore
    }

    public static void main(String[] args) {
        int[][] m = {{1, 0, 0, 0}, {1, 1, 0, 1}, {1, 1, 0, 0}, {0, 1, 1, 1}};
        List<String> out = new ArrayList<>();
        if (m[0][0] == 1) go(m, 0, 0, new StringBuilder(), out);
        System.out.println(out);
    }
}
```

Output: `[DDRDRR, DRDDRR]`.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
