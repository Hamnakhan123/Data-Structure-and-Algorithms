# Module 05 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** A node of a singly linked list contains:

- **A.** only a value
- **B.** a value and a reference to the next node
- **C.** a value and an index
- **D.** references to all other nodes

**Q2.** Inserting at the head of a singly linked list of n nodes costs:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n²)

**Q3.** What happens if you write `p.next = n; n.next = p.next;` to insert n after p?

- **A.** Correct insertion
- **B.** n points to itself, losing the rest of the list
- **C.** NullPointerException always
- **D.** n is inserted at the head

**Q4.** Which list allows O(1) removal of the last node?

- **A.** Singly linked with tail
- **B.** Doubly linked
- **C.** Circular singly with tail
- **D.** None of these

**Q5.** Sentinel nodes in a doubly linked list store:

- **A.** the first and last values
- **B.** no real data; they mark the boundaries
- **C.** the list size
- **D.** the middle element

**Q6.** Unlinking node n in a doubly linked list requires updating:

- **A.** n.next only
- **B.** n.prev.next and n.next.prev
- **C.** head only
- **D.** every node

**Q7.** In a circular singly linked list with a tail reference, adding at the end costs:

- **A.** O(1)
- **B.** O(n)
- **C.** O(log n)
- **D.** impossible

**Q8.** A traversal loop over a circular list should stop when:

- **A.** the node is null
- **B.** it returns to the starting node
- **C.** the value is zero
- **D.** after n² steps

**Q9.** Round-robin scheduling is a natural fit for:

- **A.** a sorted array
- **B.** a circular linked list
- **C.** a binary tree
- **D.** a stack

**Q10.** Josephus n = 6, k = 2: who survives (1-based)?

- **A.** 1
- **B.** 3
- **C.** 5
- **D.** 6

**Q11.** Reversing a linked list iteratively uses how much extra space?

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n²)

**Q12.** The slow/fast technique finds the middle in:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n) with one pass
- **D.** O(n) with two passes always

**Q13.** Floyd's algorithm compares:

- **A.** node values
- **B.** node references
- **C.** indices
- **D.** hash codes

**Q14.** If a list has no cycle, Floyd's loop ends because:

- **A.** slow becomes null
- **B.** fast or fast.next becomes null
- **C.** they meet
- **D.** the list is sorted

**Q15.** Merging two sorted linked lists of sizes n and m by relinking needs extra space:

- **A.** O(1)
- **B.** O(n)
- **C.** O(n + m)
- **D.** O(log n)

**Q16.** Which technique removes the n-th node from the end in one pass?

- **A.** Sorting
- **B.** Two pointers separated by a gap
- **C.** Binary search
- **D.** Hashing

**Q17.** `java.util.LinkedList` is implemented as:

- **A.** a singly linked list
- **B.** a doubly linked list
- **C.** a circular array
- **D.** a dynamic array

**Q18.** Why are linked lists often slower than arrays even for traversal?

- **A.** More comparisons
- **B.** Poor cache locality: nodes are scattered in memory
- **C.** They use recursion
- **D.** Java forbids optimisation

**Q19.** The palindrome check in O(1) space reverses:

- **A.** the whole list
- **B.** the second half (then restores it)
- **C.** the first half only, permanently
- **D.** nothing; it uses a stack

**Q20.** An LRU cache with O(1) get/put combines a hash map with:

- **A.** an array
- **B.** a doubly linked list
- **C.** a binary heap
- **D.** a stack

<details>
<summary><b>Show answers</b></summary>

1. **B**: value + next.
2. **A**: Create the node and redirect head.
3. **B**: p.next already equals n when the second statement runs.
4. **B**: The tail's predecessor is tail.prev.
5. **B**: They exist only to remove special cases.
6. **B**: The two neighbours must point at each other.
7. **A**: n.next = tail.next; tail.next = n; tail = n.
8. **B**: There is no null in a circular list.
9. **B**: Rotate the finished slice to the back in O(1).
10. **C**: Eliminations: 2, 4, 6, 3, 1 → 5 survives.
11. **A**: Three pointer variables.
12. **C**: fast reaches the end after n/2 steps of slow.
13. **B**: Values may repeat without a cycle.
14. **B**: The fast pointer reaches the end.
15. **A**: Only a dummy node and pointers.
16. **B**: When lead hits the end, lag is in position.
17. **B**: It supports both ends and bidirectional iteration.
18. **B**: Arrays are contiguous and prefetch well.
19. **B**: Compare first half with the reversed second half.
20. **B**: The map finds the node; the list moves/evicts it in O(1).

</details>

## Part B · Coding scenarios

### Scenario 1 · Reverse a sub-list

Reverse the nodes of a singly linked list from position `left` to `right` (1-based), in one pass. Example: `1 → 2 → 3 → 4 → 5`, left = 2, right = 4 → `1 → 4 → 3 → 2 → 5`.

<details>
<summary>✅ Model solution</summary>

```java
public class ReverseBetween {
    static class Node { int val; Node next; Node(int v) { val = v; } }

    static Node reverseBetween(Node head, int left, int right) {
        Node dummy = new Node(0);
        dummy.next = head;
        Node before = dummy;
        for (int i = 1; i < left; i++) before = before.next;   // node before the sub-list
        Node cur = before.next;
        for (int i = 0; i < right - left; i++) {                // move cur.next to the front each time
            Node move = cur.next;
            cur.next = move.next;
            move.next = before.next;
            before.next = move;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node h = null;
        for (int v = 5; v >= 1; v--) { Node n = new Node(v); n.next = h; h = n; }
        h = reverseBetween(h, 2, 4);
        StringBuilder sb = new StringBuilder();
        for (Node p = h; p != null; p = p.next) sb.append(p.val).append(p.next == null ? "" : " -> ");
        System.out.println(sb);
    }
}
```

O(n) time, O(1) space; the dummy handles `left = 1`.

</details>

### Scenario 2 · LRU cache

Design a cache of capacity `k` with `get(key)` and `put(key, value)` both O(1). When full, `put` evicts the **least recently used** key.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.HashMap;
import java.util.Map;

public class LruCache {
    static class Node { int key, val; Node prev, next; Node(int k, int v) { key = k; val = v; } }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(0, 0), tail = new Node(0, 0);   // sentinels; head side = most recent

    LruCache(int capacity) { this.capacity = capacity; head.next = tail; tail.prev = head; }

    private void unlink(Node n) { n.prev.next = n.next; n.next.prev = n.prev; }
    private void addFront(Node n) { n.next = head.next; n.prev = head; head.next.prev = n; head.next = n; }

    int get(int key) {
        Node n = map.get(key);
        if (n == null) return -1;
        unlink(n); addFront(n);              // mark as most recently used
        return n.val;
    }

    void put(int key, int val) {
        Node n = map.get(key);
        if (n != null) { n.val = val; unlink(n); addFront(n); return; }
        if (map.size() == capacity) {        // evict least recently used (just before tail)
            Node lru = tail.prev;
            unlink(lru);
            map.remove(lru.key);
        }
        n = new Node(key, val);
        addFront(n);
        map.put(key, n);
    }

    public static void main(String[] args) {
        LruCache c = new LruCache(2);
        c.put(1, 100); c.put(2, 200);
        System.out.println(c.get(1));   // 100, key 1 becomes most recent
        c.put(3, 300);                  // evicts key 2
        System.out.println(c.get(2));   // -1
        System.out.println(c.get(3));   // 300
    }
}
```

Hash map: key → node in O(1). Doubly linked list: move to front / evict from back in O(1).

</details>

### Scenario 3 · Polynomial addition

Represent a polynomial as a linked list of `(coefficient, exponent)` terms sorted by **decreasing** exponent. Add two polynomials, e.g. (5x³ + 4x² + 2) + (5x² − 2 + 3x⁴) after sorting.

<details>
<summary>✅ Model solution</summary>

```java
public class PolyAdd {
    static class Term { int coef, exp; Term next; Term(int c, int e) { coef = c; exp = e; } }

    static Term add(Term a, Term b) {
        Term dummy = new Term(0, 0), t = dummy;
        while (a != null || b != null) {
            Term pick;
            if (b == null || (a != null && a.exp > b.exp)) { pick = new Term(a.coef, a.exp); a = a.next; }
            else if (a == null || b.exp > a.exp) { pick = new Term(b.coef, b.exp); b = b.next; }
            else { pick = new Term(a.coef + b.coef, a.exp); a = a.next; b = b.next; }
            if (pick.coef != 0) { t.next = pick; t = pick; }   // drop zero terms
        }
        return dummy.next;
    }

    static Term poly(int... ce) {
        Term d = new Term(0, 0), t = d;
        for (int i = 0; i < ce.length; i += 2) { t.next = new Term(ce[i], ce[i + 1]); t = t.next; }
        return d.next;
    }

    public static void main(String[] args) {
        Term p = add(poly(5, 3, 4, 2, 2, 0), poly(3, 4, 5, 2, -2, 0));
        StringBuilder sb = new StringBuilder();
        for (Term t = p; t != null; t = t.next) sb.append(t.coef).append("x^").append(t.exp).append(t.next == null ? "" : " + ");
        System.out.println(sb);
    }
}
```

A merge of two sorted lists: O(n + m). Output: `3x^4 + 5x^3 + 9x^2` (the constants cancel).

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
