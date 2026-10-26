# Module 10 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** Why do we need balanced BSTs?

- **A.** BSTs cannot store duplicates
- **B.** insertion order can make a plain BST's height n − 1
- **C.** BSTs use too much memory
- **D.** hash tables cannot store integers

**Q2.** The balance factor of a node is:

- **A.** size(left) − size(right)
- **B.** height(left) − height(right)
- **C.** key(left) − key(right)
- **D.** depth − height

**Q3.** A node with balance factor −2 is:

- **A.** left heavy
- **B.** right heavy
- **C.** balanced
- **D.** a leaf

**Q4.** How long does a single rotation take?

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q5.** After rotateRight(y) where x = y.left, the subtree x.right becomes:

- **A.** x's left child
- **B.** y's left child
- **C.** y's right child
- **D.** the new root

**Q6.** Inserting 10, 20, 30 into an empty AVL tree requires:

- **A.** rotateRight(10)
- **B.** rotateLeft(10)
- **C.** a double rotation
- **D.** no rotation

**Q7.** Inserting 30, 10, 20 into an empty AVL tree is the case:

- **A.** LL
- **B.** RR
- **C.** LR
- **D.** RL

**Q8.** The minimum number of nodes in an AVL tree of height 4 is:

- **A.** 7
- **B.** 12
- **C.** 15
- **D.** 20

**Q9.** AVL insertion needs at most how many rebalancing fixes (single or double)?

- **A.** one
- **B.** two per level
- **C.** O(log n)
- **D.** O(n)

**Q10.** Which operation may need rotations at several levels?

- **A.** AVL search
- **B.** AVL deletion
- **C.** AVL inorder traversal
- **D.** AVL min

**Q11.** In a red-black tree, a red node's children are:

- **A.** always red
- **B.** always black (or null)
- **C.** one red, one black
- **D.** unrestricted

**Q12.** Every path from a node to its NIL leaves has the same number of:

- **A.** red nodes
- **B.** black nodes
- **C.** keys
- **D.** rotations

**Q13.** A left-leaning red-black tree corresponds to a:

- **A.** binary heap
- **B.** 2-3 tree
- **C.** trie
- **D.** skip list

**Q14.** In LLRB insertion, a node with two red children triggers:

- **A.** rotateLeft
- **B.** rotateRight
- **C.** flipColors
- **D.** deletion

**Q15.** New nodes in a red-black tree are initially coloured:

- **A.** black
- **B.** red
- **C.** the parent's colour
- **D.** randomly

**Q16.** Which bound holds for red-black trees?

- **A.** h ≤ log₂ n
- **B.** h ≤ 2 log₂(n + 1)
- **C.** h ≤ n/2
- **D.** h ≤ 1.2 log₂ n

**Q17.** Which Java method returns the largest key ≤ x?

- **A.** ceilingKey(x)
- **B.** higherKey(x)
- **C.** floorKey(x)
- **D.** lowerKey(x)

**Q18.** `treeMap.headMap(k)` returns:

- **A.** the first key
- **B.** a live view of all keys < k
- **C.** a copy of all keys ≥ k
- **D.** the entry for k

**Q19.** For pure lookups with no ordering needs, the faster choice is usually:

- **A.** TreeMap
- **B.** HashMap
- **C.** a sorted LinkedList
- **D.** an AVL tree

**Q20.** Databases store indexes in B-trees mainly because:

- **A.** B-trees never need rebalancing
- **B.** one wide node fits a disk page, reducing disk reads
- **C.** they are simpler than arrays
- **D.** they use hashing

<details>
<summary><b>Show answers</b></summary>

1. **B**: Height determines cost.
2. **B**: As used by AVL trees.
3. **B**: Right subtree is two levels taller.
4. **A**: A constant number of link changes.
5. **B**: Keys between x and y move under y.
6. **B**: RR case at 10.
7. **C**: Left then right from 30.
8. **B**: N(4) = N(3) + N(2) + 1 = 7 + 4 + 1.
9. **A**: The fixed subtree returns to its old height.
10. **B**: Height can shrink repeatedly up the path.
11. **B**: No two reds in a row.
12. **B**: The black-height property.
13. **B**: Red links glue 3-nodes.
14. **C**: It splits a temporary 4-node.
15. **B**: A red node does not change any black height.
16. **B**: From the black-height argument.
17. **C**: floor = largest ≤ x.
18. **B**: Views reflect later changes.
19. **B**: O(1) expected vs O(log n).
20. **B**: High fan-out ⇒ very small height.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Exam-hall seat finder

Free seats in an exam hall are numbered integers. Write a class with `takeNearest(int s)` that assigns and removes the free seat **closest** to s (ties go to the smaller seat number), returning -1 if none is free. Each call must be O(log n).

<details>
<summary>✅ Model solution</summary>

```java
import java.util.TreeSet;

public class SeatFinder {
    private final TreeSet<Integer> free = new TreeSet<>();

    SeatFinder(int... seats) { for (int s : seats) free.add(s); }

    int takeNearest(int s) {
        Integer lo = free.floor(s), hi = free.ceiling(s);
        if (lo == null && hi == null) return -1;
        int pick;
        if (lo == null) pick = hi;
        else if (hi == null) pick = lo;
        else pick = (s - lo <= hi - s) ? lo : hi;     // tie -> smaller seat
        free.remove(pick);
        return pick;
    }

    public static void main(String[] args) {
        SeatFinder f = new SeatFinder(2, 5, 9, 14);
        System.out.println(f.takeNearest(7) + " " + f.takeNearest(7) + " " + f.takeNearest(13) + " " + f.takeNearest(0) + " " + f.takeNearest(3));
    }
}
```

Output: `5 9 14 2 -1`. First 7: 5 and 9 are both 2 away ⇒ 5. Then 9 is nearest, 14 for 13, 2 for 0, and the hall is empty.

</details>

### Scenario 2 · Height-balanced check for a menu tree

A web app stores its navigation menu as a binary tree. Write `isBalanced(Node root)` in **O(n)** (one pass) that reports whether every node's subtrees differ in height by at most 1.

<details>
<summary>✅ Model solution</summary>

```java
public class MenuBalance {
    static final class Node {
        Node left, right;
        Node(Node l, Node r) { left = l; right = r; }
    }

    /** Height of the subtree, or -2 if it is unbalanced anywhere. */
    static int check(Node n) {
        if (n == null) return -1;
        int l = check(n.left);
        if (l == -2) return -2;
        int r = check(n.right);
        if (r == -2 || Math.abs(l - r) > 1) return -2;
        return 1 + Math.max(l, r);
    }

    public static void main(String[] args) {
        Node leaf = new Node(null, null);
        Node ok = new Node(new Node(leaf, null), new Node(null, null));
        Node bad = new Node(new Node(new Node(new Node(null, null), null), null), new Node(null, null));
        System.out.println((check(ok) != -2) + " " + (check(bad) != -2));
    }
}
```

Output: `true false`. Each node is visited once ⇒ O(n).

</details>

### Scenario 3 · Sliding-window median of response times

A server logs response times; operations want the **median of the last k** values after each new value (k odd). Use a balanced BST (`TreeMap` with counts) so each update is O(log k), and find the median by walking from the smaller half. For simplicity, a walk over at most k entries is accepted here; state how to make it O(log k).

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WindowMedian {
    static List<Integer> medians(int[] a, int k) {
        TreeMap<Integer, Integer> w = new TreeMap<>();
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            w.merge(a[i], 1, Integer::sum);
            if (i >= k) {
                if (w.merge(a[i - k], -1, Integer::sum) == 0) w.remove(a[i - k]);
            }
            if (i >= k - 1) {
                int need = k / 2 + 1, seen = 0;
                for (Map.Entry<Integer, Integer> e : w.entrySet()) {
                    seen += e.getValue();
                    if (seen >= need) { out.add(e.getKey()); break; }
                }
            }
        }
        return out;
    }

    public static void main(String[] args) {
        System.out.println(medians(new int[]{120, 80, 95, 300, 90, 85, 110}, 3));
    }
}
```

Output: `[95, 95, 95, 90, 90]`. Insert/remove are O(log k). For O(log k) medians, use two heaps (Module 11) or two `TreeMap`s holding the lower and upper halves.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
