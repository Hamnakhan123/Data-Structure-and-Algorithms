import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Session 3 demo: advanced dynamic-programming patterns. Interval DP (matrix-chain multiplication
 * with the optimal parenthesisation), bitmask DP (travelling salesperson over a few campuses),
 * DP on trees (maximum-value guest list with no direct manager-report pairs), and weighted interval
 * scheduling (DP + binary search) compared with the greedy rule from Module 15.
 *
 * Compile:  javac -d out AdvancedDP.java
 * Run:      java -cp out AdvancedDP
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class AdvancedDP {

    // ---------- interval DP ----------
    /** dims has n+1 entries; matrix i is dims[i] x dims[i+1]. cost[i][j] = min multiplications for i..j. */
    static long matrixChain(int[] dims, int[][] split) {
        int n = dims.length - 1;
        long[][] cost = new long[n][n];
        for (int len = 2; len <= n; len++)                      // solve short chains before long ones
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                cost[i][j] = Long.MAX_VALUE;
                for (int k = i; k < j; k++) {                   // last multiplication splits at k
                    long c = cost[i][k] + cost[k + 1][j] + (long) dims[i] * dims[k + 1] * dims[j + 1];
                    if (c < cost[i][j]) { cost[i][j] = c; split[i][j] = k; }
                }
            }
        return cost[0][n - 1];
    }

    static String parens(int[][] split, int i, int j) {
        if (i == j) return "M" + (i + 1);
        return "(" + parens(split, i, split[i][j]) + " x " + parens(split, split[i][j] + 1, j) + ")";
    }

    // ---------- bitmask DP ----------
    /** Shortest tour visiting every city once and returning to city 0. dp[mask][v]: visited set, ending at v. */
    static int tsp(int[][] d, int[] tourOut) {
        int n = d.length, FULL = 1 << n, INF = Integer.MAX_VALUE / 2;
        int[][] dp = new int[FULL][n], parent = new int[FULL][n];
        for (int[] row : dp) Arrays.fill(row, INF);
        dp[1][0] = 0;
        for (int mask = 1; mask < FULL; mask++)
            for (int v = 0; v < n; v++) {
                if (dp[mask][v] >= INF || (mask & (1 << v)) == 0) continue;
                for (int u = 0; u < n; u++) {
                    if ((mask & (1 << u)) != 0) continue;      // already visited
                    int nm = mask | (1 << u), c = dp[mask][v] + d[v][u];
                    if (c < dp[nm][u]) { dp[nm][u] = c; parent[nm][u] = v; }
                }
            }
        int best = INF, last = -1;
        for (int v = 1; v < n; v++) if (dp[FULL - 1][v] + d[v][0] < best) { best = dp[FULL - 1][v] + d[v][0]; last = v; }
        int mask = FULL - 1;
        for (int k = n - 1; k >= 1; k--) { tourOut[k] = last; int p = parent[mask][last]; mask ^= 1 << last; last = p; }
        tourOut[0] = 0;
        return best;
    }

    // ---------- DP on trees ----------
    /** Max total "fun" of invited staff such that no one is invited together with their direct manager. */
    static int[] party(int v, List<List<Integer>> reports, int[] fun) {
        int with = fun[v], without = 0;                          // {best if v invited, best if not}
        for (int c : reports.get(v)) {
            int[] r = party(c, reports, fun);
            with += r[1];                                        // v invited -> child cannot be
            without += Math.max(r[0], r[1]);                     // v not invited -> child free
        }
        return new int[]{with, without};
    }

    // ---------- weighted interval scheduling ----------
    record Job(String name, int start, int end, int pay) { }

    static int weightedSchedule(List<Job> jobs, List<String> chosen) {
        List<Job> s = new ArrayList<>(jobs);
        s.sort(Comparator.comparingInt(Job::end));
        int n = s.size();
        int[] dp = new int[n + 1], p = new int[n];
        for (int i = 0; i < n; i++) {                            // p[i] = number of jobs ending by start of job i
            int lo = 0, hi = i;
            while (lo < hi) { int mid = (lo + hi) / 2; if (s.get(mid).end() <= s.get(i).start()) lo = mid + 1; else hi = mid; }
            p[i] = lo;
        }
        for (int i = 1; i <= n; i++)
            dp[i] = Math.max(dp[i - 1], s.get(i - 1).pay() + dp[p[i - 1]]);   // skip job i, or take it
        for (int i = n; i > 0; ) {
            if (dp[i] == dp[i - 1]) i--;
            else { chosen.add(0, s.get(i - 1).name()); i = p[i - 1]; }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println("== Interval DP: matrix-chain multiplication ==");
        int[] dims = {10, 30, 5, 60, 10};                        // M1 10x30, M2 30x5, M3 5x60, M4 60x10
        int[][] split = new int[4][4];
        long best = matrixChain(dims, split);
        System.out.println("dims " + Arrays.toString(dims) + " -> best order " + parens(split, 0, 3) + " = " + best + " multiplications");
        long leftToRight = 10L * 30 * 5 + 10L * 5 * 60 + 10L * 60 * 10;
        System.out.println("left to right ((M1 x M2) x M3) x M4 = " + leftToRight + " multiplications");

        System.out.println("\n== Bitmask DP: shortest tour of 5 campuses (km) ==");
        String[] campus = {"Main", "North", "City", "East", "Port"};
        int[][] d = {
            {0, 12, 10, 19, 8},
            {12, 0, 3, 7, 2},
            {10, 3, 0, 6, 20},
            {19, 7, 6, 0, 4},
            {8, 2, 20, 4, 0}};
        int[] tour = new int[5];
        int len = tsp(d, tour);
        StringBuilder sb = new StringBuilder();
        for (int v : tour) sb.append(campus[v]).append(" -> ");
        System.out.println("tour " + sb + "Main = " + len + " km  (states: 2^5 x 5 = 160)");

        System.out.println("\n== DP on a tree: department party ==");
        String[] staff = {"Head", "Ali", "Sana", "Bilal", "Hira", "Omar", "Zoya"};
        int[] fun = {5, 6, 4, 3, 8, 2, 7};
        List<List<Integer>> rep = new ArrayList<>();
        for (int i = 0; i < staff.length; i++) rep.add(new ArrayList<>());
        rep.get(0).addAll(List.of(1, 2));                        // Head manages Ali, Sana
        rep.get(1).addAll(List.of(3, 4));                        // Ali manages Bilal, Hira
        rep.get(2).addAll(List.of(5, 6));                        // Sana manages Omar, Zoya
        int[] r = party(0, rep, fun);
        System.out.println("fun: " + Arrays.toString(fun) + " for " + Arrays.toString(staff));
        System.out.println("best with Head = " + r[0] + ", without Head = " + r[1] + " -> answer " + Math.max(r[0], r[1]));

        System.out.println("\n== Weighted interval scheduling: freelance jobs ==");
        List<Job> jobs = List.of(new Job("website", 1, 4, 50), new Job("logo", 3, 5, 20), new Job("app", 0, 6, 200),
                new Job("data", 4, 7, 70), new Job("report", 5, 9, 60), new Job("audit", 7, 10, 40));
        List<String> chosen = new ArrayList<>();
        int pay = weightedSchedule(jobs, chosen);
        System.out.println("DP: max pay = " + pay + " with " + chosen);
        List<Job> byEnd = new ArrayList<>(jobs);
        byEnd.sort(Comparator.comparingInt(Job::end));
        int greedyPay = 0, lastEnd = Integer.MIN_VALUE;
        List<String> g = new ArrayList<>();
        for (Job j : byEnd) if (j.start() >= lastEnd) { greedyPay += j.pay(); lastEnd = j.end(); g.add(j.name()); }
        System.out.println("greedy earliest-finish (ignores pay): " + greedyPay + " with " + g);
    }
}
