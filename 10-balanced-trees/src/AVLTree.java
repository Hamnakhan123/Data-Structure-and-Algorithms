import java.util.ArrayList;
import java.util.List;

/**
 * Session 1 demo: an AVL tree (self-balancing BST) with insert, delete, the four rotation cases,
 * a rotation log, and a height comparison against a plain BST on sorted input.
 *
 * Compile:  javac -d out AVLTree.java
 * Run:      java -cp out AVLTree
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class AVLTree {

    static final class Node {
        int key, height;               // height of a leaf = 0
        Node left, right;
        Node(int k) { key = k; }
    }

    private Node root;
    private int size;
    final List<String> log = new ArrayList<>();   // which rotations happened, for teaching
    int rotations;

    static int h(Node n) { return n == null ? -1 : n.height; }
    static int balance(Node n) { return h(n.left) - h(n.right); }   // +2 = left heavy, -2 = right heavy
    private static void update(Node n) { n.height = 1 + Math.max(h(n.left), h(n.right)); }

    /** Right rotation around y: y's left child x becomes the subtree root. */
    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;            // x's right subtree (keys between x and y) moves under y
        x.right = y;
        update(y);
        update(x);
        rotations++;
        return x;
    }

    /** Left rotation around x: mirror image of rotateRight. */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        update(x);
        update(y);
        rotations++;
        return y;
    }

    /** Restore the AVL property at n (|balance| <= 1), returning the new subtree root. */
    private Node rebalance(Node n) {
        update(n);
        int b = balance(n);
        if (b > 1) {                                       // left heavy
            if (balance(n.left) < 0) {                     // left-right case
                log.add("LR at " + n.key);
                n.left = rotateLeft(n.left);
            } else log.add("LL at " + n.key);
            return rotateRight(n);
        }
        if (b < -1) {                                      // right heavy
            if (balance(n.right) > 0) {                    // right-left case
                log.add("RL at " + n.key);
                n.right = rotateRight(n.right);
            } else log.add("RR at " + n.key);
            return rotateLeft(n);
        }
        return n;
    }

    void insert(int key) { root = insert(root, key); }

    private Node insert(Node n, int key) {
        if (n == null) { size++; return new Node(key); }
        if (key < n.key) n.left = insert(n.left, key);
        else if (key > n.key) n.right = insert(n.right, key);
        else return n;
        return rebalance(n);                 // fix every ancestor on the way back up
    }

    void delete(int key) { root = delete(root, key); }

    private Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) n.left = delete(n.left, key);
        else if (key > n.key) n.right = delete(n.right, key);
        else {
            if (n.left == null || n.right == null) { size--; return n.left != null ? n.left : n.right; }
            Node s = n.right;
            while (s.left != null) s = s.left;
            n.key = s.key;
            n.right = delete(n.right, s.key);
        }
        return rebalance(n);
    }

    boolean contains(int key) {
        for (Node n = root; n != null; n = key < n.key ? n.left : n.right) if (n.key == key) return true;
        return false;
    }

    int height() { return h(root); }
    int size() { return size; }

    /** Checks BST order, stored heights, and |balance| <= 1 everywhere. */
    boolean isAVL() { return check(root, Long.MIN_VALUE, Long.MAX_VALUE); }
    private boolean check(Node n, long lo, long hi) {
        if (n == null) return true;
        if (n.key <= lo || n.key >= hi) return false;
        if (n.height != 1 + Math.max(h(n.left), h(n.right))) return false;
        if (Math.abs(balance(n)) > 1) return false;
        return check(n.left, lo, n.key) && check(n.right, n.key, hi);
    }

    List<Integer> inorder() { List<Integer> out = new ArrayList<>(); in(root, out); return out; }
    private void in(Node n, List<Integer> out) { if (n == null) return; in(n.left, out); out.add(n.key); in(n.right, out); }

    void printSideways() { print(root, ""); }
    private void print(Node n, String ind) {
        if (n == null) return;
        print(n.right, ind + "     ");
        System.out.println(ind + n.key + "(b=" + balance(n) + ")");
        print(n.left, ind + "     ");
    }

    /** Plain BST height for comparison (iterative insert so sorted input does not overflow the stack). */
    static int plainBstHeight(int[] keys) {
        int[] left = new int[keys.length], right = new int[keys.length], depth = new int[keys.length];
        java.util.Arrays.fill(left, -1);
        java.util.Arrays.fill(right, -1);
        int maxDepth = 0;
        for (int i = 1; i < keys.length; i++) {
            int cur = 0, d = 0;
            while (true) {
                d++;
                if (keys[i] < keys[cur]) { if (left[cur] < 0) { left[cur] = i; break; } cur = left[cur]; }
                else { if (right[cur] < 0) { right[cur] = i; break; } cur = right[cur]; }
            }
            depth[i] = d;
            maxDepth = Math.max(maxDepth, d);
        }
        return maxDepth;
    }

    public static void main(String[] args) {
        System.out.println("== The four cases, one at a time ==");
        int[][] cases = {{30, 20, 10}, {10, 20, 30}, {30, 10, 20}, {10, 30, 20}};
        for (int[] c : cases) {
            AVLTree t = new AVLTree();
            for (int k : c) t.insert(k);
            System.out.println("insert " + java.util.Arrays.toString(c) + " -> " + t.log + ", root = " + t.root.key
                    + ", height = " + t.height());
        }

        System.out.println("\n== Insert 10 20 30 40 50 25 ==");
        AVLTree t = new AVLTree();
        for (int k : new int[]{10, 20, 30, 40, 50, 25}) {
            t.insert(k);
            System.out.println("after " + k + ": rotations so far " + t.log);
        }
        t.printSideways();
        System.out.println("inorder = " + t.inorder() + ", valid AVL = " + t.isAVL());

        System.out.println("\n== Delete 50, then 40: the root becomes left heavy and rotates ==");
        t.log.clear();
        t.delete(50);
        t.delete(40);
        t.printSideways();
        System.out.println("rotations: " + t.log + ", valid AVL = " + t.isAVL());

        System.out.println("\n== Sorted input: AVL vs plain BST ==");
        System.out.println("     n   plain BST height   AVL height   floor(log2 n)   1.44*log2(n+2)");
        for (int n : new int[]{15, 1023, 10_000}) {
            int[] keys = new int[n];
            AVLTree a = new AVLTree();
            for (int i = 0; i < n; i++) { keys[i] = i; a.insert(i); }
            double lg = Math.log(n) / Math.log(2);
            System.out.printf("%6d   %16d   %10d   %13d   %14.1f%n", n, plainBstHeight(keys), a.height(),
                    (int) Math.floor(lg), 1.44 * Math.log(n + 2) / Math.log(2));
        }
    }
}
