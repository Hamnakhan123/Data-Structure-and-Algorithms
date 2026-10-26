import java.util.ArrayList;
import java.util.List;

/**
 * Module 09 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out BinaryTrees.java Labs09.java
 * Run:      java -cp out Labs09 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs09 {
    // reuse the node type from Session 1
    static BinaryTrees.Node n(int v, BinaryTrees.Node l, BinaryTrees.Node r) { return new BinaryTrees.Node(v, l, r); }
    static BinaryTrees.Node n(int v) { return new BinaryTrees.Node(v); }

    // >>> isSymmetric
    /** A tree is symmetric if its left subtree mirrors its right subtree. */
    static boolean isSymmetric(BinaryTrees.Node root) { return root == null || mirror(root.left, root.right); }

    static boolean mirror(BinaryTrees.Node a, BinaryTrees.Node b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && mirror(a.left, b.right) && mirror(a.right, b.left);
    }
    // <<< isSymmetric

    // >>> hasPathSum
    /** Is there a root-to-leaf path whose values add up to target? Subtract as you descend. */
    static boolean hasPathSum(BinaryTrees.Node n, int target) {
        if (n == null) return false;
        if (n.left == null && n.right == null) return n.val == target;
        return hasPathSum(n.left, target - n.val) || hasPathSum(n.right, target - n.val);
    }
    // <<< hasPathSum

    // >>> diameter
    /** Diameter = longest path (in edges) between any two nodes. One post-order pass returning heights. */
    static int best;
    static int diameter(BinaryTrees.Node root) { best = 0; depth(root); return best; }

    private static int depth(BinaryTrees.Node n) {          // height in nodes
        if (n == null) return 0;
        int l = depth(n.left), r = depth(n.right);
        best = Math.max(best, l + r);                        // path that bends at n
        return 1 + Math.max(l, r);
    }
    // <<< diameter

    // >>> mirrorTree
    /** Mirror a tree in place: swap the children of every node. */
    static BinaryTrees.Node invert(BinaryTrees.Node n) {
        if (n == null) return null;
        BinaryTrees.Node tmp = n.left;
        n.left = invert(n.right);
        n.right = invert(tmp);
        return n;
    }
    // <<< mirrorTree

    // >>> lcaBst
    /** Lowest common ancestor in a BST: walk down until p and q split to different sides. */
    static int lcaBst(BinarySearchTree.Node root, int p, int q) {
        BinarySearchTree.Node n = root;
        while (true) {
            if (p < n.key && q < n.key) n = n.left;
            else if (p > n.key && q > n.key) n = n.right;
            else return n.key;
        }
    }
    // <<< lcaBst

    // >>> sortedArrayToBst
    /** Build a height-balanced BST from a sorted array: middle element becomes the root. */
    static BinaryTrees.Node sortedArrayToBst(int[] a, int lo, int hi) {
        if (lo > hi) return null;
        int mid = (lo + hi) >>> 1;
        return n(a[mid], sortedArrayToBst(a, lo, mid - 1), sortedArrayToBst(a, mid + 1, hi));
    }
    // <<< sortedArrayToBst

    // >>> kthSmallest
    /** k-th smallest key (1-based): the k-th node visited by an inorder traversal; stop early. */
    static int count, answer;
    static int kthSmallest(BinarySearchTree.Node root, int k) { count = k; answer = -1; walk(root); return answer; }

    private static void walk(BinarySearchTree.Node n) {
        if (n == null || count == 0) return;
        walk(n.left);
        if (count > 0 && --count == 0) { answer = n.key; return; }
        walk(n.right);
    }
    // <<< kthSmallest

    static BinarySearchTree sampleBst() {
        BinarySearchTree t = new BinarySearchTree();
        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80, 35, 45, 65}) t.insert(k);
        return t;
    }

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            BinaryTrees.Node sym = n(1, n(2, n(3), n(4)), n(2, n(4), n(3)));
            System.out.println("isSymmetric(1,[2,3,4],[2,4,3]) = " + isSymmetric(sym)
                    + ", isSymmetric(sample) = " + isSymmetric(BinaryTrees.sample()));
            BinaryTrees.Node s = BinaryTrees.sample();
            System.out.println("hasPathSum(sample, 15) = " + hasPathSum(s, 15) + "  (1+2+5+7)"
                    + ", hasPathSum(sample, 9) = " + hasPathSum(s, 9));
            System.out.println("diameter(sample) = " + diameter(s) + " edges  (7-5-2-1-3-6)");
            List<Integer> out = new ArrayList<>();
            BinaryTrees.inorder(invert(BinaryTrees.sample()), out);
            System.out.println("inorder of mirrored sample = " + out + "  (the original inorder reversed)");
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            BinarySearchTree t = sampleBst();
            System.out.println("LCA(35, 45) = " + lcaBst(t.rootNode(), 35, 45) + ", LCA(20, 45) = " + lcaBst(t.rootNode(), 20, 45)
                    + ", LCA(35, 65) = " + lcaBst(t.rootNode(), 35, 65));
            BinaryTrees.Node bal = sortedArrayToBst(new int[]{1, 2, 3, 4, 5, 6, 7}, 0, 6);
            System.out.println("sortedArrayToBst([1..7]) level order = " + BinaryTrees.levelOrder(bal)
                    + ", height = " + BinaryTrees.height(bal));
            System.out.println("kthSmallest k=1,4,10 = " + kthSmallest(t.rootNode(), 1) + ", "
                    + kthSmallest(t.rootNode(), 4) + ", " + kthSmallest(t.rootNode(), 10));
        }
    }
}
