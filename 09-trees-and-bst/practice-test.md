# Module 09 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** A node with no children is called a:

- **A.** root
- **B.** leaf
- **C.** parent
- **D.** sibling

**Q2.** The depth of a node is:

- **A.** the number of its descendants
- **B.** the number of edges from the root to it
- **C.** the height of its subtree
- **D.** its value

**Q3.** A perfect binary tree of height 3 has how many nodes?

- **A.** 7
- **B.** 8
- **C.** 15
- **D.** 16

**Q4.** Which tree shape is used by binary heaps?

- **A.** degenerate
- **B.** complete
- **C.** full but unbalanced
- **D.** any shape

**Q5.** Preorder of the tree root 1, left 2, right 3 is:

- **A.** 2 1 3
- **B.** 1 2 3
- **C.** 2 3 1
- **D.** 3 2 1

**Q6.** Postorder of the same tree (1; left 2; right 3) is:

- **A.** 1 2 3
- **B.** 2 1 3
- **C.** 2 3 1
- **D.** 3 1 2

**Q7.** An iterative inorder traversal uses a:

- **A.** queue
- **B.** stack
- **C.** heap
- **D.** hash set

**Q8.** Level-order traversal of a tree with n nodes takes:

- **A.** O(log n)
- **B.** O(n)
- **C.** O(n log n)
- **D.** O(n²)

**Q9.** How many null child links does a binary tree with 10 nodes have?

- **A.** 9
- **B.** 10
- **C.** 11
- **D.** 20

**Q10.** Computing the height of a tree is most naturally a:

- **A.** preorder computation
- **B.** postorder computation
- **C.** level-order computation
- **D.** inorder computation

**Q11.** In a BST, the minimum key is found by:

- **A.** going right until null
- **B.** going left until null
- **C.** inorder from the root to the end
- **D.** checking every leaf

**Q12.** Searching a BST of height h costs:

- **A.** O(1)
- **B.** O(h)
- **C.** O(n log n)
- **D.** O(2^h)

**Q13.** Keys 40, 20, 60, 10, 30 are inserted in order. What is the parent of 30?

- **A.** 40
- **B.** 20
- **C.** 10
- **D.** 60

**Q14.** Inorder traversal of any BST gives:

- **A.** level order
- **B.** sorted order
- **C.** reverse insertion order
- **D.** preorder

**Q15.** Deleting a leaf from a BST:

- **A.** requires the successor
- **B.** simply removes the node
- **C.** rebalances the whole tree
- **D.** is impossible

**Q16.** The in-order successor of a node with a right child is:

- **A.** its parent
- **B.** the minimum of its right subtree
- **C.** the maximum of its left subtree
- **D.** the root

**Q17.** ceiling(33) for keys {20, 30, 35, 40} is:

- **A.** 30
- **B.** 35
- **C.** 40
- **D.** null

**Q18.** Inserting n keys in increasing order into an unbalanced BST gives height:

- **A.** log₂ n
- **B.** n − 1
- **C.** n/2
- **D.** 1

**Q19.** Which Java class is a balanced BST?

- **A.** HashMap
- **B.** TreeMap
- **C.** ArrayList
- **D.** PriorityQueue

**Q20.** Checking only `left.key < node.key < right.key` at each node:

- **A.** always validates a BST
- **B.** can accept trees that violate the BST property deeper down
- **C.** is O(n²)
- **D.** rejects valid BSTs

<details>
<summary><b>Show answers</b></summary>

1. **B**: Leaves are at the bottom of the tree.
2. **B**: Depth counts edges down from the root.
3. **C**: 2^(h+1) − 1 = 15.
4. **B**: Complete trees fit perfectly in an array.
5. **B**: Node, Left, Right.
6. **C**: Left, Right, Node.
7. **B**: It replaces the recursion's call stack.
8. **B**: Each node enters and leaves the queue once.
9. **C**: 2n links, n − 1 used by edges ⇒ n + 1 nulls.
10. **B**: The children's heights are needed first.
11. **B**: Smaller keys are always to the left.
12. **B**: One comparison per level.
13. **B**: 30 < 40 → left to 20; 30 > 20 → right child of 20.
14. **B**: L < N < R at every node.
15. **B**: Case 1: replace by null.
16. **B**: Smallest key larger than the node.
17. **B**: Smallest key ≥ 33.
18. **B**: A chain to the right.
19. **B**: A red-black tree.
20. **B**: Use inherited (lo, hi) bounds.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Minimum depth of a decision tree

A help-desk chatbot is a binary decision tree: each leaf is an answer. Write `minDepth(Node root)` that returns the number of **nodes** on the shortest root-to-leaf path (the fewest questions a user can be asked). Use level order so that you stop at the first leaf found.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.Queue;

public class MinDepth {
    static final class Node {
        int val; Node left, right;
        Node(int v, Node l, Node r) { val = v; left = l; right = r; }
    }

    static int minDepth(Node root) {
        if (root == null) return 0;
        Queue<Node> q = new ArrayDeque<>();
        q.offer(root);
        for (int depth = 1; ; depth++) {
            for (int i = q.size(); i > 0; i--) {
                Node n = q.poll();
                if (n.left == null && n.right == null) return depth;   // first leaf = shallowest
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
        }
    }

    public static void main(String[] args) {
        Node leaf = new Node(9, null, null);
        Node deep = new Node(20, new Node(15, null, null), new Node(7, null, null));
        System.out.println(minDepth(new Node(3, leaf, deep)));
        System.out.println(minDepth(new Node(1, null, new Node(2, null, new Node(3, null, null)))));
    }
}
```

Output: `2` then `3`. Level order stops early, which helps when the tree is deep but has a shallow leaf. O(n) worst case.

</details>

### Scenario 2 · Grade boundaries

The examination cell stores cut-off marks for grades in a BST-backed `TreeMap<Integer, String>`: 85→A, 70→B, 55→C, 40→D, 0→F. Write `grade(int marks)` that returns the grade for any mark in O(log n).

<details>
<summary>✅ Model solution</summary>

```java
import java.util.TreeMap;

public class GradeBoundaries {
    static final TreeMap<Integer, String> CUT = new TreeMap<>();
    static {
        CUT.put(85, "A"); CUT.put(70, "B"); CUT.put(55, "C"); CUT.put(40, "D"); CUT.put(0, "F");
    }

    static String grade(int marks) {
        return CUT.floorEntry(marks).getValue();     // largest cut-off <= marks
    }

    public static void main(String[] args) {
        for (int m : new int[]{92, 85, 84, 56, 40, 12}) System.out.print(m + "=" + grade(m) + " ");
        System.out.println();
    }
}
```

Output: `92=A 85=A 84=B 56=C 40=D 12=F`. `floorEntry` is the BST floor operation from Session 2.

</details>

### Scenario 3 · Is it a valid BST?

A data import produced the tree `8 (left 3 (left 1, right 9), right 10)`. Write `isBst(Node root)` that detects that 9 is misplaced, using bounds.

<details>
<summary>✅ Model solution</summary>

```java
public class ValidateBst {
    static final class Node {
        int key; Node left, right;
        Node(int k, Node l, Node r) { key = k; left = l; right = r; }
    }

    static boolean isBst(Node n, long lo, long hi) {
        if (n == null) return true;
        if (n.key <= lo || n.key >= hi) return false;
        return isBst(n.left, lo, n.key) && isBst(n.right, n.key, hi);
    }

    public static void main(String[] args) {
        Node bad = new Node(8, new Node(3, new Node(1, null, null), new Node(9, null, null)), new Node(10, null, null));
        Node good = new Node(8, new Node(3, new Node(1, null, null), new Node(6, null, null)), new Node(10, null, null));
        System.out.println(isBst(bad, Long.MIN_VALUE, Long.MAX_VALUE) + " " + isBst(good, Long.MIN_VALUE, Long.MAX_VALUE));
    }
}
```

Output: `false true`. Node 9 must lie in (3, 8) and fails, although 3 < 9 locally. O(n).

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
