import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Module 17 self-check: JUnit-free tests for interview patterns, each against a brute force.
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

    public static void main(String[] args) {
        Random r = new Random(17);
        boolean water = true, window = true, unique = true, shortest = true, atMostK = true, three = true;
        for (int t = 0; t < 100; t++) {
            int n = 2 + r.nextInt(12);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = 1 + r.nextInt(9);
            int bw = 0;
            for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) bw = Math.max(bw, Math.min(a[i], a[j]) * (j - i));
            water &= TwoPointersWindow.maxWater(a) == bw;
            int k = 1 + r.nextInt(n), bk = Integer.MIN_VALUE;
            for (int i = 0; i + k <= n; i++) { int s = 0; for (int j = i; j < i + k; j++) s += a[j]; bk = Math.max(bk, s); }
            window &= TwoPointersWindow.bestWindow(a, k) == bk;
            int target = 1 + r.nextInt(30), bs = 0;
            for (int i = 0; i < n; i++) { int s = 0; for (int j = i; j < n; j++) { s += a[j]; if (s >= target) { if (bs == 0 || j - i + 1 < bs) bs = j - i + 1; break; } } }
            shortest &= TwoPointersWindow.shortestAtLeast(a, target) == bs;
            int kk = 1 + r.nextInt(3), bm = 0;
            for (int i = 0; i < n; i++) { Set<Integer> d = new HashSet<>(); for (int j = i; j < n; j++) { d.add(a[j]); if (d.size() <= kk) bm = Math.max(bm, j - i + 1); } }
            atMostK &= TwoPointersWindow.longestAtMostK(a, kk) == bm;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(5)));
            String s = sb.toString();
            int bu = 0;
            for (int i = 0; i < n; i++) for (int j = i; j < n; j++) { if (s.substring(i, j + 1).chars().distinct().count() == j - i + 1) bu = Math.max(bu, j - i + 1); }
            unique &= TwoPointersWindow.longestUnique(s).length() == bu;
            int[] b = new int[n];
            for (int i = 0; i < n; i++) b[i] = r.nextInt(9) - 4;
            Set<List<Integer>> expect = new HashSet<>();
            for (int x = 0; x < n; x++) for (int y = x + 1; y < n; y++) for (int z = y + 1; z < n; z++)
                if (b[x] + b[y] + b[z] == 0) { int[] tr = {b[x], b[y], b[z]}; Arrays.sort(tr); expect.add(List.of(tr[0], tr[1], tr[2])); }
            List<List<Integer>> got = TwoPointersWindow.threeSum(b);
            three &= new HashSet<>(got).equals(expect) && got.size() == expect.size();
        }
        check(water, "max water = brute force");
        check(window, "fixed window = brute force");
        check(shortest, "shortest window = brute force");
        check(atMostK, "at most k distinct = brute force");
        check(unique, "longest unique substring = brute force");
        check(three, "threeSum = brute force, no duplicates");
        check(TwoPointersWindow.pairSum(new int[]{1, 2}, 10) == null, "pairSum none");

        boolean dupOk = true, shipOk = true, tempOk = true;
        for (int t = 0; t < 100; t++) {
            int n = 1 + r.nextInt(10);
            int[] a = new int[n + 1];
            for (int i = 0; i <= n; i++) a[i] = 1 + (i < n ? i : r.nextInt(n));
            for (int i = a.length - 1; i > 0; i--) { int j = r.nextInt(i + 1); int x = a[i]; a[i] = a[j]; a[j] = x; }
            int[] cnt = new int[n + 2];
            int d = -1;
            for (int x : a) if (++cnt[x] == 2) d = x;
            dupOk &= MorePatterns.findDuplicate(a) == d;
            int[] w = new int[1 + r.nextInt(10)];
            for (int i = 0; i < w.length; i++) w[i] = 1 + r.nextInt(20);
            int days = 1 + r.nextInt(w.length);
            int c = MorePatterns.shipWithinDays(w, days);
            shipOk &= MorePatterns.daysNeeded(w, c) <= days && (c == Arrays.stream(w).max().getAsInt() || MorePatterns.daysNeeded(w, c - 1) > days);
            int[] temp = new int[1 + r.nextInt(12)];
            for (int i = 0; i < temp.length; i++) temp[i] = r.nextInt(10);
            int[] wait = MorePatterns.dailyTemperatures(temp);
            for (int i = 0; i < temp.length; i++) { int e = 0; for (int j = i + 1; j < temp.length; j++) if (temp[j] > temp[i]) { e = j - i; break; } tempOk &= wait[i] == e; }
        }
        check(dupOk, "Floyd duplicate finder");
        check(shipOk, "binary search on answer is minimal and feasible");
        check(tempOk, "daily temperatures = brute force");
        check(Arrays.equals(MorePatterns.missingAndDuplicate(new int[]{1, 2, 2, 4}), new int[]{3, 2}), "cyclic sort");
        check(MorePatterns.unpaired(new int[]{9}) == 9, "xor single");
        check(MorePatterns.longestSumK(new int[]{1, -1, 5, -2, 3}, 3) == 4, "longest sum k");

        check(Labs17.validPalindrome("") && Labs17.validPalindrome(".,") && !Labs17.validPalindrome("ab"), "palindrome edge cases");
        check(Labs17.minWindow("a", "aa").isEmpty() && Labs17.minWindow("aa", "aa").equals("aa"), "min window multiplicity");
        check(Labs17.maxVowels("leetcode", 3) == 2, "max vowels");
        check(Labs17.minSpeed(new int[]{3, 6, 7, 11}, 8) == 4, "min speed");
        boolean rot = true;
        for (int t = 0; t < 50; t++) {
            int n = 1 + r.nextInt(12), shift = r.nextInt(n);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[(i + shift) % n] = 3 * i + 1;
            for (int x = 0; x <= 3 * n + 1; x++) {
                int idx = Labs17.searchRotated(a, x);
                boolean present = x % 3 == 1 && x <= 3 * (n - 1) + 1;
                rot &= present ? idx >= 0 && a[idx] == x : idx == -1;
            }
        }
        check(rot, "search rotated array");
        check(Labs17.majority(new int[]{3, 3, 4}) == 3, "majority");

        System.out.println("Module 17 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
