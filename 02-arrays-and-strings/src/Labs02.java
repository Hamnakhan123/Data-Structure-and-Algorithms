import java.util.Arrays;

/**
 * Module 02 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs02.java
 * Run:      java -cp out Labs02 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs02 {

    // >>> reverseInPlace
    /** Two indices walk toward each other swapping: O(n) time, O(1) space. */
    static void reverse(int[] a, int lo, int hi) {
        while (lo < hi) {
            int t = a[lo]; a[lo] = a[hi]; a[hi] = t;
            lo++; hi--;
        }
    }
    // <<< reverseInPlace

    // >>> rotateRight
    /** Rotate right by k using three reversals: [1..7], k=3 -> [5,6,7,1,2,3,4]. */
    static void rotateRight(int[] a, int k) {
        int n = a.length;
        if (n == 0) return;
        k %= n;                 // rotating by n is a no-op
        reverse(a, 0, n - 1);   // whole array
        reverse(a, 0, k - 1);   // first k
        reverse(a, k, n - 1);   // the rest
    }
    // <<< rotateRight

    // >>> dedupSorted
    /** Removes duplicates from a SORTED array in place; returns the new logical length. */
    static int dedupSorted(int[] a) {
        if (a.length == 0) return 0;
        int write = 1;                       // a[0..write-1] holds the unique prefix
        for (int read = 1; read < a.length; read++) {
            if (a[read] != a[write - 1]) a[write++] = a[read];
        }
        return write;
    }
    // <<< dedupSorted

    // >>> staircase
    /** Rows and columns both sorted ascending: start top-right, move left or down. O(r + c). */
    static int[] searchSortedMatrix(int[][] m, int target) {
        int i = 0, j = m[0].length - 1;
        while (i < m.length && j >= 0) {
            if (m[i][j] == target) return new int[]{i, j};
            if (m[i][j] > target) j--;   // everything below in this column is bigger too
            else i++;                    // everything left in this row is smaller too
        }
        return new int[]{-1, -1};
    }
    // <<< staircase

    // >>> rowColSums
    /** Totals per row and per column, e.g. marks per student and per subject. */
    static int[][] rowColSums(int[][] m) {
        int[] rows = new int[m.length], cols = new int[m[0].length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++) {
                rows[i] += m[i][j];
                cols[j] += m[i][j];
            }
        return new int[][]{rows, cols};
    }
    // <<< rowColSums

    // >>> firstUnique
    /** Index of the first non-repeating lowercase character, or -1. Two passes, O(n). */
    static int firstUnique(String s) {
        int[] count = new int[26];
        for (char ch : s.toCharArray()) count[ch - 'a']++;
        for (int i = 0; i < s.length(); i++) if (count[s.charAt(i) - 'a'] == 1) return i;
        return -1;
    }
    // <<< firstUnique

    // >>> commonPrefix
    /** Longest common prefix of all words: shrink the candidate until every word starts with it. */
    static String longestCommonPrefix(String[] words) {
        if (words.length == 0) return "";
        String prefix = words[0];
        for (String w : words)
            while (!w.startsWith(prefix)) prefix = prefix.substring(0, prefix.length() - 1);
        return prefix;
    }
    // <<< commonPrefix

    // >>> cleanPalindrome
    /** Palindrome check ignoring case, spaces and punctuation, without building a new string. */
    static boolean isCleanPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (!Character.isLetterOrDigit(s.charAt(i))) { i++; continue; }
            if (!Character.isLetterOrDigit(s.charAt(j))) { j--; continue; }
            if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) return false;
            i++; j--;
        }
        return true;
    }
    // <<< cleanPalindrome

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            int[] a = {1, 2, 3, 4, 5, 6, 7};
            rotateRight(a, 3);
            System.out.println("rotateRight([1..7], 3) = " + Arrays.toString(a));
            int[] d = {1, 1, 2, 3, 3, 3, 7, 9, 9};
            int len = dedupSorted(d);
            System.out.println("dedupSorted -> length " + len + ": " + Arrays.toString(Arrays.copyOf(d, len)));

        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            int[][] m = {{1, 4, 7, 11}, {2, 5, 8, 12}, {3, 6, 9, 16}, {10, 13, 14, 17}};
            System.out.println("search 9 in sorted matrix -> " + Arrays.toString(searchSortedMatrix(m, 9)));
            System.out.println("search 15 in sorted matrix -> " + Arrays.toString(searchSortedMatrix(m, 15)));
            int[][] marks = {{70, 85, 90}, {60, 75, 80}};
            int[][] rc = rowColSums(marks);
            System.out.println("row sums " + Arrays.toString(rc[0]) + ", column sums " + Arrays.toString(rc[1]));

        }
        if (session == 0 || session == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("firstUnique(\"statistics\") = " + firstUnique("statistics"));
            System.out.println("longestCommonPrefix = \"" + longestCommonPrefix(new String[]{"interview", "internet", "interval"}) + "\"");
            System.out.println("isCleanPalindrome(\"A man, a plan, a canal: Panama\") = "
                    + isCleanPalindrome("A man, a plan, a canal: Panama"));
        }
    }
}
