import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Module 16 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out Labs16 1    (Session 1 labs; 2 or 3 for the others; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs16 {

    // >>> houseRobber
    /** Max money from houses in a row without robbing two neighbours: best(i) = max(best(i-1), best(i-2) + a[i]). */
    static int houseRobber(int[] a) {
        int prev2 = 0, prev1 = 0;                       // best up to i-2 and i-1
        for (int x : a) { int cur = Math.max(prev1, prev2 + x); prev2 = prev1; prev1 = cur; }
        return prev1;
    }
    // <<< houseRobber

    // >>> decodeWays
    /** Ways to decode a digit string where 'A'=1 ... 'Z'=26. ways(i) uses one digit or two digits. */
    static int decodeWays(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            if (s.charAt(i - 1) != '0') dp[i] += dp[i - 1];                         // single digit 1-9
            if (i >= 2) {
                int two = Integer.parseInt(s.substring(i - 2, i));
                if (two >= 10 && two <= 26) dp[i] += dp[i - 2];                       // two digits 10-26
            }
        }
        return dp[n];
    }
    // <<< decodeWays

    // >>> editDistance
    /** Minimum insertions, deletions and substitutions to turn a into b. */
    static int editDistance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;            // delete everything
        for (int j = 0; j <= m; j++) dp[0][j] = j;            // insert everything
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1]
                        : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        return dp[n][m];
    }
    // <<< editDistance

    // >>> canPartition
    /** Can the array be split into two groups with equal sums? Subset sum to total/2 with a boolean table. */
    static boolean canPartition(int[] a) {
        int total = Arrays.stream(a).sum();
        if (total % 2 == 1) return false;
        boolean[] can = new boolean[total / 2 + 1];
        can[0] = true;
        for (int x : a)
            for (int s = total / 2; s >= x; s--) can[s] |= can[s - x];        // downwards: each item once
        return can[total / 2];
    }
    // <<< canPartition

    // >>> longestPalSubseq
    /** Longest palindromic subsequence: interval DP over substrings s[i..j]. */
    static int longestPalSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++)
                dp[i][j] = s.charAt(i) == s.charAt(j) ? dp[i + 1][j - 1] + 2 : Math.max(dp[i + 1][j], dp[i][j - 1]);
        }
        return n == 0 ? 0 : dp[0][n - 1];
    }
    // <<< longestPalSubseq

    // >>> wordBreak
    /** Can s be segmented into dictionary words? ok[i] = some ok[j] with s[j..i) in the dictionary. */
    static boolean wordBreak(String s, List<String> dict) {
        Set<String> words = new HashSet<>(dict);
        boolean[] ok = new boolean[s.length() + 1];
        ok[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i && !ok[i]; j++)
                ok[i] = ok[j] && words.contains(s.substring(j, i));
        return ok[s.length()];
    }
    // <<< wordBreak

    public static void main(String[] args) {
        int s = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (s == 0 || s == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("houseRobber([2, 7, 9, 3, 1]) = " + houseRobber(new int[]{2, 7, 9, 3, 1}));
            System.out.println("decodeWays(\"226\") = " + decodeWays("226") + ", decodeWays(\"1201\") = " + decodeWays("1201")
                    + ", decodeWays(\"06\") = " + decodeWays("06"));
        }
        if (s == 0 || s == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("editDistance(kitten, sitting) = " + editDistance("kitten", "sitting")
                    + ", editDistance(algorithm, altruistic) = " + editDistance("algorithm", "altruistic"));
            System.out.println("canPartition([1, 5, 11, 5]) = " + canPartition(new int[]{1, 5, 11, 5})
                    + ", canPartition([1, 2, 3, 5]) = " + canPartition(new int[]{1, 2, 3, 5}));
        }
        if (s == 0 || s == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("longestPalSubseq(\"bbbab\") = " + longestPalSubseq("bbbab") + ", (\"character\") = " + longestPalSubseq("character"));
            System.out.println("wordBreak(\"datastructures\") = " + wordBreak("datastructures", List.of("data", "struct", "structures", "ures"))
                    + ", wordBreak(\"catsandog\") = " + wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")));
        }
    }
}
