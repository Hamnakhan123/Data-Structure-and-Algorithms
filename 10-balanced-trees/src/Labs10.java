import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Module 10 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out AVLTree.java RedBlackTree.java Labs10.java
 * Run:      java -cp out Labs10 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs10 {

    static final class Node {
        int val; Node left, right;
        Node(int v, Node l, Node r) { val = v; left = l; right = r; }
    }

    // >>> isBalanced
    /** Height-balanced check in ONE pass: return the height, or -2 as soon as any subtree is unbalanced. */
    static boolean isBalanced(Node root) { return heightOrFail(root) != -2; }

    private static int heightOrFail(Node n) {
        if (n == null) return -1;
        int l = heightOrFail(n.left);
        if (l == -2) return -2;
        int r = heightOrFail(n.right);
        if (r == -2) return -2;
        if (Math.abs(l - r) > 1) return -2;
        return 1 + Math.max(l, r);
    }
    // <<< isBalanced

    // >>> minNodes
    /** Fewest nodes an AVL tree of height h can have: N(h) = N(h-1) + N(h-2) + 1 (Fibonacci-like). */
    static long minNodes(int h) {
        if (h < 0) return 0;
        if (h == 0) return 1;
        long a = 1, b = 2;                 // N(0), N(1)
        for (int i = 2; i <= h; i++) { long c = a + b + 1; a = b; b = c; }
        return h == 1 ? 2 : b;
    }
    // <<< minNodes

    // >>> countRotations
    /** Insert keys into an AVL tree and report the rotation log. */
    static String rotationsFor(int... keys) {
        AVLTree t = new AVLTree();
        for (int k : keys) t.insert(k);
        return t.log + " (" + t.rotations + " single rotations), height " + t.height();
    }
    // <<< countRotations

    // >>> roomBooking
    /** Lab-room booking: accept [start, end) only if it overlaps no existing booking. O(log n) per request. */
    static final class RoomCalendar {
        private final TreeMap<Integer, Integer> bookings = new TreeMap<>();   // start -> end

        boolean book(int start, int end) {
            Integer before = bookings.floorKey(start);            // latest booking starting at or before start
            Integer after = bookings.ceilingKey(start);           // earliest booking starting at or after start
            if (before != null && bookings.get(before) > start) return false;   // previous one still running
            if (after != null && after < end) return false;                      // next one starts too early
            bookings.put(start, end);
            return true;
        }
    }
    // <<< roomBooking

    // >>> nearbyAlmostDuplicate
    /** Are there i != j with |i - j| <= k and |a[i] - a[j]| <= t? Sliding window kept in a balanced BST. */
    static boolean nearbyAlmostDuplicate(int[] a, int k, int t) {
        TreeSet<Long> window = new TreeSet<>();
        for (int i = 0; i < a.length; i++) {
            Long c = window.ceiling((long) a[i] - t);              // smallest value >= a[i] - t
            if (c != null && c <= (long) a[i] + t) return true;
            window.add((long) a[i]);
            if (i >= k) window.remove((long) a[i - k]);
        }
        return false;
    }
    // <<< nearbyAlmostDuplicate

    // >>> leaderboard
    /** Competition rank = 1 + number of students with a strictly higher score. tailMap views the higher scores. */
    static int rank(TreeMap<Integer, Integer> scoreCount, int s) {
        return 1 + scoreCount.tailMap(s, false).values().stream().mapToInt(Integer::intValue).sum();
    }
    // <<< leaderboard

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            Node bal = new Node(1, new Node(2, new Node(4, null, null), null), new Node(3, null, null));
            Node unbal = new Node(1, new Node(2, new Node(4, new Node(8, null, null), null), null), new Node(3, null, null));
            System.out.println("isBalanced(balanced) = " + isBalanced(bal) + ", isBalanced(left chain) = " + isBalanced(unbal));
            StringBuilder sb = new StringBuilder();
            for (int h = 0; h <= 10; h++) sb.append(minNodes(h)).append(h < 10 ? ", " : "");
            System.out.println("min nodes for AVL height 0..10 = " + sb);
            System.out.println("rotations for 50 40 30 20 10 = " + rotationsFor(50, 40, 30, 20, 10));
            System.out.println("rotations for 20 10 30 25 27 = " + rotationsFor(20, 10, 30, 25, 27));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            RoomCalendar cal = new RoomCalendar();
            int[][] req = {{10, 20}, {15, 25}, {20, 30}, {5, 10}, {8, 12}};
            StringBuilder sb = new StringBuilder();
            for (int[] r : req) sb.append("[").append(r[0]).append(",").append(r[1]).append(")=").append(cal.book(r[0], r[1])).append(" ");
            System.out.println("bookings: " + sb.toString().trim());
            System.out.println("nearbyAlmostDuplicate([1, 5, 9, 1, 5, 9], k=2, t=3) = " + nearbyAlmostDuplicate(new int[]{1, 5, 9, 1, 5, 9}, 2, 3)
                    + ", ([1, 2, 3, 1], k=3, t=0) = " + nearbyAlmostDuplicate(new int[]{1, 2, 3, 1}, 3, 0));
            TreeMap<Integer, Integer> scores = new TreeMap<>();
            for (int s : new int[]{88, 92, 75, 92, 60, 88}) scores.merge(s, 1, Integer::sum);
            System.out.println("rank(92) = " + rank(scores, 92) + ", rank(88) = " + rank(scores, 88) + ", rank(60) = " + rank(scores, 60));
        }
    }
}
