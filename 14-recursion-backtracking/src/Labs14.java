import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Module 14 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out Labs14 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs14 {

    // >>> parentheses
    /** All balanced strings of n pairs: add '(' while opens remain, add ')' only if it stays balanced. */
    static List<String> parentheses(int n) {
        List<String> out = new ArrayList<>();
        gen(n, 0, 0, new StringBuilder(), out);
        return out;
    }

    private static void gen(int n, int open, int close, StringBuilder sb, List<String> out) {
        if (sb.length() == 2 * n) { out.add(sb.toString()); return; }
        if (open < n) { sb.append('('); gen(n, open + 1, close, sb, out); sb.deleteCharAt(sb.length() - 1); }
        if (close < open) { sb.append(')'); gen(n, open, close + 1, sb, out); sb.deleteCharAt(sb.length() - 1); }
    }
    // <<< parentheses

    // >>> phoneLetters
    /** Letter combinations of a phone number: one choice per digit (the keypad mapping). */
    static final String[] KEYS = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    static List<String> phoneLetters(String digits) {
        List<String> out = new ArrayList<>();
        if (!digits.isEmpty()) letters(digits, 0, new StringBuilder(), out);
        return out;
    }

    private static void letters(String d, int i, StringBuilder sb, List<String> out) {
        if (i == d.length()) { out.add(sb.toString()); return; }
        for (char c : KEYS[d.charAt(i) - '0'].toCharArray()) {
            sb.append(c);
            letters(d, i + 1, sb, out);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
    // <<< phoneLetters

    // >>> subsetsWithDup
    /** Subsets without duplicate results when the input has repeats: sort, then skip equal siblings. */
    static List<List<Integer>> subsetsWithDup(int[] a) {
        int[] s = a.clone();
        Arrays.sort(s);
        List<List<Integer>> out = new ArrayList<>();
        dup(s, 0, new ArrayList<>(), out);
        return out;
    }

    private static void dup(int[] s, int start, List<Integer> cur, List<List<Integer>> out) {
        out.add(new ArrayList<>(cur));
        for (int i = start; i < s.length; i++) {
            if (i > start && s[i] == s[i - 1]) continue;     // same value already tried at this level
            cur.add(s[i]);
            dup(s, i + 1, cur, out);
            cur.remove(cur.size() - 1);
        }
    }
    // <<< subsetsWithDup

    // >>> wordSearch
    /** Does the word appear in the grid along adjacent (up/down/left/right) cells, each used once? */
    static boolean wordSearch(char[][] g, String w) {
        for (int r = 0; r < g.length; r++)
            for (int c = 0; c < g[0].length; c++)
                if (find(g, w, 0, r, c)) return true;
        return false;
    }

    private static boolean find(char[][] g, String w, int k, int r, int c) {
        if (k == w.length()) return true;
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != w.charAt(k)) return false;
        char saved = g[r][c];
        g[r][c] = '#';                                   // mark as used on this path
        boolean ok = find(g, w, k + 1, r + 1, c) || find(g, w, k + 1, r - 1, c)
                || find(g, w, k + 1, r, c + 1) || find(g, w, k + 1, r, c - 1);
        g[r][c] = saved;                                 // restore (backtrack)
        return ok;
    }
    // <<< wordSearch

    // >>> palindromePartition
    /** All ways to cut a string into palindromic pieces. */
    static List<List<String>> palindromePartition(String s) {
        List<List<String>> out = new ArrayList<>();
        part(s, 0, new ArrayList<>(), out);
        return out;
    }

    private static void part(String s, int start, List<String> cur, List<List<String>> out) {
        if (start == s.length()) { out.add(new ArrayList<>(cur)); return; }
        for (int end = start + 1; end <= s.length(); end++) {
            if (!isPal(s, start, end - 1)) continue;     // prune: this cut cannot lead to a solution
            cur.add(s.substring(start, end));
            part(s, end, cur, out);
            cur.remove(cur.size() - 1);
        }
    }

    static boolean isPal(String s, int i, int j) {
        while (i < j) if (s.charAt(i++) != s.charAt(j--)) return false;
        return true;
    }
    // <<< palindromePartition

    // >>> examColouring
    /** Assign each exam one of m time slots so that clashing exams differ (graph m-colouring). */
    static int[] colour(boolean[][] clash, int m) {
        int[] slot = new int[clash.length];
        return assign(clash, m, 0, slot) ? slot : null;
    }

    private static boolean assign(boolean[][] clash, int m, int v, int[] slot) {
        if (v == clash.length) return true;
        for (int s = 1; s <= m; s++) {
            boolean ok = true;
            for (int u = 0; u < v && ok; u++) if (clash[v][u] && slot[u] == s) ok = false;
            if (!ok) continue;
            slot[v] = s;
            if (assign(clash, m, v + 1, slot)) return true;
            slot[v] = 0;
        }
        return false;
    }
    // <<< examColouring

    public static void main(String[] args) {
        int s = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (s == 0 || s == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("parentheses(3) = " + parentheses(3));
            System.out.println("phoneLetters(\"23\") = " + phoneLetters("23"));
            System.out.println("subsetsWithDup([1, 2, 2]) = " + subsetsWithDup(new int[]{1, 2, 2}));
        }
        if (s == 0 || s == 2) {
            System.out.println("-- Session 2 labs --");
            char[][] g = {"ABCE".toCharArray(), "SFCS".toCharArray(), "ADEE".toCharArray()};
            System.out.println("wordSearch ABCCED = " + wordSearch(g, "ABCCED") + ", SEE = " + wordSearch(g, "SEE")
                    + ", ABCB = " + wordSearch(g, "ABCB"));
            System.out.println("palindromePartition(\"aab\") = " + palindromePartition("aab"));
            String[] exams = {"DSA", "OOP", "AI", "DB", "Math"};
            boolean[][] clash = new boolean[5][5];
            int[][] pairs = {{0, 1}, {0, 2}, {1, 2}, {1, 3}, {2, 3}, {3, 4}};
            for (int[] p : pairs) clash[p[0]][p[1]] = clash[p[1]][p[0]] = true;
            int[] two = colour(clash, 2), three = colour(clash, 3);
            System.out.println("2 slots possible? " + (two != null) + "; 3 slots: " + Arrays.toString(three)
                    + " for " + Arrays.toString(exams));
        }
    }
}
