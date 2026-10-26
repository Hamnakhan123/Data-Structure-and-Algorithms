import java.util.Arrays;

/**
 * Session 1 demo: from exponential recursion to dynamic programming. Fibonacci four ways (naive,
 * memoised, tabulated, O(1) space) with call counts and timings, climbing stairs, grid paths with
 * obstacles, and minimum-cost path with path reconstruction.
 *
 * Compile:  javac -d out DPIntro.java
 * Run:      java -cp out DPIntro
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class DPIntro {
    static long calls;

    static long fibNaive(int n) { calls++; return n < 2 ? n : fibNaive(n - 1) + fibNaive(n - 2); }

    /** Top-down: same recursion, but each answer is stored the first time it is computed. */
    static long fibMemo(int n, long[] memo) {
        calls++;
        if (n < 2) return n;
        if (memo[n] != 0) return memo[n];
        return memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    }

    /** Bottom-up: fill the table from the base cases upwards. */
    static long fibTab(int n) {
        if (n < 2) return n;
        long[] dp = new long[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) dp[i] = dp[i - 1] + dp[i - 2];
        return dp[n];
    }

    /** Only the last two values are ever needed: O(1) space. */
    static long fibTwoVars(int n) {
        long a = 0, b = 1;
        for (int i = 0; i < n; i++) { long t = a + b; a = b; b = t; }
        return a;
    }

    /** Ways to climb n stairs taking 1 or 2 steps: ways(i) = ways(i-1) + ways(i-2). */
    static long[] stairs(int n) {
        long[] w = new long[n + 1];
        w[0] = 1;
        for (int i = 1; i <= n; i++) w[i] = w[i - 1] + (i >= 2 ? w[i - 2] : 0);
        return w;
    }

    /** Paths from top-left to bottom-right moving right/down, avoiding blocked cells ('#'). */
    static long[][] gridPaths(String[] g) {
        int R = g.length, C = g[0].length();
        long[][] dp = new long[R][C];
        for (int r = 0; r < R; r++)
            for (int c = 0; c < C; c++) {
                if (g[r].charAt(c) == '#') continue;               // 0 ways through a wall
                if (r == 0 && c == 0) { dp[r][c] = 1; continue; }
                dp[r][c] = (r > 0 ? dp[r - 1][c] : 0) + (c > 0 ? dp[r][c - 1] : 0);
            }
        return dp;
    }

    /** Minimum-cost path (right/down) and the path itself, rebuilt by walking back through choices. */
    static String minCostPath(int[][] cost) {
        int R = cost.length, C = cost[0].length;
        int[][] dp = new int[R][C];
        for (int r = 0; r < R; r++)
            for (int c = 0; c < C; c++) {
                if (r == 0 && c == 0) dp[r][c] = cost[0][0];
                else if (r == 0) dp[r][c] = dp[r][c - 1] + cost[r][c];
                else if (c == 0) dp[r][c] = dp[r - 1][c] + cost[r][c];
                else dp[r][c] = Math.min(dp[r - 1][c], dp[r][c - 1]) + cost[r][c];
            }
        StringBuilder path = new StringBuilder();
        int r = R - 1, c = C - 1;
        while (r > 0 || c > 0) {                                     // reconstruct backwards
            if (r == 0) { path.append('R'); c--; }
            else if (c == 0) { path.append('D'); r--; }
            else if (dp[r - 1][c] <= dp[r][c - 1]) { path.append('D'); r--; }
            else { path.append('R'); c--; }
        }
        StringBuilder table = new StringBuilder();
        for (int[] row : dp) table.append("  ").append(Arrays.toString(row)).append('\n');
        return table + "  min cost = " + dp[R - 1][C - 1] + ", moves = " + path.reverse();
    }

    public static void main(String[] args) {
        System.out.println("== Fibonacci four ways ==");
        System.out.println("  n   naive calls   memo calls   value");
        for (int n : new int[]{10, 20, 30, 40}) {
            calls = 0;
            long v = fibNaive(n);
            long naive = calls;
            calls = 0;
            fibMemo(n, new long[n + 1]);
            System.out.printf("%3d   %11d   %10d   %d%n", n, naive, calls, v);
        }
        System.out.println("fibTab(90) = " + fibTab(90) + ", fibTwoVars(90) = " + fibTwoVars(90));

        long t0 = System.nanoTime();
        fibNaive(40);
        long naiveMs = (System.nanoTime() - t0) / 1_000_000;
        t0 = System.nanoTime();
        fibTab(40);
        long tabUs = (System.nanoTime() - t0) / 1_000;
        System.out.println("time for n = 40: naive ≈ " + (naiveMs > 100 ? "hundreds of ms" : "tens of ms")
                + ", table ≈ " + (tabUs < 1000 ? "under a millisecond" : "a few ms"));

        System.out.println("\n== Climbing stairs (1 or 2 steps) ==");
        System.out.println("ways for n = 0..10: " + Arrays.toString(stairs(10)));

        System.out.println("\n== Grid paths around blocked cells ==");
        String[] grid = {"....", ".#..", "...#", "#..."};
        for (String row : grid) System.out.println("  " + row);
        long[][] dp = gridPaths(grid);
        for (long[] row : dp) System.out.println("  " + Arrays.toString(row));
        System.out.println("  paths = " + dp[3][3]);

        System.out.println("\n== Minimum-cost path (right/down) ==");
        int[][] cost = {{1, 3, 1, 8}, {1, 5, 1, 2}, {4, 2, 1, 9}, {7, 1, 3, 1}};
        System.out.println(minCostPath(cost));
    }
}
