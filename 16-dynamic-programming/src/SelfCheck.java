import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Module 16 self-check: JUnit-free tests for dynamic programming (DP answers vs brute force).
 *   javac -d out *.java && java -ea -cp out SelfCheck
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SelfCheck {
    static int passed = 0, failed = 0;

    static void check(boolean ok, String name) {
        if (ok) passed++;
        else { failed++; System.out.println("FAIL: " + name); }
    }

    static boolean isSubseq(String sub, String s) {
        int i = 0;
        for (int j = 0; j < s.length() && i < sub.length(); j++) if (sub.charAt(i) == s.charAt(j)) i++;
        return i == sub.length();
    }

    public static void main(String[] args) {
        Random r = new Random(16);
        boolean fib = true;
        for (int n = 0; n <= 25; n++) {
            long a = DPIntro.fibNaive(n);
            fib &= a == DPIntro.fibMemo(n, new long[n + 1]) && a == DPIntro.fibTab(n) && a == DPIntro.fibTwoVars(n);
        }
        check(fib, "four Fibonacci versions agree");
        check(DPIntro.gridPaths(new String[]{"...", "...", "..."})[2][2] == 6, "3x3 grid has C(4,2) = 6 paths");

        // knapsack: table = 1-D = brute force over subsets
        boolean knap = true;
        for (int t = 0; t < 60; t++) {
            int n = 1 + r.nextInt(10), cap = r.nextInt(30);
            int[] w = new int[n], v = new int[n];
            for (int i = 0; i < n; i++) { w[i] = 1 + r.nextInt(10); v[i] = r.nextInt(50); }
            int brute = 0;
            for (int m = 0; m < 1 << n; m++) {
                int sw = 0, sv = 0;
                for (int i = 0; i < n; i++) if ((m >> i & 1) == 1) { sw += w[i]; sv += v[i]; }
                if (sw <= cap) brute = Math.max(brute, sv);
            }
            int[][] dp = ClassicDP.knapsackTable(w, v, cap);
            int sumChosen = 0, wChosen = 0;
            for (int i : ClassicDP.knapsackItems(dp, w)) { sumChosen += v[i]; wChosen += w[i]; }
            knap &= dp[n][cap] == brute && ClassicDP.knapsack1D(w, v, cap) == brute && sumChosen == brute && wChosen <= cap;
        }
        check(knap, "knapsack table / 1-D / reconstruction = brute force");

        boolean lcsOk = true;
        for (int t = 0; t < 60; t++) {
            String a = rnd(r, 8), b = rnd(r, 8);
            String l = ClassicDP.lcs(a, b, null);
            lcsOk &= isSubseq(l, a) && isSubseq(l, b);
            int best = 0;                                          // brute force: longest subsequence of a that is in b
            for (int m = 0; m < 1 << a.length(); m++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < a.length(); i++) if ((m >> i & 1) == 1) sb.append(a.charAt(i));
                if (isSubseq(sb.toString(), b)) best = Math.max(best, sb.length());
            }
            lcsOk &= l.length() == best;
        }
        check(lcsOk, "LCS is a common subsequence of maximum length");

        check(ClassicDP.minCoins(new int[]{1, 3, 4}, 6) == 2 && ClassicDP.minCoins(new int[]{5}, 3) == -1, "min coins");
        check(ClassicDP.coinWays(new int[]{1, 2, 5}, 5) == 4, "coin ways");
        boolean lis = true;
        for (int t = 0; t < 100; t++) {
            int[] a = new int[1 + r.nextInt(15)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(20);
            List<Integer> seq = ClassicDP.lisQuadratic(a);
            boolean inc = true;
            for (int i = 1; i < seq.size(); i++) inc &= seq.get(i) > seq.get(i - 1);
            lis &= inc && seq.size() == ClassicDP.lisFast(a);
        }
        check(lis, "LIS O(n^2) and O(n log n) agree");

        int[][] split = new int[3][3];
        check(AdvancedDP.matrixChain(new int[]{1, 2, 3, 4}, split) == 18, "matrix chain small");
        int[][] square = {{0, 1, 2, 1}, {1, 0, 1, 2}, {2, 1, 0, 1}, {1, 2, 1, 0}};
        check(AdvancedDP.tsp(square, new int[4]) == 4, "TSP on a square");
        List<List<Integer>> chain = new ArrayList<>();
        for (int i = 0; i < 4; i++) chain.add(new ArrayList<>());
        chain.get(0).add(1); chain.get(1).add(2); chain.get(2).add(3);
        int[] pr = AdvancedDP.party(0, chain, new int[]{1, 10, 1, 10});
        check(Math.max(pr[0], pr[1]) == 20, "tree DP on a chain");
        List<String> ch = new ArrayList<>();
        check(AdvancedDP.weightedSchedule(List.of(new AdvancedDP.Job("a", 0, 10, 5), new AdvancedDP.Job("b", 0, 3, 3), new AdvancedDP.Job("c", 3, 6, 3)), ch) == 6, "weighted schedule");

        boolean robber = true;
        for (int t = 0; t < 50; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(30);
            int best = 0;
            for (int m = 0; m < 1 << a.length; m++) {
                if ((m & (m >> 1)) != 0) continue;              // two adjacent houses
                int s = 0;
                for (int i = 0; i < a.length; i++) if ((m >> i & 1) == 1) s += a[i];
                best = Math.max(best, s);
            }
            robber &= Labs16.houseRobber(a) == best;
        }
        check(robber, "house robber = brute force");
        check(Labs16.decodeWays("10") == 1 && Labs16.decodeWays("27") == 1 && Labs16.decodeWays("0") == 0, "decode ways");
        check(Labs16.editDistance("", "abc") == 3 && Labs16.editDistance("same", "same") == 0, "edit distance");
        check(Labs16.canPartition(new int[]{2, 2}) && !Labs16.canPartition(new int[]{3}), "partition");
        check(Labs16.longestPalSubseq("a") == 1 && Labs16.longestPalSubseq("abcd") == 1, "palindromic subsequence");
        check(Labs16.wordBreak("", List.of("a")) && !Labs16.wordBreak("ab", List.of("a")), "word break");

        System.out.println("Module 16 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    static String rnd(Random r, int max) {
        StringBuilder sb = new StringBuilder();
        int n = r.nextInt(max + 1);
        for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(3)));
        return sb.toString();
    }
}
