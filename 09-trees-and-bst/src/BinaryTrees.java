import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

/**
 * Session 1 demo: binary-tree nodes, the four traversals (recursive and iterative), and basic
 * recursive measurements: size, height, leaves, and a sideways picture of the tree.
 *
 * Compile:  javac -d out BinaryTrees.java
 * Run:      java -cp out BinaryTrees
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class BinaryTrees {

    /** A binary-tree node: a value and two child links. */
    static final class Node {
        int val;
        Node left, right;
        Node(int v) { val = v; }
        Node(int v, Node l, Node r) { val = v; left = l; right = r; }
    }

    // ---------- depth-first traversals (recursive) ----------
    static void preorder(Node n, List<Integer> out) {      // Node, Left, Right
        if (n == null) return;
        out.add(n.val);
        preorder(n.left, out);
        preorder(n.right, out);
    }

    static void inorder(Node n, List<Integer> out) {       // Left, Node, Right
        if (n == null) return;
        inorder(n.left, out);
        out.add(n.val);
        inorder(n.right, out);
    }

    static void postorder(Node n, List<Integer> out) {     // Left, Right, Node
        if (n == null) return;
        postorder(n.left, out);
        postorder(n.right, out);
        out.add(n.val);
    }

    /** Iterative inorder with an explicit stack: go left as far as possible, visit, then go right. */
    static List<Integer> inorderIterative(Node root) {
        List<Integer> out = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        Node cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop();
            out.add(cur.val);
            cur = cur.right;
        }
        return out;
    }

    /** Breadth-first (level-order) traversal with a queue, one list per level. */
    static List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) return levels;
        Queue<Node> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int width = q.size();                    // nodes currently in the queue = one whole level
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < width; i++) {
                Node n = q.poll();
                level.add(n.val);
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
            levels.add(level);
        }
        return levels;
    }

    // ---------- recursive measurements ----------
    static int size(Node n) { return n == null ? 0 : 1 + size(n.left) + size(n.right); }

    /** Height in edges: empty tree = -1, single node = 0. */
    static int height(Node n) { return n == null ? -1 : 1 + Math.max(height(n.left), height(n.right)); }

    static int leaves(Node n) {
        if (n == null) return 0;
        if (n.left == null && n.right == null) return 1;
        return leaves(n.left) + leaves(n.right);
    }

    static int sum(Node n) { return n == null ? 0 : n.val + sum(n.left) + sum(n.right); }

    /** Prints the tree rotated 90° counter-clockwise: the root on the left, right subtree on top. */
    static void printSideways(Node n, String indent) {
        if (n == null) return;
        printSideways(n.right, indent + "      ");
        System.out.println(indent + n.val);
        printSideways(n.left, indent + "      ");
    }

    /** Rebuild a tree from preorder with nulls (-1 marks an empty child): the usual serialisation. */
    static int pos;
    static Node fromPreorder(int[] a) { pos = 0; return build(a); }
    private static Node build(int[] a) {
        int v = a[pos++];
        if (v == -1) return null;
        Node n = new Node(v);
        n.left = build(a);
        n.right = build(a);
        return n;
    }

    /** The sample tree used throughout the session. */
    static Node sample() {
        //            1
        //          /   \
        //         2     3
        //        / \     \
        //       4   5     6
        //          /
        //         7
        return new Node(1,
                new Node(2, new Node(4), new Node(5, new Node(7), null)),
                new Node(3, null, new Node(6)));
    }

    public static void main(String[] args) {
        Node root = sample();
        System.out.println("Tree printed sideways (root at left):");
        printSideways(root, "");

        List<Integer> pre = new ArrayList<>(), in = new ArrayList<>(), post = new ArrayList<>();
        preorder(root, pre);
        inorder(root, in);
        postorder(root, post);
        System.out.println("\npreorder  (N L R) = " + pre);
        System.out.println("inorder   (L N R) = " + in);
        System.out.println("postorder (L R N) = " + post);
        System.out.println("inorder, iterative = " + inorderIterative(root));
        System.out.println("level order        = " + levelOrder(root));

        System.out.println("\nsize = " + size(root) + ", height = " + height(root)
                + ", leaves = " + leaves(root) + ", sum = " + sum(root));

        int[] serial = {1, 2, 4, -1, -1, 5, 7, -1, -1, -1, 3, -1, 6, -1, -1};
        Node copy = fromPreorder(serial);
        List<Integer> check = new ArrayList<>();
        preorder(copy, check);
        System.out.println("rebuilt from serialised preorder -> preorder = " + check
                + ", same as original: " + check.equals(pre));
    }
}
