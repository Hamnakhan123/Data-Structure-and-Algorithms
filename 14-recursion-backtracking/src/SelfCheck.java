import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Module 14 self-check: JUnit-free tests for backtracking (counts checked against formulas).
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

    static long binom(int n, int k) { long r = 1; for (int i = 1; i <= k; i++) r = r * (n - k + i) / i; return r; }

    public static void main(String[] args) {
        for (int n = 0; n <= 10; n++) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i;
            List<List<Integer>> subs = BacktrackingBasics.subsets(a);
            check(subs.size() == 1 << n && new HashSet<>(subs).size() == subs.size(), "subsets of " + n);
        }
        for (int n = 1; n <= 7; n++) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i;
            List<List<Integer>> p = BacktrackingBasics.permutations(a);
            check(p.size() == BacktrackingBasics.factorial(n) && new HashSet<>(p).size() == p.size(), "permutations of " + n);
        }
        for (int n = 1; n <= 9; n++) for (int k = 0; k <= n; k++) {
            check(BacktrackingBasics.combinations(n, k, true).size() == binom(n, k), "C(" + n + "," + k + ")");
            check(BacktrackingBasics.combinations(n, k, true).equals(BacktrackingBasics.combinations(n, k, false)), "pruning same result " + n + "," + k);
        }
        List<List<Integer>> cs = BacktrackingBasics.combinationSum(new int[]{2, 3, 5}, 8);
        boolean sumsOk = cs.size() == 3;
        for (List<Integer> l : cs) sumsOk &= l.stream().mapToInt(Integer::intValue).sum() == 8;
        check(sumsOk, "combination sum");

        int[] known = {1, 0, 0, 2, 10, 4, 40, 92, 352, 724};
        for (int n = 1; n <= 10; n++) check(ConstraintProblems.countQueens(n) == known[n - 1], "queens " + n);
        int[] q = ConstraintProblems.firstQueens(8);
        boolean safe = true;
        for (int i = 0; i < 8; i++) for (int j = i + 1; j < 8; j++) safe &= q[i] != q[j] && Math.abs(q[i] - q[j]) != j - i;
        check(safe, "first 8-queens board is safe");

        int[][] g = ConstraintProblems.parse(new String[]{
            "..9748...", "7........", ".2.1.9...", "..7...24.", ".64.1.59.", ".98...3..", "...8.3.2.", "........6", "...2759.."});
        check(ConstraintProblems.solve(g, true), "hard sudoku solved");
        boolean valid = true;
        for (int i = 0; i < 9; i++) {
            Set<Integer> row = new HashSet<>(), col = new HashSet<>(), box = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                row.add(g[i][j]); col.add(g[j][i]); box.add(g[3 * (i / 3) + j / 3][3 * (i % 3) + j % 3]);
            }
            valid &= row.size() == 9 && col.size() == 9 && box.size() == 9 && !row.contains(0);
        }
        check(valid, "sudoku solution valid");

        check(Labs14.parentheses(4).size() == 14, "Catalan(4) = 14");
        check(Labs14.phoneLetters("79").size() == 16 && Labs14.phoneLetters("").isEmpty(), "phone letters");
        check(Labs14.subsetsWithDup(new int[]{2, 2, 2}).size() == 4, "subsets with dup");
        check(!Labs14.wordSearch(new char[][]{"aa".toCharArray()}, "aaa"), "cell reuse forbidden");
        check(Labs14.palindromePartition("aaa").size() == 4, "palindrome partitions");
        boolean[][] triangle = {{false, true, true}, {true, false, true}, {true, true, false}};
        check(Labs14.colour(triangle, 2) == null && Labs14.colour(triangle, 3) != null, "triangle needs 3 colours");

        System.out.println("Module 14 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
