import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Module 01 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers so the
 * lesson notes can quote exactly the code that is compiled and tested here.
 *
 * Compile:  javac -d out Labs01.java
 * Run:      java -cp out Labs01 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs01 {

    // ---------------- Session 1 ----------------

    // >>> swap
    /** Generic swap: works for an array of ANY reference type. */
    static <T> void swap(T[] arr, int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    // <<< swap

    // >>> byLength
    /** Shorter words first; ties broken alphabetically. */
    static final Comparator<String> BY_LENGTH_THEN_ALPHA =
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());
    // <<< byLength

    // >>> secondLargest
    /** Second largest DISTINCT value in one pass; throws if it does not exist. */
    static int secondLargest(int[] a) {
        Integer first = null, second = null;
        for (int v : a) {
            if (first == null || v > first) {
                second = first;
                first = v;
            } else if (v != first && (second == null || v > second)) {
                second = v;
            }
        }
        if (second == null) throw new IllegalArgumentException("no second largest");
        return second;
    }
    // <<< secondLargest

    // ---------------- Session 2 ----------------

    // >>> duplicates
    /** O(n^2) time, O(1) extra space: compare every pair. */
    static boolean hasDuplicateBrute(int[] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[i] == a[j]) return true;
        return false;
    }

    /** O(n log n) time: after sorting, duplicates are neighbours. */
    static boolean hasDuplicateSorted(int[] a) {
        int[] c = a.clone();          // do not disturb the caller's array
        Arrays.sort(c);
        for (int i = 1; i < c.length; i++)
            if (c[i] == c[i - 1]) return true;
        return false;
    }

    /** O(n) expected time, O(n) space: remember what we have seen. */
    static boolean hasDuplicateHash(int[] a) {
        Set<Integer> seen = new HashSet<>();
        for (int v : a)
            if (!seen.add(v)) return true; // add() returns false if already present
        return false;
    }
    // <<< duplicates

    // >>> pairSum
    /** Sorted input: two pointers walk inward -> O(n) time, O(1) space. */
    static int[] pairWithSum(int[] sorted, int target) {
        int lo = 0, hi = sorted.length - 1;
        while (lo < hi) {
            int s = sorted[lo] + sorted[hi];
            if (s == target) return new int[]{lo, hi};
            if (s < target) lo++;   // need a bigger sum
            else hi--;              // need a smaller sum
        }
        return new int[0];
    }
    // <<< pairSum

    // ---------------- Session 3 ----------------

    // >>> arraySum
    /** Sum of a[i..end] recursively. Call arraySum(a, 0). */
    static int arraySum(int[] a, int i) {
        if (i == a.length) return 0;          // base case: empty suffix
        return a[i] + arraySum(a, i + 1);     // first element + rest
    }
    // <<< arraySum

    // >>> reverse
    /** Reverse a string: last char + reverse of the rest. */
    static String reverse(String s) {
        if (s.length() <= 1) return s;
        return s.charAt(s.length() - 1) + reverse(s.substring(0, s.length() - 1));
    }
    // <<< reverse

    // >>> gridPaths
    /** Number of right/down paths from (0,0) to (r,c). */
    static long gridPaths(int r, int c) {
        if (r == 0 || c == 0) return 1;        // only one straight path left
        return gridPaths(r - 1, c) + gridPaths(r, c - 1);
    }
    // <<< gridPaths

    // >>> countDown
    /** Prints n, n-1, ..., 1 then "Go!" — the simplest possible recursion. */
    static void countDown(int n) {
        if (n == 0) {
            System.out.println("Go!");
            return;
        }
        System.out.print(n + " ");
        countDown(n - 1);
    }
    // <<< countDown

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            String[] names = {"Hina", "Omar", "Zara"};
            swap(names, 0, 2);
            System.out.println("swap -> " + Arrays.toString(names));

            String[] words = {"stack", "map", "queue", "heap", "set", "graph"};
            Arrays.sort(words, BY_LENGTH_THEN_ALPHA);
            System.out.println("by length then alpha -> " + Arrays.toString(words));
            System.out.println("secondLargest({4, 9, 9, 7, 1}) = " + secondLargest(new int[]{4, 9, 9, 7, 1}));

        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            int[] data = {12, 5, 8, 21, 5, 30};
            System.out.println("duplicates? brute=" + hasDuplicateBrute(data) + " sorted=" + hasDuplicateSorted(data)
                    + " hash=" + hasDuplicateHash(data));
            System.out.println("pairWithSum({1,3,4,6,8,11}, 10) -> indices "
                    + Arrays.toString(pairWithSum(new int[]{1, 3, 4, 6, 8, 11}, 10)));

        }
        if (session == 0 || session == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("arraySum({3,1,4,1,5}) = " + arraySum(new int[]{3, 1, 4, 1, 5}, 0));
            System.out.println("reverse(\"recursion\") = " + reverse("recursion"));
            System.out.println("gridPaths(2,2) = " + gridPaths(2, 2) + ", gridPaths(5,5) = " + gridPaths(5, 5));
            countDown(5);
        }
    }
}
