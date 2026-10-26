# Module 06 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** A stack follows which order?

- **A.** FIFO
- **B.** LIFO
- **C.** Priority order
- **D.** Random

**Q2.** Sequence: push A, push B, pop, push C, push D, pop, pop. What remains?

- **A.** A
- **B.** B
- **C.** C
- **D.** A and C

**Q3.** In a linked stack the top should be the list's:

- **A.** tail
- **B.** head
- **C.** middle
- **D.** any node

**Q4.** Amortised cost of push on an array stack that doubles:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(√n)

**Q5.** Which situation is NOT naturally modelled by a stack?

- **A.** Undo in an editor
- **B.** Browser back button
- **C.** Printing jobs in arrival order
- **D.** Matching brackets

**Q6.** The JVM stores method frames on:

- **A.** the heap
- **B.** a queue
- **C.** the call stack
- **D.** a hash table

**Q7.** Postfix of `(A + B) * (C - D)`:

- **A.** A B + C D - *
- **B.** A B C D + -
- **C.** + A B - C D
- **D.** A B + * C D -

**Q8.** Prefix of `(A + B) * C`:

- **A.** * + A B C
- **B.** + A * B C
- **C.** A B + C
- **D.** * A + B C

**Q9.** Evaluate postfix `2 3 4 * +`:

- **A.** 20
- **B.** 14
- **C.** 24
- **D.** 9

**Q10.** Evaluate postfix `8 2 - 3 /`:

- **A.** 2
- **B.** 1
- **C.** 6
- **D.** −2

**Q11.** In shunting-yard, when `)` is read:

- **A.** push it
- **B.** pop operators to the output until `(`, then discard `(`
- **C.** output it
- **D.** clear the stack

**Q12.** Which expression requires right-associativity to evaluate correctly?

- **A.** 10 − 4 − 3
- **B.** 2 ^ 3 ^ 2
- **C.** 8 / 4 / 2
- **D.** 1 + 2 + 3

**Q13.** `{[()]}[` is:

- **A.** balanced
- **B.** unbalanced: an opener remains at the end
- **C.** unbalanced: wrong type
- **D.** unbalanced: closer without opener

**Q14.** The space needed to check brackets in a string of length n is at most:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n²)

**Q15.** Next greater elements of `[2, 7, 3, 5, 4, 6, 8]`:

- **A.** [7, 8, 5, 6, 6, 8, -1]
- **B.** [7, -1, 5, 6, 6, 8, -1]
- **C.** [3, 8, 5, 6, 6, 8, -1]
- **D.** [7, 8, 5, 6, 5, 8, -1]

**Q16.** A monotonic stack algorithm on n elements performs at most how many pushes plus pops?

- **A.** n
- **B.** 2n
- **C.** n²
- **D.** n log n

**Q17.** Largest rectangle in histogram `[2, 4]` has area:

- **A.** 2
- **B.** 4
- **C.** 6
- **D.** 8

**Q18.** Min-stack: push 4, 2, 6, then pop. getMin returns:

- **A.** 4
- **B.** 2
- **C.** 6
- **D.** undefined

**Q19.** The legacy `java.util.Stack` class extends:

- **A.** ArrayDeque
- **B.** LinkedList
- **C.** Vector
- **D.** ArrayList

**Q20.** Using two stacks, a queue's dequeue is:

- **A.** O(1) worst case
- **B.** O(1) amortised
- **C.** O(n) always
- **D.** O(log n)

<details>
<summary><b>Show answers</b></summary>

1. **B**: Last In, First Out.
2. **A**: Pops return B, D, C; only A remains.
3. **B**: Removing the head is O(1); removing the tail is O(n).
4. **A**: Resizes are rare enough to average out.
5. **C**: Arrival order is FIFO: a queue.
6. **C**: Each call pushes a frame; returning pops it.
7. **C**: Each bracket becomes a sub-postfix, then *.
8. **C**: The operator comes before its two operands.
9. **B**: 3·4 = 12, then 2 + 12.
10. **A**: (8 − 2) / 3 = 2.
11. **B**: Brackets never appear in the postfix output.
12. **B**: Exponentiation groups from the right.
13. **B**: The final `[` is never closed.
14. **C**: In the worst case every character is an opener.
15. **A**: 7's next greater is 8; 4's is 6.
16. **B**: Each index is pushed once and popped at most once.
17. **B**: max(2·2, 4·1) = 4.
18. **B**: Remaining 4, 2: minimum 2.
19. **C**: Vector is synchronised and allows index access.
20. **B**: Each element is moved between stacks at most once.

</details>

## Part B · Coding scenarios

### Scenario 1 · Validate a code snippet

Given source code as a string, report whether its `()`, `[]` and `{}` are balanced, **ignoring brackets inside string literals** (between double quotes). Example: `print("(:"); if (x) { a[0] = 1; }` is balanced.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class CodeBrackets {
    static boolean balanced(String code) {
        Deque<Character> st = new ArrayDeque<>();
        boolean inString = false;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '"' && (i == 0 || code.charAt(i - 1) != '\\')) { inString = !inString; continue; }
            if (inString) continue;
            if ("([{".indexOf(c) >= 0) st.push(c);
            else if (")]}".indexOf(c) >= 0) {
                if (st.isEmpty() || "([{".indexOf(st.pop()) != ")]}".indexOf(c)) return false;
            }
        }
        return st.isEmpty() && !inString;
    }

    public static void main(String[] args) {
        System.out.println(balanced("print(\"(:\"); if (x) { a[0] = 1; }"));
        System.out.println(balanced("for (int i = 0; i < n; i++) { sum += a[i]; "));
    }
}
```

Output: `true`, `false`. O(n) time, O(n) space.

</details>

### Scenario 2 · Postfix calculator with error handling

Evaluate a space-separated postfix expression of integers and `+ - * /`. Return a clear error message for: too few operands, too many operands left, and division by zero.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class SafePostfix {
    static String eval(String expr) {
        Deque<Long> st = new ArrayDeque<>();
        for (String t : expr.trim().split("\\s+")) {
            if (t.matches("-?\\d+")) { st.push(Long.parseLong(t)); continue; }
            if (st.size() < 2) return "error: not enough operands for " + t;
            long b = st.pop(), a = st.pop();
            switch (t) {
                case "+": st.push(a + b); break;
                case "-": st.push(a - b); break;
                case "*": st.push(a * b); break;
                case "/":
                    if (b == 0) return "error: division by zero";
                    st.push(a / b); break;
                default: return "error: unknown token " + t;
            }
        }
        return st.size() == 1 ? String.valueOf(st.pop()) : "error: " + st.size() + " values left";
    }

    public static void main(String[] args) {
        for (String e : new String[]{"5 1 2 + 4 * + 3 -", "4 +", "1 2 3 +", "7 0 /"})
            System.out.println(e + "  =>  " + eval(e));
    }
}
```

</details>

### Scenario 3 · Car fleet arrival (monotonic stack)

Cars on a one-lane road drive toward a target at `target` km. Car i starts at `position[i]` with `speed[i]`. A faster car that catches a slower one joins its **fleet** and moves at the slower speed. How many fleets arrive?

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class CarFleet {
    static int fleets(int target, int[] position, int[] speed) {
        Integer[] idx = new Integer[position.length];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, (x, y) -> position[y] - position[x]);       // closest to target first
        Deque<Double> st = new ArrayDeque<>();                       // arrival times of fleet leaders
        for (int i : idx) {
            double time = (double) (target - position[i]) / speed[i];
            if (st.isEmpty() || time > st.peek()) st.push(time);     // slower than fleet ahead: new fleet
        }                                                            // else it catches up and merges
        return st.size();
    }

    public static void main(String[] args) {
        System.out.println(fleets(12, new int[]{10, 8, 0, 5, 3}, new int[]{2, 4, 1, 1, 3}));
    }
}
```

Arrival times by position (10, 8, 5, 3, 0): 1, 1, 7, 3, 12 ⇒ fleets {10, 8}, {5, 3}, {0} ⇒ **3**. O(n log n) for the sort.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
