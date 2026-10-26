import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Session 2 demo: a binary search tree of int keys with insert, search, min/max, floor/ceiling,
 * delete (all three cases), range queries, and a demonstration of how insertion order controls height.
 *
 * Compile:  javac -d out BinarySearchTree.java
 * Run:      java -cp out BinarySearchTree
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class BinarySearchTree {

    static final class Node {
        int key;
        Node left, right;
        Node(int k) { key = k; }
    }

    private Node root;
    private int size;

    /** Iterative search: go left if smaller, right if larger. */
    boolean contains(int key) {
        Node n = root;
        while (n != null) {
            if (key == n.key) return true;
            n = key < n.key ? n.left : n.right;
        }
        return false;
    }

    void insert(int key) { root = insert(root, key); }

    private Node insert(Node n, int key) {
        if (n == null) { size++; return new Node(key); }
        if (key < n.key) n.left = insert(n.left, key);
        else if (key > n.key) n.right = insert(n.right, key);
        // equal: duplicate ignored (a set)
        return n;
    }

    int min() { Node n = root; while (n.left != null) n = n.left; return n.key; }
    int max() { Node n = root; while (n.right != null) n = n.right; return n.key; }

    /** Largest key <= x, or null. */
    Integer floor(int x) {
        Integer best = null;
        for (Node n = root; n != null; ) {
            if (n.key == x) return x;
            if (n.key < x) { best = n.key; n = n.right; }   // candidate; maybe a larger one on the right
            else n = n.left;
        }
        return best;
    }

    /** Smallest key >= x, or null. */
    Integer ceiling(int x) {
        Integer best = null;
        for (Node n = root; n != null; ) {
            if (n.key == x) return x;
            if (n.key > x) { best = n.key; n = n.left; }
            else n = n.right;
        }
        return best;
    }

    void delete(int key) { root = delete(root, key); }

    /** Hibbard deletion: 0 or 1 child -> splice out; 2 children -> copy in-order successor, delete it. */
    private Node delete(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) n.left = delete(n.left, key);
        else if (key > n.key) n.right = delete(n.right, key);
        else {
            if (n.left == null) { size--; return n.right; }       // case 1 and 2
            if (n.right == null) { size--; return n.left; }       // case 2
            Node s = n.right;                                      // case 3: successor = min of right subtree
            while (s.left != null) s = s.left;
            n.key = s.key;
            n.right = delete(n.right, s.key);
        }
        return n;
    }

    /** Keys in [lo, hi] in sorted order: prune subtrees that cannot contain answers. */
    List<Integer> range(int lo, int hi) {
        List<Integer> out = new ArrayList<>();
        range(root, lo, hi, out);
        return out;
    }

    private void range(Node n, int lo, int hi, List<Integer> out) {
        if (n == null) return;
        if (lo < n.key) range(n.left, lo, hi, out);
        if (lo <= n.key && n.key <= hi) out.add(n.key);
        if (n.key < hi) range(n.right, lo, hi, out);
    }

    List<Integer> inorder() { return range(Integer.MIN_VALUE, Integer.MAX_VALUE); }

    Node rootNode() { return root; }
    int height() { return height(root); }
    private int height(Node n) { return n == null ? -1 : 1 + Math.max(height(n.left), height(n.right)); }
    int size() { return size; }

    /** Is the subtree a valid BST? Every key must lie strictly inside the (lo, hi) window. */
    boolean isValid() { return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE); }
    private boolean isValid(Node n, long lo, long hi) {
        if (n == null) return true;
        if (n.key <= lo || n.key >= hi) return false;
        return isValid(n.left, lo, n.key) && isValid(n.right, n.key, hi);
    }

    void printSideways() { print(root, ""); }
    private void print(Node n, String ind) {
        if (n == null) return;
        print(n.right, ind + "     ");
        System.out.println(ind + n.key);
        print(n.left, ind + "     ");
    }

    public static void main(String[] args) {
        BinarySearchTree t = new BinarySearchTree();
        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80, 35, 45, 65}) t.insert(k);
        System.out.println("BST after inserting 50 30 70 20 40 60 80 35 45 65:");
        t.printSideways();
        System.out.println("inorder (always sorted) = " + t.inorder());
        System.out.println("size = " + t.size() + ", height = " + t.height() + ", valid = " + t.isValid());
        System.out.println("contains(45) = " + t.contains(45) + ", contains(55) = " + t.contains(55));
        System.out.println("min = " + t.min() + ", max = " + t.max());
        System.out.println("floor(58) = " + t.floor(58) + ", ceiling(58) = " + t.ceiling(58)
                + ", floor(10) = " + t.floor(10));
        System.out.println("range [33, 62] = " + t.range(33, 62));

        System.out.println("\ndelete 30 (two children)  -> " + del(t, 30));
        System.out.println("delete 20 (leaf)          -> " + del(t, 20));
        System.out.println("delete 60 (one child)     -> " + del(t, 60));
        System.out.println("delete 50 (root, two ch.) -> " + del(t, 50));
        System.out.println("tree now:");
        t.printSideways();
        System.out.println("valid = " + t.isValid());

        System.out.println("\nHeight depends on insertion order (n = 1023 keys):");
        BinarySearchTree sorted = new BinarySearchTree(), shuffled = new BinarySearchTree();
        List<Integer> keys = new ArrayList<>();
        for (int i = 1; i <= 1023; i++) { sorted.insert(i); keys.add(i); }
        java.util.Collections.shuffle(keys, new Random(9));
        for (int k : keys) shuffled.insert(k);
        System.out.println("  sorted insertion   height = " + sorted.height() + "  (a linked list!)");
        System.out.println("  random insertion   height = " + shuffled.height());
        System.out.println("  perfectly balanced height = " + (int) (Math.log(1024) / Math.log(2) - 1));
    }

    private static List<Integer> del(BinarySearchTree t, int k) { t.delete(k); return t.inorder(); }
}
