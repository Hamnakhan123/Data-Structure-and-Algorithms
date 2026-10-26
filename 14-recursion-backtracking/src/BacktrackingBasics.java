import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Session 1 demo: the backtracking template ("choose, explore, un-choose") applied to subsets,
 * permutations, combinations and combination sum, with call counters that show how pruning
 * shrinks the search tree.
 *
 * Compile:  javac -d out BacktrackingBasics.java
 * Run:      java -cp out BacktrackingBasics
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class BacktrackingBasics {
    static long calls;                       // recursive calls made by the last search

    /** All subsets: at each index decide "skip" or "take". 2^n leaves. */
    static List<List<Integer>> subsets(int[] a) {
        List<List<Integer>> out = new ArrayList<>();
        calls = 0;
        subsets(a, 0, new ArrayList<>(), out);
        return out;
    }

    private static void subsets(int[] a, int i, List<Integer> cur, List<List<Integer>> out) {
        calls++;
        if (i == a.length) { out.add(new ArrayList<>(cur)); return; }   // copy: cur keeps changing
        subsets(a, i + 1, cur, out);                 // choice 1: skip a[i]
        cur.add(a[i]);                               // choose
        subsets(a, i + 1, cur, out);                 // explore
        cur.remove(cur.size() - 1);                  // un-choose (backtrack)
    }

    /** All permutations with a used[] array. n! leaves. */
    static List<List<Integer>> permutations(int[] a) {
        List<List<Integer>> out = new ArrayList<>();
        calls = 0;
        permute(a, new boolean[a.length], new ArrayList<>(), out);
        return out;
    }

    private static void permute(int[] a, boolean[] used, List<Integer> cur, List<List<Integer>> out) {
        calls++;
        if (cur.size() == a.length) { out.add(new ArrayList<>(cur)); return; }
        for (int i = 0; i < a.length; i++) {
            if (used[i]) continue;
            used[i] = true; cur.add(a[i]);           // choose
            permute(a, used, cur, out);              // explore
            used[i] = false; cur.remove(cur.size() - 1);   // un-choose
        }
    }

    /** Combinations of k numbers from 1..n, with pruning: stop when too few numbers remain. */
    static List<List<Integer>> combinations(int n, int k, boolean prune) {
        List<List<Integer>> out = new ArrayList<>();
        calls = 0;
        combine(1, n, k, prune, new ArrayList<>(), out);
        return out;
    }

    private static void combine(int start, int n, int k, boolean prune, List<Integer> cur, List<List<Integer>> out) {
        calls++;
        if (cur.size() == k) { out.add(new ArrayList<>(cur)); return; }
        int last = prune ? n - (k - cur.size()) + 1 : n;     // need k - size more numbers after this one
        for (int x = start; x <= last; x++) {
            cur.add(x);
            combine(x + 1, n, k, prune, cur, out);
            cur.remove(cur.size() - 1);
        }
    }

    /** Combination sum: numbers may be reused; candidates sorted so we can stop early (prune). */
    static List<List<Integer>> combinationSum(int[] cand, int target) {
        int[] c = cand.clone();
        Arrays.sort(c);
        List<List<Integer>> out = new ArrayList<>();
        calls = 0;
        sum(c, 0, target, new ArrayList<>(), out);
        return out;
    }

    private static void sum(int[] c, int start, int remain, List<Integer> cur, List<List<Integer>> out) {
        calls++;
        if (remain == 0) { out.add(new ArrayList<>(cur)); return; }
        for (int i = start; i < c.length; i++) {
            if (c[i] > remain) break;                // sorted: every later candidate is too big too
            cur.add(c[i]);
            sum(c, i, remain - c[i], cur, out);      // i, not i + 1: reuse allowed
            cur.remove(cur.size() - 1);
        }
    }

    /** Indented trace of the subset recursion tree for a small input. */
    static void traceSubsets(int[] a, int i, List<Integer> cur, String indent) {
        if (i == a.length) { System.out.println(indent + "leaf " + cur); return; }
        System.out.println(indent + "i=" + i + " cur=" + cur + "  skip " + a[i] + " / take " + a[i]);
        traceSubsets(a, i + 1, cur, indent + "  ");
        cur.add(a[i]);
        traceSubsets(a, i + 1, cur, indent + "  ");
        cur.remove(cur.size() - 1);
    }

    public static void main(String[] args) {
        System.out.println("== Recursion tree of subsets([1, 2]) ==");
        traceSubsets(new int[]{1, 2}, 0, new ArrayList<>(), "");

        System.out.println("\n== Subsets of [1, 2, 3] ==");
        System.out.println(subsets(new int[]{1, 2, 3}) + "  calls=" + calls);

        System.out.println("\n== Permutations of [1, 2, 3] ==");
        System.out.println(permutations(new int[]{1, 2, 3}) + "  calls=" + calls);

        System.out.println("\n== Combinations C(5, 3) ==");
        List<List<Integer>> c = combinations(5, 3, false);
        long noPrune = calls;
        System.out.println(c);
        combinations(5, 3, true);
        System.out.println(c.size() + " combinations; calls without pruning = " + noPrune + ", with pruning = " + calls);

        System.out.println("\n== Combination sum: candidates [2, 3, 6, 7], target 7 ==");
        System.out.println(combinationSum(new int[]{2, 3, 6, 7}, 7) + "  calls=" + calls);

        System.out.println("\n== Growth of the search space ==");
        System.out.println(" n   subsets 2^n   permutations n!   calls(subsets)   calls(perms)");
        for (int n : new int[]{4, 8, 10}) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i;
            subsets(a);
            long cs = calls;
            permutations(a);
            System.out.printf("%2d   %11d   %15d   %14d   %12d%n", n, 1L << n, factorial(n), cs, calls);
        }
    }

    static long factorial(int n) { long f = 1; for (int i = 2; i <= n; i++) f *= i; return f; }
}
