import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * Session 2 demo: a left-leaning red-black tree (LLRB), the simplest red-black variant to code.
 * Insert uses three local fixes: rotate left, rotate right, flip colours. The demo validates the
 * red-black invariants, counts fixes, compares heights with AVL, and shows java.util.TreeMap.
 *
 * Compile:  javac -d out AVLTree.java RedBlackTree.java
 * Run:      java -cp out RedBlackTree
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class RedBlackTree {
    private static final boolean RED = true, BLACK = false;

    static final class Node {
        int key;
        Node left, right;
        boolean color;               // colour of the link from the parent to this node
        Node(int k) { key = k; color = RED; }
    }

    private Node root;
    private int size;
    int leftRotations, rightRotations, flips;

    private static boolean isRed(Node n) { return n != null && n.color == RED; }

    private Node rotateLeft(Node h) {             // a right-leaning red link becomes left-leaning
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        leftRotations++;
        return x;
    }

    private Node rotateRight(Node h) {            // two reds in a row on the left: lift the middle one
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        rightRotations++;
        return x;
    }

    private void flipColors(Node h) {             // split a temporary 4-node: pass red up to the parent
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
        flips++;
    }

    void insert(int key) {
        root = insert(root, key);
        root.color = BLACK;                        // the root is always black
    }

    private Node insert(Node h, int key) {
        if (h == null) { size++; return new Node(key); }   // new nodes arrive red
        if (key < h.key) h.left = insert(h.left, key);
        else if (key > h.key) h.right = insert(h.right, key);
        else return h;
        // the three local fixes, applied bottom-up
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    boolean contains(int key) {
        for (Node n = root; n != null; n = key < n.key ? n.left : n.right) if (n.key == key) return true;
        return false;
    }

    int height() { return height(root); }
    private int height(Node n) { return n == null ? -1 : 1 + Math.max(height(n.left), height(n.right)); }
    int size() { return size; }

    /** Number of black links from the root to any null link; -1 if paths disagree. */
    int blackHeight() { return bh(root); }
    private int bh(Node n) {
        if (n == null) return 0;
        int l = bh(n.left), r = bh(n.right);
        if (l < 0 || r < 0 || l != r) return -1;
        return l + (isRed(n) ? 0 : 1);
    }

    /** Red-black invariants: BST order, root black, no red right links, no two reds in a row, perfect black balance. */
    boolean isValid() {
        return !isRed(root) && ordered(root, Long.MIN_VALUE, Long.MAX_VALUE) && noBadReds(root) && blackHeight() >= 0;
    }
    private boolean ordered(Node n, long lo, long hi) {
        return n == null || (n.key > lo && n.key < hi && ordered(n.left, lo, n.key) && ordered(n.right, n.key, hi));
    }
    private boolean noBadReds(Node n) {
        if (n == null) return true;
        if (isRed(n.right)) return false;
        if (isRed(n) && isRed(n.left)) return false;
        return noBadReds(n.left) && noBadReds(n.right);
    }

    List<String> levels() {
        List<String> out = new ArrayList<>();
        List<Node> level = new ArrayList<>();
        if (root != null) level.add(root);
        while (!level.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            List<Node> next = new ArrayList<>();
            for (Node n : level) {
                sb.append(n.key).append(isRed(n) ? "R " : "B ");
                if (n.left != null) next.add(n.left);
                if (n.right != null) next.add(n.right);
            }
            out.add(sb.toString().trim());
            level = next;
        }
        return out;
    }

    public static void main(String[] args) {
        System.out.println("== Insert 1..10 in order into an LLRB tree ==");
        RedBlackTree t = new RedBlackTree();
        for (int k = 1; k <= 10; k++) t.insert(k);
        System.out.println("levels (R = red link from parent, B = black):");
        for (String l : t.levels()) System.out.println("  " + l);
        System.out.println("height = " + t.height() + ", black height = " + t.blackHeight() + ", valid = " + t.isValid());
        System.out.println("fixes: rotateLeft=" + t.leftRotations + " rotateRight=" + t.rightRotations + " flips=" + t.flips);

        System.out.println("\n== Heights for n keys (sorted and random input) ==");
        System.out.println("      n   input    AVL   red-black   log2 n   2*log2 n");
        for (int n : new int[]{1000, 100_000}) {
            for (String order : new String[]{"sorted", "random"}) {
                int[] keys = new int[n];
                for (int i = 0; i < n; i++) keys[i] = i;
                if (order.equals("random")) {
                    Random r = new Random(10);
                    for (int i = n - 1; i > 0; i--) { int j = r.nextInt(i + 1); int tmp = keys[i]; keys[i] = keys[j]; keys[j] = tmp; }
                }
                AVLTree a = new AVLTree();
                RedBlackTree rb = new RedBlackTree();
                for (int k : keys) { a.insert(k); rb.insert(k); }
                double lg = Math.log(n) / Math.log(2);
                System.out.printf("%7d   %-6s %4d   %9d   %6.1f   %8.1f   valid=%b%n", n, order, a.height(), rb.height(), lg, 2 * lg, rb.isValid());
            }
        }

        System.out.println("\n== java.util.TreeMap is a red-black tree ==");
        TreeMap<Integer, String> slots = new TreeMap<>();
        slots.put(900, "DSA lecture");
        slots.put(1100, "AI lab");
        slots.put(1400, "OOP tutorial");
        slots.put(1600, "Office hours");
        System.out.println("firstKey = " + slots.firstKey() + ", lastKey = " + slots.lastKey());
        System.out.println("floorEntry(1230)   = " + slots.floorEntry(1230));
        System.out.println("ceilingEntry(1230) = " + slots.ceilingEntry(1230));
        System.out.println("headMap(1400)      = " + slots.headMap(1400));
        System.out.println("descendingKeySet   = " + slots.descendingKeySet());
    }
}
