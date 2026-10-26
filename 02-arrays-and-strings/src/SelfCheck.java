import java.util.List;

/**
 * Module 02 self-check: JUnit-free tests for arrays, matrices and strings.
 *   javac -d out *.java && java -ea -cp out SelfCheck
 * Exits with status 1 if any check fails.
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SelfCheck {
    static int passed = 0, failed = 0;

    static void check(boolean ok, String name) {
        if (ok) passed++;
        else { failed++; System.out.println("FAIL: " + name); }
    }

    public static void main(String[] args) {
        DynamicArray<String> d = new DynamicArray<>(1);
        for (String s : new String[]{"a", "b", "c", "d", "e"}) d.add(s);
        check(d.size() == 5 && d.capacity() == 8, "doubling 1->2->4->8");
        d.insert(2, "x");
        check(d.toString().equals("[a, b, x, c, d, e]"), "insert middle");
        check(d.removeAt(0).equals("a") && d.get(0).equals("b"), "removeAt shifts left");
        check(d.indexOf("e") == 4 && d.indexOf("zz") == -1, "indexOf");
        boolean threw = false;
        try { d.get(99); } catch (IndexOutOfBoundsException e) { threw = true; }
        check(threw, "bounds check");

        int[][] m = {{1, 2, 3}, {4, 5, 6}};
        check(MatrixOps.transpose(m)[2][1] == 6, "transpose");
        check(MatrixOps.spiral(m).equals(List.of(1, 2, 3, 6, 5, 4)), "spiral");
        int[][] sq = {{1, 2}, {3, 4}};
        MatrixOps.rotateClockwise(sq);
        check(sq[0][0] == 3 && sq[0][1] == 1 && sq[1][0] == 4 && sq[1][1] == 2, "rotate");
        int[][] p = MatrixOps.multiply(new int[][]{{1, 2}}, new int[][]{{3}, {4}});
        check(p[0][0] == 11, "multiply 1x2 * 2x1");
        long[][] pre = MatrixOps.prefixSums(new int[][]{{1, 2}, {3, 4}});
        check(MatrixOps.rectSum(pre, 0, 0, 1, 1) == 10 && MatrixOps.rectSum(pre, 1, 1, 1, 1) == 4, "rectSum");

        check(StringAlgorithms.isAnagram("listen", "silent"), "anagram yes");
        check(!StringAlgorithms.isAnagram("abc", "abd"), "anagram no");
        check(StringAlgorithms.reverseWords(" a  b c ").equals("c b a"), "reverseWords");
        check(StringAlgorithms.compress("aab").equals("a2b1"), "compress");
        check(StringAlgorithms.kmpSearch("hello world", "o w") == 4, "kmp found");
        check(StringAlgorithms.kmpSearch("aaaa", "b") == -1, "kmp absent");
        check(StringAlgorithms.naiveSearch("abcabd", "abd") == 3, "naive");
        check(java.util.Arrays.equals(StringAlgorithms.buildLps("AAACAAAA"), new int[]{0, 1, 2, 0, 1, 2, 3, 3}), "lps");

        int[] r = {1, 2, 3, 4, 5};
        Labs02.rotateRight(r, 7);
        check(java.util.Arrays.equals(r, new int[]{4, 5, 1, 2, 3}), "rotate k > n");
        check(Labs02.dedupSorted(new int[]{2, 2, 2}) == 1, "dedup");
        check(Labs02.firstUnique("aabb") == -1, "firstUnique none");
        check(Labs02.longestCommonPrefix(new String[]{"dog", "cat"}).isEmpty(), "lcp empty");
        check(!Labs02.isCleanPalindrome("race a car"), "clean palindrome no");

        System.out.println("Module 02 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
