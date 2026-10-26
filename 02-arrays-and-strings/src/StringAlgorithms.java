import java.util.Arrays;

/**
 * Module 02 · Session 03 — Strings, StringBuilder and pattern matching.
 *
 * Immutability and StringBuilder, frequency-count anagram check, word reversal,
 * run-length compression, naive substring search and the Knuth-Morris-Pratt (KMP)
 * algorithm with its LPS (longest proper prefix that is also a suffix) table.
 *
 * Compile:  javac -d out StringAlgorithms.java
 * Run:      java -cp out StringAlgorithms
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class StringAlgorithms {

    static long comparisons; // counts character comparisons in the search algorithms

    /** Anagram check with a 26-slot frequency array: O(n) time, O(1) space. */
    static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) return false;
        int[] freq = new int[26];
        for (int i = 0; i < a.length(); i++) {
            freq[a.charAt(i) - 'a']++;
            freq[b.charAt(i) - 'a']--;
        }
        for (int f : freq) if (f != 0) return false;
        return true;
    }

    /** "data structures in java" -> "java in structures data". */
    static String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) sb.append(' ');
        }
        return sb.toString();
    }

    /** Run-length encoding: "aaabccdddd" -> "a3b1c2d4". */
    static String compress(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
            sb.append(s.charAt(i)).append(j - i);
            i = j;
        }
        return sb.toString();
    }

    /** Naive search: try every shift. Worst case O(n*m). Returns first index or -1. */
    static int naiveSearch(String text, String pat) {
        int n = text.length(), m = pat.length();
        for (int s = 0; s <= n - m; s++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(s + j) != pat.charAt(j)) break;
                j++;
            }
            if (j == m) return s;
        }
        return -1;
    }

    /** lps[i] = length of the longest proper prefix of pat[0..i] that is also a suffix of it. */
    static int[] buildLps(String pat) {
        int[] lps = new int[pat.length()];
        int len = 0; // length of the current matched prefix
        for (int i = 1; i < pat.length(); ) {
            if (pat.charAt(i) == pat.charAt(len)) {
                lps[i++] = ++len;
            } else if (len > 0) {
                len = lps[len - 1]; // fall back, do not move i
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    /** KMP search: never moves backwards in the text. O(n + m). */
    static int kmpSearch(String text, String pat) {
        int[] lps = buildLps(pat);
        int j = 0; // characters of pat matched so far
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pat.charAt(j)) {
                comparisons++;
                j = lps[j - 1];
            }
            comparisons++;
            if (text.charAt(i) == pat.charAt(j)) j++;
            if (j == pat.length()) return i - j + 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== immutability ===");
        String s = "data";
        String t = s.toUpperCase();
        System.out.println("s = " + s + ", s.toUpperCase() = " + t + "  (s itself is unchanged)");

        int n = 20_000;
        long t0 = System.nanoTime();
        String slow = "";
        for (int i = 0; i < n; i++) slow += 'x';
        long t1 = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append('x');
        String fast = sb.toString();
        long t2 = System.nanoTime();
        System.out.println("+= built " + slow.length() + " chars; StringBuilder built " + fast.length() + " chars");
        System.out.println("StringBuilder faster? " + ((t2 - t1) < (t1 - t0)));

        System.out.println("\n=== classic string problems ===");
        System.out.println("isAnagram(listen, silent) = " + isAnagram("listen", "silent"));
        System.out.println("isAnagram(karachi, chakira) = " + isAnagram("karachi", "chakira"));
        System.out.println("isAnagram(lahore, hollow) = " + isAnagram("lahore", "hollow"));
        System.out.println("reverseWords = \"" + reverseWords("  data structures   in java ") + "\"");
        System.out.println("compress(aaabccdddd) = " + compress("aaabccdddd"));

        System.out.println("\n=== pattern matching ===");
        String pat = "ABABCABAB";
        System.out.println("LPS of " + pat + " = " + Arrays.toString(buildLps(pat)));
        String text = "ABABDABACDABABCABABCABAB";
        comparisons = 0;
        int a = naiveSearch(text, pat);
        long naiveCmp = comparisons;
        comparisons = 0;
        int b = kmpSearch(text, pat);
        System.out.println("text = " + text);
        System.out.println("naive -> index " + a + " with " + naiveCmp + " comparisons");
        System.out.println("KMP   -> index " + b + " with " + comparisons + " comparisons");

        String worstText = "A".repeat(5000) + "B";
        String worstPat = "A".repeat(50) + "B";
        comparisons = 0;
        naiveSearch(worstText, worstPat);
        long w1 = comparisons;
        comparisons = 0;
        kmpSearch(worstText, worstPat);
        System.out.printf("worst case (n=%d, m=%d): naive %,d vs KMP %,d comparisons%n",
                worstText.length(), worstPat.length(), w1, comparisons);
    }
}
