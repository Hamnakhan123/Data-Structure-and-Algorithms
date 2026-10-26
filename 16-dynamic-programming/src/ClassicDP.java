import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Session 2 demo: classic dynamic-programming problems with tables and reconstruction:
 * 0/1 knapsack (2-D table, chosen items, 1-D space optimisation), longest common subsequence,
 * coin change (minimum coins and number of ways), and longest increasing subsequence (O(n^2) and O(n log n)).
 *
 * Compile:  javac -d out ClassicDP.java
 * Run:      java -cp out ClassicDP
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class ClassicDP {

    /** dp[i][c] = best value using the first i items with capacity c. */
    static int[][] knapsackTable(int[] w, int[] v, int cap) {
        int n = w.length;
        int[][] dp = new int[n + 1][cap + 1];
        for (int i = 1; i <= n; i++)
            for (int c = 0; c <= cap; c++) {
                dp[i][c] = dp[i - 1][c];                                    // skip item i
                if (w[i - 1] <= c) dp[i][c] = Math.max(dp[i][c], dp[i - 1][c - w[i - 1]] + v[i - 1]);   // take it
            }
        return dp;
    }

    static List<Integer> knapsackItems(int[][] dp, int[] w) {
        List<Integer> items = new ArrayList<>();
        int c = dp[0].length - 1;
        for (int i = dp.length - 1; i > 0; i--)
            if (dp[i][c] != dp[i - 1][c]) { items.add(i - 1); c -= w[i - 1]; }   // value changed => item taken
        Collections.reverse(items);
        return items;
    }

    /** Same answer with one row; iterate capacity DOWNWARDS so each item is used at most once. */
    static int knapsack1D(int[] w, int[] v, int cap) {
        int[] dp = new int[cap + 1];
        for (int i = 0; i < w.length; i++)
            for (int c = cap; c >= w[i]; c--) dp[c] = Math.max(dp[c], dp[c - w[i]] + v[i]);
        return dp[cap];
    }

    /** LCS length table and one longest common subsequence. */
    static String lcs(String a, String b, int[][] outTable) {
        int n = a.length(), m = b.length();
        int[][] dp = outTable != null ? outTable : new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
        StringBuilder sb = new StringBuilder();
        for (int i = n, j = m; i > 0 && j > 0; ) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) { sb.append(a.charAt(i - 1)); i--; j--; }
            else if (dp[i - 1][j] >= dp[i][j - 1]) i--;
            else j--;
        }
        return sb.reverse().toString();
    }

    /** Fewest coins to make amount (-1 if impossible). Works for ANY coin system. */
    static int minCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins)
                if (c <= a && dp[a - c] != Integer.MAX_VALUE) dp[a] = Math.min(dp[a], dp[a - c] + 1);
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    /** Number of ways (combinations, order ignored): loop coins OUTSIDE, amounts inside. */
    static long coinWays(int[] coins, int amount) {
        long[] dp = new long[amount + 1];
        dp[0] = 1;
        for (int c : coins)
            for (int a = c; a <= amount; a++) dp[a] += dp[a - c];
        return dp[amount];
    }

    /** LIS in O(n^2): dp[i] = length of the longest increasing subsequence ending at i. */
    static List<Integer> lisQuadratic(int[] a) {
        int n = a.length;
        int[] dp = new int[n], prev = new int[n];
        int best = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++)
                if (a[j] < a[i] && dp[j] + 1 > dp[i]) { dp[i] = dp[j] + 1; prev[i] = j; }
            if (dp[i] > dp[best]) best = i;
        }
        List<Integer> seq = new ArrayList<>();
        for (int i = best; i != -1; i = prev[i]) seq.add(a[i]);
        Collections.reverse(seq);
        return seq;
    }

    /** LIS length in O(n log n): tails[k] = smallest possible tail of an increasing subsequence of length k+1. */
    static int lisFast(int[] a) {
        int[] tails = new int[a.length];
        int len = 0;
        for (int x : a) {
            int i = Arrays.binarySearch(tails, 0, len, x);
            if (i < 0) i = -(i + 1);              // insertion point
            tails[i] = x;
            if (i == len) len++;
        }
        return len;
    }

    public static void main(String[] args) {
        System.out.println("== 0/1 knapsack: laptop bag, capacity 7 kg ==");
        String[] names = {"laptop", "charger", "books", "camera"};
        int[] w = {3, 1, 4, 2}, v = {40, 10, 35, 25};
        int[][] dp = knapsackTable(w, v, 7);
        System.out.println("    c = 0..7");
        for (int i = 0; i < dp.length; i++)
            System.out.printf("  i=%d %-8s %s%n", i, i == 0 ? "(none)" : names[i - 1], Arrays.toString(dp[i]));
        List<String> chosen = new ArrayList<>();
        for (int i : knapsackItems(dp, w)) chosen.add(names[i]);
        System.out.println("best value = " + dp[4][7] + " with " + chosen + "; 1-D version = " + knapsack1D(w, v, 7));

        System.out.println("\n== Longest common subsequence ==");
        String a = "ABCBDAB", b = "BDCABA";
        int[][] t = new int[a.length() + 1][b.length() + 1];
        String s = lcs(a, b, t);
        System.out.println("      " + String.join("  ", b.split("")));
        for (int i = 1; i <= a.length(); i++) {
            StringBuilder row = new StringBuilder("  " + a.charAt(i - 1) + " ");
            for (int j = 1; j <= b.length(); j++) row.append(String.format("%3d", t[i][j]));
            System.out.println(row);
        }
        System.out.println("LCS(" + a + ", " + b + ") = " + s + " (length " + s.length() + ")");
        System.out.println("LCS of two student answers: \"" + lcs("the stack is lifo", "a stack is last in first out", null) + "\"");

        System.out.println("\n== Coin change ==");
        System.out.println("minCoins({1, 3, 4}, 6) = " + minCoins(new int[]{1, 3, 4}, 6) + "  (greedy gave 3)");
        System.out.println("minCoins({2}, 3) = " + minCoins(new int[]{2}, 3));
        System.out.println("ways to make 10 from {1, 2, 5} = " + coinWays(new int[]{1, 2, 5}, 10));
        System.out.println("ways to make Rs 100 from {1, 2, 5, 10, 20, 50, 100} = " + coinWays(new int[]{1, 2, 5, 10, 20, 50, 100}, 100));

        System.out.println("\n== Longest increasing subsequence ==");
        int[] marks = {52, 48, 60, 55, 70, 65, 80, 58, 90};
        System.out.println("marks = " + Arrays.toString(marks));
        System.out.println("O(n^2) LIS = " + lisQuadratic(marks) + ", O(n log n) length = " + lisFast(marks));
    }
}
