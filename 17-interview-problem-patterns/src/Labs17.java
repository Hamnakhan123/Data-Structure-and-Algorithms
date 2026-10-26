/**
 * Module 17 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out Labs17 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs17 {

    // >>> validPalindrome
    /** Palindrome check ignoring case and non-alphanumerics: two pointers moving inward. */
    static boolean validPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (!Character.isLetterOrDigit(s.charAt(i))) { i++; continue; }
            if (!Character.isLetterOrDigit(s.charAt(j))) { j--; continue; }
            if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) return false;
            i++; j--;
        }
        return true;
    }
    // <<< validPalindrome

    // >>> minWindow
    /** Shortest substring of s containing every character of t (with multiplicity). Variable window. */
    static String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int missing = t.length(), left = 0, bestL = 0, bestLen = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
            if (need[s.charAt(right)]-- > 0) missing--;          // this char was still needed
            while (missing == 0) {                               // valid window: try to shrink it
                if (right - left + 1 < bestLen) { bestLen = right - left + 1; bestL = left; }
                if (++need[s.charAt(left++)] > 0) missing++;     // dropped a needed char
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
    }
    // <<< minWindow

    // >>> maxVowels
    /** Most vowels in any window of length k: fixed window with an add/drop update. */
    static int maxVowels(String s, int k) {
        String v = "aeiou";
        int cur = 0, best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (v.indexOf(s.charAt(i)) >= 0) cur++;
            if (i >= k && v.indexOf(s.charAt(i - k)) >= 0) cur--;
            best = Math.max(best, cur);
        }
        return best;
    }
    // <<< maxVowels

    // >>> minSpeed
    /** Slowest marking speed (papers per hour) to finish all piles within h hours. Binary search on the answer. */
    static int minSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            long hours = 0;
            for (int p : piles) hours += (p + mid - 1) / mid;    // ceil(p / mid)
            if (hours <= h) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    // <<< minSpeed

    // >>> searchRotated
    /** Search in a rotated sorted array: one half is always sorted; check whether the target lies in it. */
    static int searchRotated(int[] a, int target) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] == target) return mid;
            if (a[lo] <= a[mid]) {                                // left half sorted
                if (a[lo] <= target && target < a[mid]) hi = mid - 1; else lo = mid + 1;
            } else {                                              // right half sorted
                if (a[mid] < target && target <= a[hi]) lo = mid + 1; else hi = mid - 1;
            }
        }
        return -1;
    }
    // <<< searchRotated

    // >>> majority
    /** Element appearing more than n/2 times (guaranteed): Boyer-Moore voting, O(n) time, O(1) space. */
    static int majority(int[] a) {
        int cand = 0, count = 0;
        for (int x : a) {
            if (count == 0) cand = x;
            count += x == cand ? 1 : -1;
        }
        return cand;
    }
    // <<< majority

    public static void main(String[] args) {
        int s = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (s == 0 || s == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("validPalindrome(\"A man, a plan, a canal: Panama\") = " + validPalindrome("A man, a plan, a canal: Panama")
                    + ", (\"race a car\") = " + validPalindrome("race a car"));
            System.out.println("minWindow(\"ADOBECODEBANC\", \"ABC\") = \"" + minWindow("ADOBECODEBANC", "ABC") + "\"");
            System.out.println("maxVowels(\"abciiidef\", 3) = " + maxVowels("abciiidef", 3));
        }
        if (s == 0 || s == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("minSpeed([30, 11, 23, 4, 20], h=6) = " + minSpeed(new int[]{30, 11, 23, 4, 20}, 6));
            int[] rot = {40, 50, 60, 10, 20, 30};
            System.out.println("searchRotated(" + java.util.Arrays.toString(rot) + ", 20) = " + searchRotated(rot, 20)
                    + ", (…, 45) = " + searchRotated(rot, 45));
            System.out.println("majority([2, 2, 1, 1, 1, 2, 2]) = " + majority(new int[]{2, 2, 1, 1, 1, 2, 2}));
        }
    }
}
