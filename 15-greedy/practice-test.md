# Module 15 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** Which pair of properties makes a greedy algorithm correct?

- **A.** overlapping subproblems and memoisation
- **B.** greedy-choice property and optimal substructure
- **C.** recursion and backtracking
- **D.** sorting and hashing

**Q2.** A single counterexample is enough to:

- **A.** prove a greedy rule correct
- **B.** prove a greedy rule incorrect
- **C.** compute the optimum
- **D.** find the time complexity

**Q3.** Greedy change for Rs 76 with PKR denominations uses:

- **A.** 50, 20, 5, 1
- **B.** 50, 10, 10, 5, 1
- **C.** 20, 20, 20, 10, 5, 1
- **D.** 50, 25, 1

**Q4.** With coins {1, 5, 6, 9}, greedy for 11 gives 9+1+1 but the optimum is:

- **A.** 6+5 (2 coins)
- **B.** 9+1+1 (3 coins)
- **C.** 5+5+1 (3 coins)
- **D.** 11 is impossible

**Q5.** Fractional knapsack runs in:

- **A.** O(n)
- **B.** O(n log n)
- **C.** O(nW)
- **D.** O(2ⁿ)

**Q6.** The best algorithm for the 0/1 knapsack in general is:

- **A.** greedy by ratio
- **B.** greedy by value
- **C.** dynamic programming
- **D.** binary search

**Q7.** In the exchange argument for fractional knapsack, we swap:

- **A.** whole items
- **B.** one unit of a lower-ratio item for one unit of a higher-ratio item
- **C.** two capacities
- **D.** the order of sorting

**Q8.** Jump game [3, 2, 1, 0, 4] is unreachable because:

- **A.** the array is too short
- **B.** every path gets stuck at index 3 (jump length 0)
- **C.** the last value is 4
- **D.** the first value is odd

**Q9.** In the gas-station problem, if the tank goes negative at station i, the next start candidate is:

- **A.** 0
- **B.** i
- **C.** i + 1
- **D.** the station with most gas

**Q10.** Huffman coding assigns the shortest code to:

- **A.** the rarest symbol
- **B.** the most frequent symbol
- **C.** the first symbol alphabetically
- **D.** every symbol equally

**Q11.** Huffman's algorithm uses which data structure?

- **A.** stack
- **B.** queue
- **C.** min-priority queue
- **D.** hash table only

**Q12.** Which set of codes is prefix-free?

- **A.** {0, 01, 11}
- **B.** {0, 10, 11}
- **C.** {1, 10, 0}
- **D.** {00, 0, 1}

**Q13.** With 8 equally frequent symbols, Huffman codes have length:

- **A.** 1
- **B.** 2
- **C.** 3
- **D.** 8

**Q14.** For maximum non-overlapping intervals, sort by:

- **A.** start time
- **B.** end time
- **C.** length
- **D.** number of overlaps

**Q15.** The proof technique "greedy stays ahead" shows that:

- **A.** greedy uses less memory
- **B.** after each step greedy's partial solution is at least as good as the optimal one's
- **C.** greedy is faster than DP
- **D.** greedy explores all branches

**Q16.** Merging intervals first requires sorting by:

- **A.** end time
- **B.** start time
- **C.** length
- **D.** midpoint

**Q17.** Intervals [1,4], [2,5], [6,8]: minimum arrows needed is:

- **A.** 1
- **B.** 2
- **C.** 3
- **D.** 0

**Q18.** Minimum removals for non-overlap equals:

- **A.** number of overlaps
- **B.** n minus the maximum non-overlapping subset
- **C.** n / 2
- **D.** number of distinct start times

**Q19.** Weighted interval scheduling (each talk has a value) is solved by:

- **A.** earliest finish greedy
- **B.** dynamic programming
- **C.** Huffman coding
- **D.** BFS

**Q20.** Which earlier algorithm is NOT greedy?

- **A.** Dijkstra
- **B.** Kruskal
- **C.** Prim
- **D.** Floyd-Warshall

<details>
<summary><b>Show answers</b></summary>

1. **B**: The greedy choice is safe and the rest is a smaller instance.
2. **B**: One failing input disproves "always optimal".
3. **A**: Largest note that fits each time.
4. **A**: Non-canonical system.
5. **B**: Sorting by ratio dominates.
6. **C**: Greedy can waste capacity.
7. **B**: Value never decreases.
8. **B**: The farthest reach stops at 3.
9. **C**: No start before i can pass i.
10. **B**: Minimises Σ frequency × length.
11. **C**: Repeatedly extract the two smallest weights.
12. **B**: No codeword starts another.
13. **C**: A perfect tree of depth 3.
14. **B**: Earliest finish leaves the most room.
15. **B**: Used for interval scheduling.
16. **B**: Then extend the last merged interval.
17. **B**: One at 4 (hits the first two), one at 8.
18. **B**: Keep as many as possible.
19. **B**: Greedy cannot weigh value against time.
20. **D**: Floyd-Warshall is dynamic programming.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Fewest notes from an ATM

An ATM holds unlimited notes of Rs 5000, 1000, 500, 100 and 50. Write `notes(int amount)` that returns how many of each note to dispense with the fewest notes (amount is a multiple of 50), and explain why greedy is safe here.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class AtmNotes {
    static Map<Integer, Integer> notes(int amount) {
        int[] d = {5000, 1000, 500, 100, 50};
        Map<Integer, Integer> out = new LinkedHashMap<>();
        for (int x : d) {
            if (amount >= x) { out.put(x, amount / x); amount %= x; }
        }
        return out;
    }

    public static void main(String[] args) {
        System.out.println(notes(17850));
        System.out.println(notes(650));
    }
}
```

Output: `{5000=3, 1000=2, 500=1, 100=3, 50=1}` then `{500=1, 100=1, 50=1}`. Every note is a multiple of the next smaller one (5000 = 5×1000, 1000 = 2×500, 500 = 5×100, 100 = 2×50), so any combination of smaller notes worth at least a larger note can be replaced by fewer notes: the system is canonical and greedy is optimal.

</details>

### Scenario 2 · Lecture-hall booking

Given requests `{start, end}` for one lecture hall, return the **names** of the maximum number of compatible bookings (half-open intervals) using earliest finish time.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HallBooking {
    record Req(String who, int start, int end) { }

    static List<String> book(List<Req> reqs) {
        List<Req> s = new ArrayList<>(reqs);
        s.sort(Comparator.comparingInt(Req::end));
        List<String> out = new ArrayList<>();
        int lastEnd = Integer.MIN_VALUE;
        for (Req r : s) if (r.start() >= lastEnd) { out.add(r.who()); lastEnd = r.end(); }
        return out;
    }

    public static void main(String[] args) {
        List<Req> reqs = List.of(new Req("Quiz", 8, 9), new Req("Seminar", 8, 12), new Req("Viva", 9, 11),
                new Req("Club", 10, 13), new Req("Lab demo", 11, 12), new Req("Workshop", 12, 15));
        System.out.println(book(reqs));
    }
}
```

Output: `[Quiz, Viva, Lab demo, Workshop]`. O(n log n).

</details>

### Scenario 3 · Encode a short message with Huffman

Write a program that builds a Huffman code for "mississippi river", prints each symbol's code and the total number of bits, and verifies that decoding gives back the message.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class HuffmanMessage {
    static final class N {
        char c; int f; N l, r; int id;
        N(char c, int f, N l, N r, int id) { this.c = c; this.f = f; this.l = l; this.r = r; this.id = id; }
    }

    static void codes(N n, String p, Map<Character, String> m) {
        if (n.l == null) { m.put(n.c, p.isEmpty() ? "0" : p); return; }
        codes(n.l, p + "0", m);
        codes(n.r, p + "1", m);
    }

    public static void main(String[] args) {
        String s = "mississippi river";
        Map<Character, Integer> f = new TreeMap<>();
        for (char c : s.toCharArray()) f.merge(c, 1, Integer::sum);
        PriorityQueue<N> pq = new PriorityQueue<>((a, b) -> a.f != b.f ? Integer.compare(a.f, b.f) : Integer.compare(a.id, b.id));
        int id = 0;
        for (Map.Entry<Character, Integer> e : f.entrySet()) pq.offer(new N(e.getKey(), e.getValue(), null, null, id++));
        while (pq.size() > 1) { N a = pq.poll(), b = pq.poll(); pq.offer(new N('\0', a.f + b.f, a, b, id++)); }
        N root = pq.poll();
        Map<Character, String> code = new TreeMap<>();
        codes(root, "", code);
        StringBuilder bits = new StringBuilder();
        for (char c : s.toCharArray()) bits.append(code.get(c));
        StringBuilder back = new StringBuilder();
        N cur = root;
        for (char b : bits.toString().toCharArray()) {
            cur = b == '0' ? cur.l : cur.r;
            if (cur.l == null) { back.append(cur.c); cur = root; }
        }
        System.out.println(code);
        System.out.println(bits.length() + " bits, decoded ok: " + back.toString().equals(s));
    }
}
```

Output: `{ =1000, e=1001, i=11, m=1010, p=010, r=011, s=00, v=1011}` and `46 bits, decoded ok: true` (versus 136 bits in 8-bit ASCII).

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
