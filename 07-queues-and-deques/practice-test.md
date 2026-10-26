# Module 07 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** A queue removes elements in which order?

- **A.** LIFO
- **B.** FIFO
- **C.** by priority
- **D.** random

**Q2.** Dequeuing from a naive array queue with the front fixed at index 0 costs:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n²)

**Q3.** The main idea of a circular buffer is:

- **A.** sorting the array
- **B.** reusing freed slots by wrapping indices around with modulo
- **C.** doubling the array on every enqueue
- **D.** storing elements in a tree

**Q4.** Capacity 6, front = 4, size = 3. The rear element is at index:

- **A.** 6
- **B.** 0
- **C.** 1
- **D.** 7

**Q5.** Capacity 6, front = 5, after one dequeue front becomes:

- **A.** 6
- **B.** 0
- **C.** 4
- **D.** 1

**Q6.** Why keep a size counter in a circular queue?

- **A.** To sort elements
- **B.** To distinguish empty from full
- **C.** To speed up peek
- **D.** Java requires it

**Q7.** When a circular queue grows, elements are copied:

- **A.** in array-index order from 0
- **B.** in queue order starting at front
- **C.** in reverse order
- **D.** they are not copied

**Q8.** In a linked queue, enqueue happens at the:

- **A.** head
- **B.** tail
- **C.** middle
- **D.** either end randomly

**Q9.** Which `Queue` method throws an exception when the queue is empty?

- **A.** poll()
- **B.** peek()
- **C.** remove()
- **D.** offer()

**Q10.** Which data structure is best for BFS?

- **A.** stack
- **B.** queue
- **C.** hash map
- **D.** binary heap

**Q11.** A deque supports insertion at:

- **A.** the front only
- **B.** the back only
- **C.** both ends
- **D.** any index in O(1)

**Q12.** Which Java class is recommended for both stacks and queues?

- **A.** Vector
- **B.** Stack
- **C.** ArrayDeque
- **D.** TreeMap

**Q13.** Can `ArrayDeque` store null elements?

- **A.** Yes
- **B.** No
- **C.** Only at the front
- **D.** Only if capacity is large

**Q14.** Sliding-window maximum with a monotonic deque runs in:

- **A.** O(n·k)
- **B.** O(n log k)
- **C.** O(n)
- **D.** O(k)

**Q15.** In the sliding-window max deque, an index is removed from the FRONT when:

- **A.** its value is small
- **B.** it has left the window
- **C.** the deque is full
- **D.** it is the maximum

**Q16.** Window minima of `[3, 1, 4, 1, 5]` with k = 2:

- **A.** [1, 1, 1, 1]
- **B.** [3, 1, 4, 1]
- **C.** [1, 4, 1, 5]
- **D.** [1, 1, 4, 1]

**Q17.** In 0-1 BFS, a neighbour reached through a weight-1 edge is pushed to the:

- **A.** front
- **B.** back
- **C.** stack
- **D.** it is ignored

**Q18.** BFS on an R × C grid visits each cell at most:

- **A.** once
- **B.** twice
- **C.** four times
- **D.** R times

**Q19.** A printer spooler is best modelled as:

- **A.** a stack
- **B.** a queue
- **C.** a binary search tree
- **D.** a hash set

**Q20.** The two-stack queue's dequeue is amortised O(1) because:

- **A.** each element is moved from the input stack to the output stack at most once
- **B.** stacks are faster than queues
- **C.** it never moves elements
- **D.** it sorts elements

<details>
<summary><b>Show answers</b></summary>

1. **B**: First In, First Out.
2. **C**: All remaining elements shift left.
3. **B**: The array is treated as a ring.
4. **B**: (4 + 3 − 1) mod 6 = 0.
5. **B**: (5 + 1) mod 6 = 0.
6. **B**: front alone cannot tell the two states apart.
7. **B**: The new array starts with the front element at index 0.
8. **B**: Dequeue happens at the head.
9. **C**: poll and peek return null instead.
10. **B**: BFS processes vertices in FIFO order of discovery.
11. **C**: Double-ended queue.
12. **C**: Fast, resizable, unsynchronised.
13. **B**: null is reserved to mean "empty" in poll/peek.
14. **C**: Each index is added and removed at most once.
15. **B**: Expired indices are dropped from the front.
16. **A**: min(3,1)=1, min(1,4)=1, min(4,1)=1, min(1,5)=1.
17. **B**: It is one step farther than the current cell.
18. **A**: Cells are marked when enqueued.
19. **B**: Jobs print in submission order.
20. **A**: Total work over n operations is O(n).

</details>

## Part B · Coding scenarios

### Scenario 1 · Ticket counter with VIPs

A railway counter serves **VIP** passengers before regular passengers, but in arrival order within each group. Implement `arrive(name, isVip)` and `serve()` using two queues.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Queue;

public class TicketCounter {
    private final Queue<String> vip = new ArrayDeque<>(), regular = new ArrayDeque<>();

    void arrive(String name, boolean isVip) { (isVip ? vip : regular).offer(name); }

    String serve() {
        if (!vip.isEmpty()) return vip.poll();
        return regular.poll();                     // null if nobody is waiting
    }

    public static void main(String[] args) {
        TicketCounter c = new TicketCounter();
        c.arrive("Asma", false); c.arrive("Bilal", true); c.arrive("Cyrus", false); c.arrive("Dania", true);
        StringBuilder sb = new StringBuilder();
        for (String s; (s = c.serve()) != null; ) sb.append(s).append(' ');
        System.out.println(sb.toString().trim());
    }
}
```

Output: `Bilal Dania Asma Cyrus`. Both operations O(1). (With many priority levels, use a priority queue: Module 11.)

</details>

### Scenario 2 · Rotting oranges (multi-source BFS)

In a grid, `2` = rotten orange, `1` = fresh, `0` = empty. Every minute, rotten oranges rot their 4-neighbours. Return the minutes until no fresh orange remains, or −1 if impossible.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class RottingOranges {
    static int minutes(int[][] g) {
        Deque<int[]> q = new ArrayDeque<>();
        int fresh = 0;
        for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[0].length; j++) {
                if (g[i][j] == 2) q.offer(new int[]{i, j, 0});   // all rotten oranges start together
                if (g[i][j] == 1) fresh++;
            }
        int time = 0;
        int[][] d = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!q.isEmpty()) {
            int[] p = q.poll();
            time = p[2];
            for (int[] m : d) {
                int a = p[0] + m[0], b = p[1] + m[1];
                if (a >= 0 && b >= 0 && a < g.length && b < g[0].length && g[a][b] == 1) {
                    g[a][b] = 2;
                    fresh--;
                    q.offer(new int[]{a, b, p[2] + 1});
                }
            }
        }
        return fresh == 0 ? time : -1;
    }

    public static void main(String[] args) {
        System.out.println(minutes(new int[][]{{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}));
        System.out.println(minutes(new int[][]{{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}));
    }
}
```

Output: `4` and `-1`. Multi-source BFS: O(R·C).

</details>

### Scenario 3 · Maximum of each shift

A factory logs the temperature of a furnace every minute. Supervisors need the maximum over every rolling **k-minute** window. Write `windowMax(int[] temps, int k)` in O(n) and test it on `[70, 72, 68, 75, 74, 71, 80, 69]`, k = 3.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class ShiftMax {
    static int[] windowMax(int[] a, int k) {
        int[] out = new int[a.length - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < a.length; i++) {
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) dq.pollLast();
            dq.offerLast(i);
            if (i >= k - 1) out[i - k + 1] = a[dq.peekFirst()];
        }
        return out;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(windowMax(new int[]{70, 72, 68, 75, 74, 71, 80, 69}, 3)));
    }
}
```

Output: `[72, 75, 75, 75, 80, 80]`.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
